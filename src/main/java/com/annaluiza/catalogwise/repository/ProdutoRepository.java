package com.annaluiza.catalogwise.repository;

import com.annaluiza.catalogwise.model.Produto;
import com.annaluiza.catalogwise.model.StatusProduto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    Optional<Produto> findBySku(String sku);

    List<Produto> findByStatus(StatusProduto status);
}