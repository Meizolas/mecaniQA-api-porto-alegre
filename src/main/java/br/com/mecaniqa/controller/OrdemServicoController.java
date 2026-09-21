package br.com.mecaniqa.controller;

import br.com.mecaniqa.dto.ordemservico.AtualizarStatusOrdemDTO;
import br.com.mecaniqa.dto.ordemservico.OrdemServicoRequestDTO;
import br.com.mecaniqa.dto.ordemservico.OrdemServicoResponseDTO;
import br.com.mecaniqa.mapper.OrdemServicoMapper;
import br.com.mecaniqa.model.OrdemServico;
import br.com.mecaniqa.repository.OrdemServicoRepository;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ordens-servico")
public class OrdemServicoController {
    private final OrdemServicoRepository repository;

    public OrdemServicoController() { repository = OrdemServicoRepository.getInstance(); }

    @PostMapping
    public ResponseEntity<OrdemServicoResponseDTO> criar(@RequestBody OrdemServicoRequestDTO dto) {
        OrdemServico salva = repository.salvar(OrdemServicoMapper.paraModel(dto));
        return ResponseEntity.status(HttpStatus.CREATED).body(OrdemServicoMapper.paraResponseDTO(salva));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<OrdemServicoResponseDTO> atualizarStatus(
            @PathVariable Long id, @RequestBody AtualizarStatusOrdemDTO dto) {
        if (dto.getStatus() == null) return ResponseEntity.badRequest().build();
        return repository.atualizarStatus(id, dto.getStatus())
                .map(ordem -> ResponseEntity.ok(OrdemServicoMapper.paraResponseDTO(ordem)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<OrdemServicoResponseDTO>> listar() {
        return ResponseEntity.ok(repository.listar().stream()
                .map(OrdemServicoMapper::paraResponseDTO).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrdemServicoResponseDTO> buscarPorId(@PathVariable Long id) {
        return repository.buscarPorId(id)
                .map(ordem -> ResponseEntity.ok(OrdemServicoMapper.paraResponseDTO(ordem)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
