package br.com.mecaniqa.controller;

import java.util.List;
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
import br.com.mecaniqa.dto.peca.PecaRequestDTO;
import br.com.mecaniqa.dto.peca.PecaResponseDTO;
import br.com.mecaniqa.mapper.PecaMapper;

@RestController
@RequestMapping("/api/pecas")
public class PecaController {

    private final PecaRepository repository;

    public PecaController() {
        this.repository = PecaRepository.getInstance();
    }

    @PostMapping
    public ResponseEntity<PecaResponseDTO> cadastrar(
            @RequestBody PecaRequestDTO dto) {

        Peca peca = PecaMapper.paraModel(dto);

        Peca pecaSalva = repository.salvar(peca);

        PecaResponseDTO resposta =
                PecaMapper.paraResponseDTO(pecaSalva);

        return ResponseEntity.status(HttpStatus.CREATED).body(resposta);
    }

    @GetMapping
    public ResponseEntity<List<PecaResponseDTO>> listar() {
        return ResponseEntity.ok(repository.listar().stream().map(PecaMapper::paraResponseDTO).toList());
    }

    @GetMapping("/{codigo}")
    public ResponseEntity<PecaResponseDTO> buscarPorCodigo(@PathVariable Long codigo) {
        return repository.buscarPorCodigo(codigo)
                .map(peca -> ResponseEntity.ok(PecaMapper.paraResponseDTO(peca)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{codigo}")
    public ResponseEntity<PecaResponseDTO> atualizar(
            @PathVariable Long codigo,
            @RequestBody PecaRequestDTO dto) {

        return repository.atualizar(codigo, PecaMapper.paraModel(dto))
                .map(peca -> ResponseEntity.ok(PecaMapper.paraResponseDTO(peca)))
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
