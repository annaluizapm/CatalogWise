package com.annaluiza.catalogwise.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ResumoCatalogoDTO {

    private long totalProdutos;
    private long produtosProntos;
    private long produtosRevisar;
    private long produtosIncompletos;
}