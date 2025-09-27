package com.sistemaUnion2.model;

import java.math.BigDecimal;

public class ItemOrdemServico{
    private int id;
    private String codigoProduto;
    private String descricao;
    private String unidade;
    private BigDecimal quantidade;
    private BigDecimal valorUnitario;
    private BigDecimal valorTotal;
    
    public ItemOrdemServico() {
        this.quantidade = BigDecimal.ONE;
        this.valorUnitario = BigDecimal.ZERO;
        this.valorTotal = BigDecimal.ZERO;
    }
    
    public ItemOrdemServico(String codigoProduto, String descricao, String unidade, 
                           BigDecimal quantidade, BigDecimal valorUnitario) {
        this.codigoProduto = codigoProduto;
        this.descricao = descricao;
        this.unidade = unidade;
        this.quantidade = quantidade != null ? quantidade : BigDecimal.ONE;
        this.valorUnitario = valorUnitario != null ? valorUnitario : BigDecimal.ZERO;
        calcularValorTotal();
    }

    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getCodigoProduto() {
        return codigoProduto;
    }
    
    public void setCodigoProduto(String codigoProduto) {
        this.codigoProduto = codigoProduto;
    }
    
    public String getDescricao() {
        return descricao;
    }
    
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
    
    public String getUnidade() {
        return unidade;
    }
    
    public void setUnidade(String unidade) {
        this.unidade = unidade;
    }
    
    public BigDecimal getQuantidade() {
        return quantidade;
    }
    
    public void setQuantidade(BigDecimal quantidade) {
        this.quantidade = quantidade != null ? quantidade : BigDecimal.ONE;
        calcularValorTotal();
    }
    
    public BigDecimal getValorUnitario() {
        return valorUnitario;
    }
    
    public void setValorUnitario(BigDecimal valorUnitario) {
        this.valorUnitario = valorUnitario != null ? valorUnitario : BigDecimal.ZERO;
        calcularValorTotal();
    }
    
    public BigDecimal getValorTotal() {
        return valorTotal;
    }
    
    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal != null ? valorTotal : BigDecimal.ZERO;
    }

    public void calcularValorTotal() {
        if (quantidade != null && valorUnitario != null) {
            this.valorTotal = quantidade.multiply(valorUnitario);
        }
    }

    public static ItemOrdemServico fromProduto(Produto produto, BigDecimal quantidade) {
        ItemOrdemServico item = new ItemOrdemServico();
        item.setCodigoProduto(String.valueOf(produto.getId()));
        item.setDescricao(produto.getNome());
        item.setUnidade("UN");
        item.setQuantidade(quantidade);
        item.setValorUnitario(produto.getPreco());
        return item;
    }
}
