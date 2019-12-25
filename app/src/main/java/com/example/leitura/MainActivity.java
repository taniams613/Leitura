package com.example.leitura;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu){
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.idioma:
                break;
            case R.id.abrir:
                abrirFicheiro();
                break;
            case R.id.sobre:
                Intent intent = new Intent(this, SobreActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }


    private void abrirFicheiro() {
        final int READ_REQUEST_CODE = 42;

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");
        startActivityForResult(intent, READ_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        if (requestCode == 42 && resultCode == Activity.RESULT_OK) {
            Uri uri = null;
            if (resultData != null) {
                uri = resultData.getData();

                TextView mostrarTexto = findViewById(R.id.mostrarTexto);
                mostrarTexto.setText(readTextFromUri(uri));

                /*
                Toast t = Toast.makeText(getApplicationContext(), uri.getPath(), Toast.LENGTH_LONG);
                t.show();
                */
            }
        }
    }

    private String readTextFromUri(Uri uri) {
        StringBuilder builder = new StringBuilder();
        InputStream is =  null;
        BufferedReader br = null;

        try {
            is = getContentResolver().openInputStream(uri);
            br = new BufferedReader(new InputStreamReader(is));

            String line;
            while ((line = br.readLine()) != null) {
                builder.append(line + '\n');
            }
        }
        catch (Exception e) { showError(e.getMessage()); }
        finally {
            try {
                if (br != null) { br.close(); }
                if (is != null) { is.close(); }
            }
            catch (IOException ioe) { showError(ioe.getMessage()); }
        }
        return builder.toString();
    }

    private void showError(String message) {
        Toast t = Toast.makeText(getApplicationContext(), "Erro: " + message, Toast.LENGTH_LONG);
        t.show();
    }

}
