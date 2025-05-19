package br.fecapads.fecapassjava.services;
import java.io.IOException;

import okhttp3.*;
public class apiService {
    private static final String BASE_URL = "https://fecapassnewcloud.azurewebsites.net/api"; // Alterar para o IP local caso necessário
    private final OkHttpClient client = new OkHttpClient();

    public String cadastrarUsuario(String nome, String email, String senha) throws IOException {
        String json = "{\"nome\":\"" + nome + "\", \"email\":\"" + email + "\", \"senha\":\"" + senha + "\"}";
        RequestBody body = RequestBody.create(json, MediaType.get("application/json; charset=utf-8"));

        Request request = new Request.Builder()
                .url(BASE_URL + "/cadastro") // Endpoint correto
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                // Se o cadastro foi bem-sucedido, retornamos a mensagem do backend
                return response.body() != null ? response.body().string() : "Erro no servidor";
            } else {
                return "Erro ao cadastrar usuário";
            }
        }
    }
    public String getEventos() throws IOException {
        Request request = new Request.Builder()
                .url(BASE_URL + "/listaEventos") // Endpoint para pegar os eventos
                .get()
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                return response.body() != null ? response.body().string() : null;
            } else {
                return null;
            }
        }
    }
    public String realizarPagamento(String usuarioId, double valor) throws IOException {
        String json = "{\"usuarioId\":\"" + usuarioId + "\", \"valor\":\"" + valor + "\"}";
        RequestBody body = RequestBody.create(json, MediaType.get("application/json; charset=utf-8"));

        Request request = new Request.Builder()
                .url(BASE_URL + "/criarPagamento") // Chama o endpoint de pagamento no backend
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                // Se o pagamento foi bem-sucedido, retornamos o URL do QR code
                return response.body() != null ? response.body().string() : "Erro no servidor";
            } else {
                return "Erro ao criar pagamento";
            }
        }
    }

}