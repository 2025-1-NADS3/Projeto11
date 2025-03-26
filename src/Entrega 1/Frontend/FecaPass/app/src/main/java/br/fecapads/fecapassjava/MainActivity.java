package br.fecapads.fecapassjava;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public String capturarNome() {
        EditText etNome = findViewById(R.id.etNome);
        return etNome.getText().toString();
    }

    public void mudarTela(View view) {
        // Adicionar um Toast para verificar se o método é chamado
        Toast.makeText(MainActivity.this, "Clicou no texto!", Toast.LENGTH_SHORT).show();

        // Cria a Intent para navegar até a TelaEntrarActivity
        Intent intent = new Intent(MainActivity.this, TelaEntrar.class);
        startActivity(intent);
    }




    // Método para capturar o email do usuário
    public String capturarEmail() {
        EditText etEmail = findViewById(R.id.etEmail);
        return etEmail.getText().toString();
    }

    // Método para capturar a senha do usuário
    public String capturarSenha() {
        EditText etSenha = findViewById(R.id.etSenha);
        return etSenha.getText().toString();
    }

    // Método para verificar os dados e exibir a mensagem
    public void verificarUsuario(View view) {
        String nome = capturarNome();
        String email = capturarEmail();
        String senha = capturarSenha();

        // Verifica se os dados correspondem ao esperado
        if (nome.equals("Erika") && email.equals("erika@gmail.com") && senha.equals("12345")) {
            exibirMensagem("Usuário logado: " + nome);
        } else {
            exibirMensagem("Dados inválidos. Tente novamente.");
        }
    }

    // Método para exibir a mensagem com o Toast
    public void exibirMensagem(String mensagem) {
        // Atualiza o texto da TextView com a mensagem
        TextView tvMensagem;
        tvMensagem = findViewById(R.id.etNome);
        tvMensagem.setText(mensagem);
    }
}
