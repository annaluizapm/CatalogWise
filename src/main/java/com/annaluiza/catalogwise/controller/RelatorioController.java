package com.annaluiza.catalogwise.controller;

import com.annaluiza.catalogwise.dto.ResumoCatalogoDTO;
import com.annaluiza.catalogwise.service.RelatorioService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*")
public class RelatorioController {

    private final RelatorioService relatorioService;

    public RelatorioController(RelatorioService relatorioService) {
        this.relatorioService = relatorioService;
    }

    @GetMapping("/relatorios/resumo")
    public ResumoCatalogoDTO resumo() {
        return relatorioService.gerarResumo();
    }
}