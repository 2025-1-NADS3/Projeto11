package br.fecapads.fecapassjava;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import br.fecapads.fecapassjava.services.apiService;

public class MainActivity extends AppCompatActivity {

    private ExecutorService executorService = Executors.newSingleThreadExecutor(); // Usando um executor com uma única thread

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Ajusta o layout para sistemas de barras de status e navegação
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    // Método para capturar o nome do usuário
    public String capturarNome() {
        EditText etNome = findViewById(R.id.etNome);
        return etNome.getText().toString();
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

    // Método para exibir mensagens na tela
    public void exibirMensagem(String mensagem) {
        // Atualiza o texto da TextView com a mensagem
        TextView tvMensagem;
        tvMensagem = findViewById(R.id.etNome);
        tvMensagem.setText(mensagem);
    }


// Método para cadastrar o usuário
    public void cadastrarUsuario(View view) {
        String nome = capturarNome();
        String email = capturarEmail();
        String senha = capturarSenha();

        apiService api = new apiService();
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        executorService.execute(() -> {
            try {
                String resposta = api.cadastrarUsuario(nome, email, senha);
                Log.d("DEBUG", "Resposta da API: " + resposta);

                boolean sucesso = false;
                String mensagem = "Erro desconhecido";

                try {
                    JSONObject jsonResponse = new JSONObject(resposta);

                    if (jsonResponse.has("success") && jsonResponse.getBoolean("success")) {
                        sucesso = true;
                        mensagem = jsonResponse.optString("message", "Cadastro realizado com sucesso!");
                    } else {
                        mensagem = jsonResponse.optString("message", "Erro ao processar cadastro.");
                    }

                } catch (JSONException e) {
                    Log.e("DEBUG", "Erro ao analisar resposta JSON", e);
                    mensagem = resposta;
                }

                Intent intent = new Intent(MainActivity.this, CadastroResultado.class);
                intent.putExtra("sucesso", sucesso);
                intent.putExtra("mensagem", mensagem);

                runOnUiThread(() -> {
                    Log.d("DEBUG", "Iniciando a Activity CadastroResultado");
                    startActivity(intent);
                    finish();
                });

            } catch (IOException e) {
                Log.e("DEBUG", "Erro ao conectar-se à API", e);
                runOnUiThread(() -> {
                    Intent intent = new Intent(MainActivity.this, CadastroResultado.class);
                    intent.putExtra("sucesso", false);
                    intent.putExtra("mensagem", "Erro ao conectar-se à API");
                    startActivity(intent);
                    finish();
                });
            }
        });
    }



    // Método para verificar se o usuário está correto
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

    // Método para navegar para a próxima tela
    public void mudarTela(View view) {
        // Adicionar um Toast para verificar se o método é chamado
        Toast.makeText(MainActivity.this, "Clicou no texto!", Toast.LENGTH_SHORT).show();

        // Cria a Intent para navegar até a TelaEntrarActivity
        Intent intent = new Intent(MainActivity.this, TelaEntrar.class);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executorService.shutdown(); // Fecha o executor quando a activity for destruída
    }
}
