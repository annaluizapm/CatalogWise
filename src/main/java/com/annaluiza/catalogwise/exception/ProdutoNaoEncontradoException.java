package com.annaluiza.catalogwise.exception;

public class ProdutoNaoEncontradoException extends RuntimeException {

    public ProdutoNaoEncontradoException(String message) {
        super(message);
    }
}