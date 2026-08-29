package br.com.mecaniqa.controller;

import br.com.mecaniqa.repository.PecaRepository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pecas")
public class PecaController {

    private final PecaRepository repository;

    public PecaController() {
        this.repository = PecaRepository.getInstance();
    }
}