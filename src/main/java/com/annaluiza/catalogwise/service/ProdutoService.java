package com.annaluiza.catalogwise.service;

import com.annaluiza.catalogwise.model.Produto;
import com.annaluiza.catalogwise.model.StatusProduto;
import com.annaluiza.catalogwise.repository.ProdutoRepository;
import com.annaluiza.catalogwise.exception.SkuDuplicadoException;
import com.annaluiza.catalogwise.exception.ProdutoNaoEncontradoException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProdutoService {

    private final ProdutoRepository produtoRepository;

    public ProdutoService(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    public Produto salvar(Produto produto) {
        boolean skuJaExiste = produtoRepository.findBySku(produto.getSku()).isPresent();

        if (skuJaExiste) {
            throw new SkuDuplicadoException("Já existe um produto cadastrado com este SKU");
        }

        produto.setPontuacao(calcularPontuacao(produto));
        produto.setStatus(definirStatus(produto.getPontuacao()));

        return produtoRepository.save(produto);
    }

    public Produto atualizar(Long id, Produto produtoAtualizado) {
        Produto produtoExistente = buscarPorId(id);

        produtoRepository.findBySku(produtoAtualizado.getSku())
                .ifPresent(produtoComMesmoSku -> {
                    if (!produtoComMesmoSku.getId().equals(id)) {
                        throw new SkuDuplicadoException("Já existe outro produto cadastrado com este SKU");
                    }
                });

        produtoExistente.setSku(produtoAtualizado.getSku());
        produtoExistente.setNome(produtoAtualizado.getNome());
        produtoExistente.setMarca(produtoAtualizado.getMarca());
        produtoExistente.setCategoria(produtoAtualizado.getCategoria());
        produtoExistente.setPreco(produtoAtualizado.getPreco());
        produtoExistente.setEstoque(produtoAtualizado.getEstoque());
        produtoExistente.setDescricao(produtoAtualizado.getDescricao());

        produtoExistente.setPontuacao(calcularPontuacao(produtoExistente));
        produtoExistente.setStatus(definirStatus(produtoExistente.getPontuacao()));

        return produtoRepository.save(produtoExistente);
    }

    public List<Produto> listarTodos() {
        return produtoRepository.findAll();
    }

    public Produto buscarPorId(Long id) {
        return produtoRepository.findById(id)
                .orElseThrow(() -> new ProdutoNaoEncontradoException("Produto não encontrado"));
    }

    public void excluir(Long id) {
        produtoRepository.deleteById(id);
    }

    public int calcularPontuacao(Produto produto) {
        int pontuacao = 0;

        if (produto.getNome() != null && !produto.getNome().isBlank()) {
            pontuacao += 10;
        }

        if (produto.getNome() != null && produto.getNome().length() >= 25) {
            pontuacao += 10;
        }

        if (produto.getMarca() != null && !produto.getMarca().isBlank()) {
            pontuacao += 10;
        }

        if (produto.getCategoria() != null && !produto.getCategoria().isBlank()) {
            pontuacao += 10;
        }

        if (produto.getDescricao() != null && !produto.getDescricao().isBlank()) {
            pontuacao += 10;
        }

        if (produto.getDescricao() != null && produto.getDescricao().length() >= 80) {
            pontuacao += 15;
        }

        if (produto.getPreco() != null && produto.getPreco().doubleValue() > 0) {
            pontuacao += 10;
        }

        if (produto.getEstoque() != null && produto.getEstoque() >= 0) {
            pontuacao += 5;
        }

        if (produto.getSku() != null && !produto.getSku().isBlank()) {
            pontuacao += 15;
        }

        return pontuacao;
    }

    public StatusProduto definirStatus(int pontuacao) {
        if (pontuacao >= 80) {
            return StatusProduto.PRONTO;
        }

        if (pontuacao >= 50) {
            return StatusProduto.REVISAR;
        }

        return StatusProduto.INCOMPLETO;
    }

    public List<Produto> listarPorStatus(StatusProduto status) {
        return produtoRepository.findByStatus(status);
    }
}