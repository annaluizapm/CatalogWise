package com.annaluiza.catalogwise.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O SKU é obrigatório")
    @Column(unique = true)
    private String sku;

    @NotBlank(message = "O nome do produto é obrigatório")
    private String nome;

    @NotBlank(message = "A marca é obrigatória")
    private String marca;

    @NotBlank(message = "A categoria é obrigatória")
    private String categoria;

    @NotNull(message = "O preço é obrigatório")
    @DecimalMin(value = "0.01", message = "O preço deve ser maior que zero")
    private BigDecimal preco;

    @NotNull(message = "O estoque é obrigatório")
    @Min(value = 0, message = "O estoque não pode ser negativo")
    private Integer estoque;

    @NotBlank(message = "A descrição é obrigatória")
    @Column(length = 1000)
    private String descricao;

    private Integer pontuacao;

    @Enumerated(EnumType.STRING)
    private StatusProduto status;
}
