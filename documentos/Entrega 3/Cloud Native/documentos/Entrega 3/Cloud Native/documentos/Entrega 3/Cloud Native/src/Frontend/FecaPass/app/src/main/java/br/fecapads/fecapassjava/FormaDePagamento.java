package br.fecapads.fecapassjava;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

public class FormaDePagamento extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forma_de_pagamento);

        // Inicializar componentes
        ImageView btnVoltar = findViewById(R.id.btnVoltar);
        ImageView btnDetalhePix = findViewById(R.id.btnDetalhePix);

        // Receber dados da Intent
        Intent intent = getIntent();
        int eventoId = intent.getIntExtra("eventoId", -1);
        String titulo = intent.getStringExtra("titulo");
        double valorTotal = intent.getDoubleExtra("valorTotal", 0.0);
        int quantidade = intent.getIntExtra("quantidade", 0);
        String local = intent.getStringExtra("local");
        String data = intent.getStringExtra("data");
        String descricao = intent.getStringExtra("descricao");

        // Ação do botão Pix
        btnDetalhePix.setOnClickListener(v -> {
            Intent intentPix = new Intent(FormaDePagamento.this, Pix.class);
            intentPix.putExtra("eventoId", eventoId);
            intentPix.putExtra("titulo", titulo);
            intentPix.putExtra("valorTotal", valorTotal);
            intentPix.putExtra("quantidade", quantidade);
            intentPix.putExtra("local", local);
            intentPix.putExtra("data", data);
            intentPix.putExtra("descricao", descricao);
            startActivity(intentPix);
        });

        btnVoltar.setOnClickListener(v -> finish());
    }
}