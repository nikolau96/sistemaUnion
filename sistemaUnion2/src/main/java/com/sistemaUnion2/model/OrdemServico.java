package com.sistemaUnion2.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class OrdemServico {
    private int id;
    private String numeroOS;
    private LocalDateTime dataEmissao;
    private String status;
    private Cliente cliente;
    private LocalDateTime vencimento;
    private BigDecimal valor;
    private String valorPorExtenso;
    public List<ItemOrdemServico> itens;
    private BigDecimal valorLocacao;
    private BigDecimal valorTotalNota;
    private String localEntrega;
    private String observacoes;
    private String vendedor;
    private String contrato;
    private String numeroPedido;
    private String informacoesComplementares;

    public OrdemServico(){
        this.dataEmissao = LocalDateTime.now();
        this.status = "ABERTA";
        this.itens = new ArrayList<>();
        this.valor = BigDecimal.ZERO;
        this.valorLocacao = BigDecimal.ZERO;
        this.valorTotalNota = BigDecimal.ZERO;
    }

    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id = id;
    }

    public String getNumeroOS(){
        return numeroOS;
    }

    public void setNumeroOS(String numeroOS){
        this.numeroOS = numeroOS;
    }

    public LocalDateTime getDataEmissao(){
        return dataEmissao;
    }

    public void setDataEmissao(LocalDateTime dataEmissao){
        this.dataEmissao = dataEmissao;
    }

    public String getStatus(){
        return status;
    }

    public void setStatus(String status){
        this.status = status;
    }

    public Cliente getCliente(){
        return cliente;
    }

    public void setCliente(Cliente cliente){
        this.cliente = cliente;
    }

    public LocalDateTime getVencimento() {
        return vencimento;
    }
    
    public void setVencimento(LocalDateTime vencimento) {
        this.vencimento = vencimento;
    }
    
    public BigDecimal getValor() {
        return valor;
    }
    
    public void setValor(BigDecimal valor) {
        this.valor = valor != null ? valor : BigDecimal.ZERO;
    }
    
    public String getValorPorExtenso() {
        return valorPorExtenso;
    }
    
    public void setValorPorExtenso(String valorPorExtenso) {
        this.valorPorExtenso = valorPorExtenso;
    }
    
    public List<ItemOrdemServico> getItens() {
        return itens;
    }
    
    public void setItens(List<ItemOrdemServico> itens) {
        this.itens = itens != null ? itens : new ArrayList<>();
    }
    
    public BigDecimal getValorLocacao() {
        return valorLocacao;
    }
    
    public void setValorLocacao(BigDecimal valorLocacao) {
        this.valorLocacao = valorLocacao != null ? valorLocacao : BigDecimal.ZERO;
    }
    
    public BigDecimal getValorTotalNota() {
        return valorTotalNota;
    }
    
    public void setValorTotalNota(BigDecimal valorTotalNota) {
        this.valorTotalNota = valorTotalNota != null ? valorTotalNota : BigDecimal.ZERO;
    }
    
    public String getLocalEntrega() {
        return localEntrega;
    }
    
    public void setLocalEntrega(String localEntrega) {
        this.localEntrega = localEntrega;
    }
    
    public String getObservacoes() {
        return observacoes;
    }
    
    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }
    
    public String getVendedor() {
        return vendedor;
    }
    
    public void setVendedor(String vendedor) {
        this.vendedor = vendedor;
    }
    
    public String getContrato() {
        return contrato;
    }
    
    public void setContrato(String contrato) {
        this.contrato = contrato;
    }
    
    public String getNumeroPedido() {
        return numeroPedido;
    }
    
    public void setNumeroPedido(String numeroPedido) {
        this.numeroPedido = numeroPedido;
    }
    
    public String getInformacoesComplementares() {
        return informacoesComplementares;
    }
    
    public void setInformacoesComplementares(String informacoesComplementares) {
        this.informacoesComplementares = informacoesComplementares;
    }

    public void adicionarItem(ItemOrdemServico item) {
        this.itens.add(item);
        recalcularTotais();
    }

    public void removerItem(ItemOrdemServico item) {
        this.itens.remove(item);
        recalcularTotais();
    }

    public void recalcularTotais() {
        BigDecimal total = BigDecimal.ZERO;
        for (ItemOrdemServico item : itens) {
            total = total.add(item.getValorTotal());
        }
        this.valor = total;
        this.valorLocacao = total;
        this.valorTotalNota = total;
    }

    public String getDataEmissaoFormatada() {
        if (dataEmissao != null) {
            return dataEmissao.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        }
        return "";
    }

    public String getVencimentoFormatado() {
        if (vencimento != null) {
            return vencimento.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        }
        return "";
    }

    public boolean isValid() {
        return numeroOS != null && !numeroOS.trim().isEmpty() &&
               cliente != null &&
               !itens.isEmpty();
    }
    
    @Override
    public String toString() {
        return "OS " + numeroOS + " - " + (cliente != null ? cliente.getNome() : "");
    }
}
