package com.annaluiza.catalogwise.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Map;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(SkuDuplicadoException.class)
    public ResponseEntity<Map<String, Object>> tratarSkuDuplicado(SkuDuplicadoException exception) {
        Map<String, Object> resposta = Map.of(
                "timestamp", LocalDateTime.now(),
                "status", 400,
                "erro", "SKU duplicado",
                "mensagem", exception.getMessage()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resposta);
    }

    @ExceptionHandler(ProdutoNaoEncontradoException.class)
    public ResponseEntity<Map<String, Object>> tratarProdutoNaoEncontrado(ProdutoNaoEncontradoException exception) {
        Map<String, Object> resposta = Map.of(
                "timestamp", LocalDateTime.now(),
                "status", 404,
                "erro", "Produto não encontrado",
                "mensagem", exception.getMessage()
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(resposta);
    }
}