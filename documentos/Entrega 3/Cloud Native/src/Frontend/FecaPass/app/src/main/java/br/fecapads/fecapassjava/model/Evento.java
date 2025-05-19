package br.fecapads.fecapassjava.model;

import com.google.gson.annotations.SerializedName;

public class Evento {
    private int id;
    private String titulo;
    @SerializedName("preco")
    private double preco;
    private String data;
    private String localizacao;
    private String descricao;
    private String imagemUrl;

    // Construtor
    public Evento(int id, String titulo, double preco, String data, String localizacao, String descricao, String imagemUrl) {
        this.id = id;
        this.titulo = titulo;
        this.preco = preco;
        this.data = data;
        this.localizacao = localizacao;
        this.descricao = descricao;
        this.imagemUrl = imagemUrl;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public double getPrecoAsDouble() {
        return preco;
    }

    public String getData() {
        return data;
    }

    public String getLocalizacao() {
        return localizacao;
    }

    public String getDescricao() {
        return descricao;
    }

    public String getImagemUrl() {
        return imagemUrl;
    }
}