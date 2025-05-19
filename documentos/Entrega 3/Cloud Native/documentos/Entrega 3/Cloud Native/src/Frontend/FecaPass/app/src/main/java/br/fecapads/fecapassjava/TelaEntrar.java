package br.fecapads.fecapassjava;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONObject;
import java.io.IOException;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class TelaEntrar extends AppCompatActivity {
    private EditText etEmail, etSenha;
    private CheckBox cbLembrar;
    private Button btnEntrar;
    private ImageView btnVoltar;
    private static final String BASE_URL = "https://fecapassnewcloud.azurewebsites.net/api";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.telaentrar);

        // Inicializar os componentes
        etEmail = findViewById(R.id.etEmail);
        etSenha = findViewById(R.id.etSenha);
        cbLembrar = findViewById(R.id.cbLembrar);
        btnEntrar = findViewById(R.id.btnCriarConta);
        btnVoltar = findViewById(R.id.btnVoltar);

        // Verificar se há dados salvos no SharedPreferences (para "Lembrar de mim")
        SharedPreferences prefs = getSharedPreferences("auth", MODE_PRIVATE);
        boolean lembrar = prefs.getBoolean("lembrar", false);
        if (lembrar) {
            String savedEmail = prefs.getString("email", "");
            String savedSenha = prefs.getString("senha", "");
            etEmail.setText(savedEmail);
            etSenha.setText(savedSenha);
            cbLembrar.setChecked(true);
        }

        // Ação do botão Voltar
        btnVoltar.setOnClickListener(v -> finish());
    }

    // Método chamado pelo botão "Entrar"
    public void mudarTelaEvento(View view) {
        String email = etEmail.getText().toString().trim();
        String senha = etSenha.getText().toString().trim();

        if (email.isEmpty() || senha.isEmpty()) {
            Toast.makeText(this, "Por favor, preencha todos os campos", Toast.LENGTH_SHORT).show();
            return;
        }

        // Se "Lembrar de mim" estiver marcado, salvar email e senha
        SharedPreferences prefs = getSharedPreferences("auth", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        if (cbLembrar.isChecked()) {
            editor.putBoolean("lembrar", true);
            editor.putString("email", email);
            editor.putString("senha", senha);
        } else {
            editor.putBoolean("lembrar", false);
            editor.remove("email");
            editor.remove("senha");
        }
        editor.apply();

        // Fazer a autenticação
        realizarLogin(email, senha);
    }

    private void realizarLogin(String email, String password) {
        OkHttpClient client = new OkHttpClient();

        // Criar o JSON para a requisição
        JSONObject json = new JSONObject();
        try {
            json.put("email", email);
            json.put("senha", password);
        } catch (Exception e) {
            Log.e("TelaEntrar", "Erro ao criar JSON: ", e);
            runOnUiThread(() -> Toast.makeText(TelaEntrar.this, "Erro ao criar requisição", Toast.LENGTH_SHORT).show());
            return;
        }

        Log.d("TelaEntrar", "Enviando JSON: " + json.toString());
        RequestBody body = RequestBody.create(json.toString(), MediaType.get("application/json; charset=utf-8"));

        Request request = new Request.Builder()
                .url(BASE_URL + "/login")
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() -> {
                    Log.e("TelaEntrar", "Erro de conexão: ", e);
                    Toast.makeText(TelaEntrar.this, "Erro de conexão com o servidor", Toast.LENGTH_SHORT).show();
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseBody = response.body().string();
                Log.d("TelaEntrar", "Código HTTP: " + response.code());
                Log.d("TelaEntrar", "Resposta do servidor: " + responseBody);

                if (response.isSuccessful()) {
                    try {
                        JSONObject jsonResponse = new JSONObject(responseBody);

                        // Verificar se o campo "token" existe
                        String token = null;
                        String userId = null;

                        if (jsonResponse.has("token")) {
                            // Verificar se "token" é uma string ou um objeto
                            if (jsonResponse.get("token") instanceof JSONObject) {
                                JSONObject tokenObject = jsonResponse.getJSONObject("token");
                                token = tokenObject.optString("token", null);
                                userId = jsonResponse.optString("userId", null);
                                if (userId == null && tokenObject.has("id")) {
                                    userId = tokenObject.optString("id", null);
                                }
                            } else {
                                // Se "token" for uma string diretamente
                                token = jsonResponse.getString("token");
                                userId = jsonResponse.optString("userId", null);
                                if (userId == null) {
                                    // Extrair o userId do token
                                    userId = extractUserIdFromToken(token);
                                }
                            }
                        }

                        if (token == null || userId == null) {
                            runOnUiThread(() -> Toast.makeText(TelaEntrar.this, "Resposta inválida do servidor: token ou userId não encontrado", Toast.LENGTH_LONG).show());
                            return;
                        }

                        // Salvar os dados de autenticação no SharedPreferences
                        SharedPreferences prefs = getSharedPreferences("auth", MODE_PRIVATE);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString("token", token);
                        editor.putString("userId", userId);
                        editor.apply();

                        Log.d("TelaEntrar", "Token salvo: " + token);
                        Log.d("TelaEntrar", "UserId salvo: " + userId);

                        // Navegar para a tela de eventos
                        runOnUiThread(() -> {
                            Toast.makeText(TelaEntrar.this, "Login bem-sucedido!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(TelaEntrar.this, Eventos.class);
                            startActivity(intent);
                            finish();
                        });
                    } catch (Exception e) {
                        runOnUiThread(() -> {
                            Log.e("TelaEntrar", "Erro ao processar resposta: ", e);
                            Toast.makeText(TelaEntrar.this, "Erro ao processar resposta: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        });
                    }
                } else {
                    runOnUiThread(() -> {
                        String mensagemErro = "Credenciais inválidas";
                        try {
                            JSONObject jsonErro = new JSONObject(responseBody);
                            if (jsonErro.has("error")) {
                                mensagemErro = jsonErro.getString("error");
                            }
                        } catch (Exception e) {
                            Log.e("TelaEntrar", "Erro ao parsear mensagem de erro: ", e);
                        }
                        Toast.makeText(TelaEntrar.this, mensagemErro, Toast.LENGTH_SHORT).show();
                    });
                }
            }
        });
    }

    // Método para extrair o userId do token JWT
    private String extractUserIdFromToken(String token) {
        try {
            // O token JWT tem 3 partes separadas por pontos: Header.Payload.Signature
            String[] parts = token.split("\\.");
            if (parts.length != 3) {
                Log.e("TelaEntrar", "Token JWT inválido: número de partes incorreto");
                return null;
            }

            // Decodificar o Payload (segunda parte) do token
            String payload = new String(Base64.decode(parts[1], Base64.URL_SAFE));
            Log.d("TelaEntrar", "Payload decodificado: " + payload);

            // Parsear o payload como JSON
            JSONObject payloadJson = new JSONObject(payload);

            // Extrair o "id" do payload
            if (payloadJson.has("id")) {
                return String.valueOf(payloadJson.getInt("id"));
            } else {
                Log.e("TelaEntrar", "Campo 'id' não encontrado no payload do token");
                return null;
            }
        } catch (Exception e) {
            Log.e("TelaEntrar", "Erro ao extrair userId do token: ", e);
            return null;
        }
    }

    public void mudarParaEsqueciSenha(View view) {
        Intent intent = new Intent(TelaEntrar.this, EsqueciSenha.class);
        startActivity(intent);
    }

    public void mudarTelaCadastro(View view) {
        Intent intent = new Intent(TelaEntrar.this, MainActivity.class);
        startActivity(intent);
    }
}