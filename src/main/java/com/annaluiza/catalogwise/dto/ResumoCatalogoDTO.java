package com.annaluiza.catalogwise.dto;

public class ResumoCatalogoDTO {

    private long totalProdutos;
    private long produtosProntos;
    private long produtosRevisar;
    private long produtosIncompletos;

    public ResumoCatalogoDTO() {
    }

    public ResumoCatalogoDTO(long totalProdutos, long produtosProntos, long produtosRevisar, long produtosIncompletos) {
        this.totalProdutos = totalProdutos;
        this.produtosProntos = produtosProntos;
        this.produtosRevisar = produtosRevisar;
        this.produtosIncompletos = produtosIncompletos;
    }

    public long getTotalProdutos() {
        return totalProdutos;
    }

    public void setTotalProdutos(long totalProdutos) {
        this.totalProdutos = totalProdutos;
    }

    public long getProdutosProntos() {
        return produtosProntos;
    }

    public void setProdutosProntos(long produtosProntos) {
        this.produtosProntos = produtosProntos;
    }

    public long getProdutosRevisar() {
        return produtosRevisar;
    }

    public void setProdutosRevisar(long produtosRevisar) {
        this.produtosRevisar = produtosRevisar;
    }

    public long getProdutosIncompletos() {
        return produtosIncompletos;
    }

    public void setProdutosIncompletos(long produtosIncompletos) {
        this.produtosIncompletos = produtosIncompletos;
    }
}