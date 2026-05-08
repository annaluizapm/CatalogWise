package com.annaluiza.catalogwise.service;

import com.annaluiza.catalogwise.dto.ResumoCatalogoDTO;
import com.annaluiza.catalogwise.model.StatusProduto;
import com.annaluiza.catalogwise.repository.ProdutoRepository;
import org.springframework.stereotype.Service;

@Service
public class RelatorioService {

    private final ProdutoRepository produtoRepository;

    public RelatorioService(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    public ResumoCatalogoDTO gerarResumo() {
        long totalProdutos = produtoRepository.count();
        long produtosProntos = produtoRepository.findByStatus(StatusProduto.PRONTO).size();
        long produtosRevisar = produtoRepository.findByStatus(StatusProduto.REVISAR).size();
        long produtosIncompletos = produtoRepository.findByStatus(StatusProduto.INCOMPLETO).size();

        return new ResumoCatalogoDTO(
                totalProdutos,
                produtosProntos,
                produtosRevisar,
                produtosIncompletos
        );
    }
}