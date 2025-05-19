package br.fecapads.fecapassjava;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class CadastroResultado  extends AppCompatActivity {

    private ImageView resultadoImagem;
    private TextView resultadoMensagem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_resultado);

        resultadoImagem = findViewById(R.id.resultadoImagem);
        resultadoMensagem = findViewById(R.id.resultadoMensagem);

        // Pegando os dados passados pela Intent
        Intent intent = getIntent();
        boolean sucesso = intent.getBooleanExtra("sucesso", false);
        String mensagem = intent.getStringExtra("mensagem");

        // Alterando a imagem e o texto com base no sucesso ou erro
        if (sucesso) {
            resultadoImagem.setImageResource(R.drawable.ic_check); // imagem de check
            resultadoMensagem.setText(mensagem);
        } else {
            resultadoImagem.setImageResource(R.drawable.ic_error); // imagem de erro
            resultadoMensagem.setText(mensagem);
        }
    }
}