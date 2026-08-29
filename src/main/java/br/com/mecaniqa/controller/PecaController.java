package br.com.mecaniqa.controller;

import br.com.mecaniqa.model.Peca;
import br.com.mecaniqa.repository.PecaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;

import java.util.List;

@RestController
@RequestMapping("/api/pecas")
public class PecaController {

    private final PecaRepository repository;

    public PecaController() {
        this.repository = PecaRepository.getInstance();
    }

    @PostMapping
    public ResponseEntity<Peca> cadastrar(@RequestBody Peca peca) {
        Peca pecaSalva = repository.salvar(peca);
        return ResponseEntity.status(HttpStatus.CREATED).body(pecaSalva);
    }
    @GetMapping
    public ResponseEntity<List<Peca>> listar() {
        return ResponseEntity.ok(repository.listar());
    }
    @GetMapping("/{codigo}")
    public ResponseEntity<Peca> buscarPorCodigo(@PathVariable Long codigo) {
        return repository.buscarPorCodigo(codigo)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    @PutMapping("/{codigo}")
    public ResponseEntity<Peca> atualizar(
            @PathVariable Long codigo,
            @RequestBody Peca novosDados) {

        return repository.atualizar(codigo, novosDados)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{codigo}")
    public ResponseEntity<Void> excluir(@PathVariable Long codigo) {
        boolean excluiu = repository.excluir(codigo);

        if (excluiu) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();
    }
}