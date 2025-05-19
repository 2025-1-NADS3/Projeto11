package br.fecapads.fecapassjava;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Base64;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONObject;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Pedido extends AppCompatActivity {
    private ImageView qrCodeImageView;
    private TextView txtSubtotal, txtEvento, txtPedidoNumero, txtQuantidade;
    private Button btnCopiarPix;
    private String ticketUrl;
    private String paymentIdMP;
    private int eventoId;
    private int quantidade;
    private double valorTotal; // Adicionado para armazenar o valor total
    private String pedidoId; // Adicionado para armazenar o número do pedido
    private static final String BASE_URL = "https://fecapassnewcloud.azurewebsites.net/api";
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("Pedido", "onCreate iniciado");
        setContentView(R.layout.activity_pedido);

        // Inicializar componentes
        Log.d("Pedido", "Inicializando componentes");
        qrCodeImageView = findViewById(R.id.qrCodeImageView);
        txtSubtotal = findViewById(R.id.subtotal);
        txtEvento = findViewById(R.id.evento);
        txtPedidoNumero = findViewById(R.id.txtTitulo);
        txtQuantidade = findViewById(R.id.txtQuantidade);
        btnCopiarPix = findViewById(R.id.btnCopiarPix);
        ImageView btnVoltar = findViewById(R.id.btnVoltar);

        // Verificar se qrCodeImageView foi inicializado corretamente
        if (qrCodeImageView == null) {
            Log.e("Pedido", "qrCodeImageView é nulo. Verifique o layout activity_pedido.xml");
            Toast.makeText(this, "Erro: Não foi possível carregar o QR Code", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        // Receber dados da Intent
        Log.d("Pedido", "Recebendo dados da Intent");
        Intent intent = getIntent();
        String titulo = intent.getStringExtra("titulo");
        valorTotal = intent.getDoubleExtra("valorTotal", 0.0);
        eventoId = intent.getIntExtra("eventoId", -1);
        quantidade = intent.getIntExtra("quantidade", 0);
        Log.d("Pedido", "Dados recebidos - titulo: " + titulo + ", valorTotal: " + valorTotal + ", eventoId: " + eventoId + ", quantidade: " + quantidade);

        // Validar dados
        Log.d("Pedido", "Validando dados");
        if (eventoId <= 0 || quantidade <= 0 || valorTotal <= 0) {
            Log.e("Pedido", "Dados inválidos - eventoId: " + eventoId + ", quantidade: " + quantidade + ", valorTotal: " + valorTotal);
            Toast.makeText(this, "ID do evento, quantidade ou valor inválido", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        // Gerar número do pedido e armazenar
        pedidoId = generatePedidoId();

        // Exibir dados
        Log.d("Pedido", "Exibindo dados na interface");
        txtSubtotal.setText(String.format("Subtotal: R$ %.2f", valorTotal));
        txtEvento.setText("Evento: " + (titulo != null ? titulo : "Desconhecido"));
        txtPedidoNumero.setText("Pedido nº: " + pedidoId);
        txtQuantidade.setText("Quantidade: " + quantidade + " ingresso(s)");

        // Obter o userId e exibir ou usar conforme necessário
        Log.d("Pedido", "Obtendo userId");
        String userId = getUserId();
        if (userId != null) {
            Log.d("Pedido", "ID do usuário: " + userId);
        } else {
            Log.e("Pedido", "userId é nulo");
        }

        // Iniciar criação do pagamento
        Log.d("Pedido", "Iniciando criação do pagamento");
        criarPagamento(eventoId, quantidade);

        // Ação do botão "Copiar código Pix"
        Log.d("Pedido", "Configurando ação do botão Copiar Pix");
        btnCopiarPix.setOnClickListener(v -> {
            Log.d("Pedido", "Botão Copiar Pix clicado");
            if (ticketUrl != null) {
                Log.d("Pedido", "Copiando ticketUrl: " + ticketUrl);
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Código Pix", ticketUrl);
                clipboard.setPrimaryClip(clip);
                Toast.makeText(this, "Código Pix copiado!", Toast.LENGTH_SHORT).show();
            } else {
                Log.e("Pedido", "ticketUrl é nulo");
                Toast.makeText(this, "Código Pix não disponível", Toast.LENGTH_SHORT).show();
            }
        });

        // Ação do botão Voltar
        Log.d("Pedido", "Configurando ação do botão Voltar");
        btnVoltar.setOnClickListener(v -> {
            Log.d("Pedido", "Botão Voltar clicado");
            finish();
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executorService.shutdown();
    }

    private String getUserId() {
        Log.d("Pedido", "Obtendo userId do SharedPreferences");
        SharedPreferences prefs = getSharedPreferences("auth", MODE_PRIVATE);
        String userId = prefs.getString("userId", null);
        if (userId == null) {
            Log.e("Pedido", "ID do usuário não encontrado no SharedPreferences");
            runOnUiThread(() -> Toast.makeText(this, "ID do usuário não encontrado", Toast.LENGTH_LONG).show());
        }
        return userId;
    }

    private String getAuthToken() {
        Log.d("Pedido", "Obtendo token do SharedPreferences");
        SharedPreferences prefs = getSharedPreferences("auth", MODE_PRIVATE);
        String token = prefs.getString("token", null);
        if (token == null) {
            Log.e("Pedido", "Token não encontrado no SharedPreferences");
        }
        return token;
    }

    private String generatePedidoId() {
        String pedidoId = String.valueOf(System.currentTimeMillis());
        Log.d("Pedido", "Gerando pedidoId: " + pedidoId);
        return pedidoId;
    }

    private String extractPaymentIdFromTicketUrl(String ticketUrl) {
        if (ticketUrl == null) {
            Log.e("Pedido", "ticketUrl é nulo, não é possível extrair paymentIdMP");
            return null;
        }
        // Padrão para extrair o paymentIdMP da URL (ex.: https://www.mercadopago.com.br/payments/109349031319/ticket?...)
        Pattern pattern = Pattern.compile("payments/(\\d+)/ticket");
        Matcher matcher = pattern.matcher(ticketUrl);
        if (matcher.find()) {
            String paymentId = matcher.group(1);
            Log.d("Pedido", "paymentIdMP extraído do ticketUrl: " + paymentId);
            return paymentId;
        } else {
            Log.e("Pedido", "Não foi possível extrair paymentIdMP do ticketUrl: " + ticketUrl);
            return null;
        }
    }

    private void criarPagamento(int eventoId, int quantidade) {
        Log.d("Pedido", "Iniciando criarPagamento - eventoId: " + eventoId + ", quantidade: " + quantidade);
        OkHttpClient client = new OkHttpClient();
        String token = getAuthToken();
        if (token == null) {
            Log.e("Pedido", "Token é nulo, finalizando atividade");
            finish();
            return;
        }
        Log.d("Pedido", "Token obtido: " + token);

        Request request = new Request.Builder()
                .url(BASE_URL + "/criarPagamento")
                .addHeader("Authorization", "Bearer " + token)
                .addHeader("eventoid", String.valueOf(eventoId))
                .addHeader("quantidade", String.valueOf(quantidade))
                .put(RequestBody.create("", MediaType.get("application/json; charset=utf-8")))
                .build();
        Log.d("Pedido", "Requisição criada para /criarPagamento");

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() -> {
                    Log.e("Pedido", "Erro de conexão ao criar pagamento: ", e);
                    Toast.makeText(Pedido.this, "Erro de conexão com o servidor", Toast.LENGTH_SHORT).show();
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseBody = response.body().string();
                Log.d("Pedido", "Código HTTP (criarPagamento): " + response.code());
                Log.d("Pedido", "Resposta do servidor (criarPagamento): " + responseBody);

                if (response.isSuccessful()) {
                    try {
                        JSONObject jsonResponse = new JSONObject(responseBody);
                        Log.d("Pedido", "Resposta JSON parseada: " + jsonResponse.toString());
                        if (jsonResponse.has("qrCodeBase64") && jsonResponse.has("ticketUrl")) {
                            String qrCodeBase64 = jsonResponse.getString("qrCodeBase64");
                            ticketUrl = jsonResponse.getString("ticketUrl");
                            Log.d("Pedido", "qrCodeBase64: " + qrCodeBase64);
                            Log.d("Pedido", "ticketUrl: " + ticketUrl);
                            // Mover a decodificação do QR Code para uma thread secundária
                            decodeAndDisplayQRCode(qrCodeBase64);
                            // Tentar obter paymentIdMP do JSON
                            if (jsonResponse.has("paymentIdMP")) {
                                paymentIdMP = jsonResponse.getString("paymentIdMP");
                                Log.d("Pedido", "paymentIdMP encontrado no JSON: " + paymentIdMP);
                            } else {
                                // Se não estiver no JSON, extrair do ticketUrl
                                paymentIdMP = extractPaymentIdFromTicketUrl(ticketUrl);
                                if (paymentIdMP == null) {
                                    Log.e("Pedido", "paymentIdMP não encontrado na resposta e não pôde ser extraído do ticketUrl");
                                    runOnUiThread(() -> Toast.makeText(Pedido.this, "paymentIdMP não encontrado", Toast.LENGTH_SHORT).show());
                                    return;
                                }
                            }
                            // Verificar o status do pagamento
                            verificarStatusPagamento();
                        } else {
                            Log.e("Pedido", "Campos qrCodeBase64 ou ticketUrl não encontrados na resposta");
                            runOnUiThread(() -> Toast.makeText(Pedido.this, "Resposta inválida: campos necessários não encontrados", Toast.LENGTH_SHORT).show());
                        }
                    } catch (Exception e) {
                        runOnUiThread(() -> {
                            Log.e("Pedido", "Erro ao processar resposta (criarPagamento): ", e);
                            Toast.makeText(Pedido.this, "Erro ao processar resposta do servidor", Toast.LENGTH_SHORT).show();
                        });
                    }
                } else {
                    if (response.code() == 401 || response.code() == 403) {
                        runOnUiThread(() -> {
                            Log.e("Pedido", "Sessão expirada ou token inválido - Código HTTP: " + response.code());
                            Toast.makeText(Pedido.this, "Sessão expirada ou token inválido. Faça login novamente.", Toast.LENGTH_LONG).show();
                            SharedPreferences prefs = getSharedPreferences("auth", MODE_PRIVATE);
                            SharedPreferences.Editor editor = prefs.edit();
                            editor.clear();
                            editor.apply();
                            Intent intent = new Intent(Pedido.this, TelaEntrar.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        });
                        return;
                    }
                    runOnUiThread(() -> {
                        String mensagemErro = "Falha na requisição (criarPagamento): " + response.code();
                        try {
                            JSONObject jsonErro = new JSONObject(responseBody);
                            if (jsonErro.has("error")) {
                                mensagemErro = jsonErro.getString("error");
                            }
                        } catch (Exception e) {
                            Log.e("Pedido", "Erro ao parsear mensagem de erro (criarPagamento): ", e);
                        }
                        Log.e("Pedido", "Erro na requisição: " + mensagemErro);
                        Toast.makeText(Pedido.this, mensagemErro, Toast.LENGTH_LONG).show();
                    });
                }
            }
        });
    }

    private void decodeAndDisplayQRCode(String qrCodeBase64) {
        Log.d("Pedido", "Iniciando decodificação do QR Code em thread secundária");
        executorService.execute(() -> {
            try {
                Log.d("Pedido", "Decodificando Base64");
                byte[] decodedString = Base64.decode(qrCodeBase64, Base64.DEFAULT);
                Log.d("Pedido", "Base64 decodificado, tamanho dos bytes: " + decodedString.length);
                Log.d("Pedido", "Decodificando bytes para Bitmap");
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                if (decodedByte == null) {
                    Log.e("Pedido", "Falha ao decodificar bytes para Bitmap: Bitmap é nulo");
                    mainHandler.post(() -> Toast.makeText(Pedido.this, "Erro ao decodificar QR Code", Toast.LENGTH_SHORT).show());
                    return;
                }
                Log.d("Pedido", "Bitmap decodificado com sucesso - largura: " + decodedByte.getWidth() + ", altura: " + decodedByte.getHeight());
                // Atualizar a UI na thread principal
                mainHandler.post(() -> {
                    Log.d("Pedido", "Atualizando ImageView com o Bitmap");
                    qrCodeImageView.setImageBitmap(decodedByte);
                    Log.d("Pedido", "QR Code exibido com sucesso");
                });
            } catch (Exception e) {
                Log.e("Pedido", "Erro ao decodificar ou exibir QR Code: ", e);
                mainHandler.post(() -> Toast.makeText(Pedido.this, "Erro ao exibir QR Code: " + e.getMessage(), Toast.LENGTH_LONG).show());
            }
        });
    }

    private void verificarStatusPagamento() {
        Log.d("Pedido", "Iniciando verificação de status do pagamento");
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();

        String token = getAuthToken();
        if (token == null) {
            Log.e("Pedido", "Token é nulo ao verificar status, finalizando atividade");
            finish();
            return;
        }
        Log.d("Pedido", "Token para verificar status: " + token);

        if (paymentIdMP == null) {
            Log.e("Pedido", "paymentIdMP é nulo, não é possível verificar o status");
            runOnUiThread(() -> Toast.makeText(Pedido.this, "Erro: paymentIdMP não disponível", Toast.LENGTH_LONG).show());
            return;
        }
        Log.d("Pedido", "paymentIdMP: " + paymentIdMP);

        Request request = new Request.Builder()
                .url(BASE_URL + "/getPagamento")
                .addHeader("Authorization", "Bearer " + token)
                .addHeader("paymentid", paymentIdMP)
                .get()
                .build();
        Log.d("Pedido", "Requisição criada para /getPagamento");

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() -> {
                    Log.e("Pedido", "Erro ao verificar status do pagamento: ", e);
                    Toast.makeText(Pedido.this, "Erro ao verificar status do pagamento", Toast.LENGTH_SHORT).show();
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseBody = response.body().string();
                Log.d("Pedido", "Status Pagamento - Código HTTP: " + response.code());
                Log.d("Pedido", "Status Pagamento - Resposta: " + responseBody);

                if (response.isSuccessful()) {
                    try {
                        JSONObject jsonResponse = new JSONObject(responseBody);
                        Log.d("Pedido", "Resposta JSON parseada (getPagamento): " + jsonResponse.toString());
                        JSONObject responseObject = jsonResponse.getJSONObject("response");
                        String status = responseObject.getString("status");
                        Log.d("Pedido", "Status do pagamento: " + status);

                        if ("approved".equals(status)) {
                            runOnUiThread(() -> {
                                Log.d("Pedido", "Pagamento aprovado, navegando para ConfirmacaoPagamento");
                                Toast.makeText(Pedido.this, "Pagamento aprovado!", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(Pedido.this, ConfirmacaoPagamento.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();
                            });
                        } else if ("rejected".equals(status)) {
                            runOnUiThread(() -> {
                                Log.d("Pedido", "Pagamento rejeitado, navegando para Eventos");
                                Toast.makeText(Pedido.this, "Pagamento rejeitado", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(Pedido.this, Eventos.class);
                                startActivity(intent);
                                finish();
                            });
                        } else {
                            // Continuar verificando (status pendente)
                            Log.d("Pedido", "Status pendente, verificando novamente em 5 segundos");
                            try {
                                Thread.sleep(5000); // Espera 5 segundos antes de verificar novamente
                                verificarStatusPagamento();
                            } catch (InterruptedException e) {
                                Log.e("Pedido", "Erro ao pausar thread: ", e);
                            }
                        }
                    } catch (Exception e) {
                        runOnUiThread(() -> {
                            Log.e("Pedido", "Erro ao processar status do pagamento: ", e);
                            Toast.makeText(Pedido.this, "Erro ao processar status do pagamento", Toast.LENGTH_SHORT).show();
                        });
                    }
                } else {
                    runOnUiThread(() -> {
                        String mensagemErro = "Falha ao verificar status: " + response.code();
                        try {
                            JSONObject jsonErro = new JSONObject(responseBody);
                            if (jsonErro.has("error")) {
                                mensagemErro = jsonErro.getString("error");
                            }
                        } catch (Exception e) {
                            Log.e("Pedido", "Erro ao parsear mensagem de erro (getPagamento): ", e);
                        }
                        Log.e("Pedido", "Erro ao verificar status: " + mensagemErro);
                        Toast.makeText(Pedido.this, mensagemErro, Toast.LENGTH_LONG).show();
                    });
                }
            }
        });
    }
}