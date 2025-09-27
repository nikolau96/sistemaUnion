package com.sistemaUnion2.model;

import java.math.BigDecimal;

public class Produto {
    private int id;
    private String nome;
    private String descricao;
    private BigDecimal preco;
    //private String categoria;
    private boolean ativo;
    
    public Produto() {
        this.ativo = true;
        this.preco = BigDecimal.ZERO;
    }
    
    public Produto(String nome, String descricao, BigDecimal preco, String categoria) {
        this.nome = nome;
        this.descricao = descricao;
        this.preco = preco != null ? preco : BigDecimal.ZERO;
        //this.categoria = categoria;
        this.ativo = true;
    }
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getNome() {
        return nome;
    }
    
    public void setNome(String nome) {
        this.nome = nome;
    }
    
    public String getDescricao() {
        return descricao;
    }
    
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
    
    public BigDecimal getPreco() {
        return preco;
    }
    
    public void setPreco(BigDecimal preco) {
        this.preco = preco != null ? preco : BigDecimal.ZERO;
    }
    
    /*public String getCategoria() {
        return categoria;
    }
    
    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }*/
    
    public boolean isAtivo() {
        return ativo;
    }
    
    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }
    
    @Override
    public String toString() {
        return nome + " - R$ " + preco;
    }
    
    public boolean isValid() {
        return nome != null && !nome.trim().isEmpty() && preco != null && preco.compareTo(BigDecimal.ZERO) >= 0;
    }
    
    public String getPrecoFormatado() {
        return String.format("R$ %.2f", preco);
    }
}
