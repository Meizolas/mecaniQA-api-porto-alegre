package br.com.mecaniqa.controller;

import br.com.mecaniqa.dto.servico.ServicoRequestDTO;
import br.com.mecaniqa.dto.servico.ServicoResponseDTO;
import br.com.mecaniqa.mapper.ServicoMapper;
import br.com.mecaniqa.service.ServicoService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/servicos")
public class ServicoController {
    private final ServicoService service;

    public ServicoController() {
        service = new ServicoService();
    }

    @PostMapping
    public ResponseEntity<ServicoResponseDTO> cadastrar(@RequestBody ServicoRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ServicoMapper.paraResponseDTO(
                        service.cadastrar(ServicoMapper.paraModel(dto))));
    }

    @GetMapping
    public ResponseEntity<List<ServicoResponseDTO>> listar() {
        return ResponseEntity.ok(service.listar().stream()
                .map(ServicoMapper::paraResponseDTO).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServicoResponseDTO> buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id)
                .map(servico -> ResponseEntity.ok(ServicoMapper.paraResponseDTO(servico)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ServicoResponseDTO> atualizar(
            @PathVariable Long id, @RequestBody ServicoRequestDTO dto) {
        return service.atualizar(id, ServicoMapper.paraModel(dto))
                .map(servico -> ResponseEntity.ok(ServicoMapper.paraResponseDTO(servico)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        return service.excluir(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}
