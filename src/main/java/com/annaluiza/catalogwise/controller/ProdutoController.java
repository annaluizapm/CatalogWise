package com.annaluiza.catalogwise.controller;

import com.annaluiza.catalogwise.model.Produto;
import com.annaluiza.catalogwise.service.ProdutoService;
import jakarta.validation.Valid;
import com.annaluiza.catalogwise.model.StatusProduto;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/produtos")
@CrossOrigin(origins = "*")
public class ProdutoController {

    private final ProdutoService produtoService;

    public ProdutoController(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    @PostMapping
    public Produto criar(@RequestBody @Valid Produto produto) {
        return produtoService.salvar(produto);
    }

    @GetMapping
    public List<Produto> listarTodos() {
        return produtoService.listarTodos();
    }

    @GetMapping("/{id}")
    public Produto buscarPorId(@PathVariable Long id) {
        return produtoService.buscarPorId(id);
    }

    @DeleteMapping("/{id}")
    public void excluir(@PathVariable Long id) {
        produtoService.excluir(id);
    }

    @PutMapping("/{id}")
    public Produto atualizar(@PathVariable Long id, @RequestBody @Valid Produto produto) {
        return produtoService.atualizar(id, produto);
    }

    @GetMapping("/status/{status}")
    public List<Produto> listarPorStatus(@PathVariable StatusProduto status) {
        return produtoService.listarPorStatus(status);
    }
}


