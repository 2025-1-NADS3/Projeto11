package br.fecapads.fecapassjava;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class CadastroResultado extends AppCompatActivity {
    private ImageView resultadoImagem;
    private TextView resultadoMensagem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_resultado);

        resultadoImagem = findViewById(R.id.resultadoImagem);
        resultadoMensagem = findViewById(R.id.resultadoMensagem);

        Intent intent = getIntent();
        boolean sucesso = intent.getBooleanExtra("sucesso", false);
        String mensagem = intent.getStringExtra("mensagem");

        // Alterando a imagem e texto com base no sucesso ou erro
        if (sucesso) {
            resultadoImagem.setImageResource(R.drawable.ic_check); // imagem de check
            resultadoMensagem.setText(mensagem);

            // Redireciona para a tela de login após 3 segundos se o cadastro foi bem-sucedido
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                Toast.makeText(CadastroResultado.this, "Redirecionando para login...", Toast.LENGTH_SHORT).show();

                Intent loginIntent = new Intent(CadastroResultado.this, TelaEntrar.class);
                startActivity(loginIntent);
                finish(); // Finaliza a tela de resultado para não voltar a ela
            }, 3000); // 3000 milissegundos = 3 segundos
        } else {
            resultadoImagem.setImageResource(R.drawable.ic_error); // imagem de erro
            resultadoMensagem.setText(mensagem);
        }
    }
}