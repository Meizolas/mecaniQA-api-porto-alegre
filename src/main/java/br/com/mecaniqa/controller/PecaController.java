package br.com.mecaniqa.controller;

import br.com.mecaniqa.model.Peca;
import br.com.mecaniqa.model.Repository.PecaRepository;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/pecas")
public class PecaController {

    private final PecaRepository repository = PecaRepository.getInstance();

    @PostMapping
    public ResponseEntity<Peca> criar(@RequestBody Peca peca) {
        Peca criada = repository.adicionarPeca(peca);
        return ResponseEntity.status(HttpStatus.CREATED).body(criada);
    }

    @GetMapping
    public ResponseEntity<List<Peca>> listarTodas() {
        List<Peca> pecas = repository.listarPecas();
        return ResponseEntity.ok(pecas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Peca> buscarPorId(@PathVariable Long id) {
        Optional<Peca> peca = repository.buscarPorId(id);

        return peca.map(ResponseEntity::ok)
                   .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Peca> atualizar(@PathVariable Long id,
                                          @RequestBody Peca peca) {
        Optional<Peca> atualizada = repository.atualizarPeca(id, peca);

        return atualizada.map(ResponseEntity::ok)
                         .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        boolean removida = repository.removerPeca(id);

        if (removida) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();
    }
}
