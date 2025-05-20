package br.fecapads.fecapassjava;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Activity_faq extends AppCompatActivity {

    TextView pergunta1, resposta1;
    TextView pergunta2, resposta2;
    TextView pergunta3, resposta3;
    TextView pergunta4, resposta4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq);

        // Mapeando os elementos
        pergunta1 = findViewById(R.id.pergunta1);
        resposta1 = findViewById(R.id.resposta1);
        pergunta2 = findViewById(R.id.pergunta2);
        resposta2 = findViewById(R.id.resposta2);
        pergunta3 = findViewById(R.id.pergunta3);
        resposta3 = findViewById(R.id.resposta3);
        pergunta4 = findViewById(R.id.pergunta4);
        resposta4 = findViewById(R.id.resposta4);

        // Botão de voltar
        ImageView btnVoltar = findViewById(R.id.btnVoltar);
        btnVoltar.setOnClickListener(v -> finish());

        // Esconde todas as respostas no início
        resposta1.setVisibility(View.GONE);
        resposta2.setVisibility(View.GONE);
        resposta3.setVisibility(View.GONE);
        resposta4.setVisibility(View.GONE);

        // Define os listeners para alternar a visibilidade
        pergunta1.setOnClickListener(v -> toggleResposta(resposta1));
        pergunta2.setOnClickListener(v -> toggleResposta(resposta2));
        pergunta3.setOnClickListener(v -> toggleResposta(resposta3));
        pergunta4.setOnClickListener(v -> toggleResposta(resposta4));
    }

    private void toggleResposta(TextView resposta) {
        if (resposta.getVisibility() == View.VISIBLE) {
            resposta.setVisibility(View.GONE);
        } else {
            resposta.setVisibility(View.VISIBLE);
        }
    }
}
