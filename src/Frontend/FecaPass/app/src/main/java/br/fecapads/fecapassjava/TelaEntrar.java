package br.fecapads.fecapassjava;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class TelaEntrar extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.telaentrar); // Defina o layout para essa tela
    }

    public void mudarTelaCadastro(View view) {
        Intent intent = new Intent(TelaEntrar.this, MainActivity.class);
        startActivity(intent);
    }
    public void mudarParaEsqueciSenha(View view) {
        Intent intent = new Intent(TelaEntrar.this, EsqueciSenha.class);
        startActivity(intent);
    }
}

