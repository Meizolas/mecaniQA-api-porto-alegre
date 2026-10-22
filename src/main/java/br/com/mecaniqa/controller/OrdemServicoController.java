package br.com.mecaniqa.controller;

import br.com.mecaniqa.dto.ordemservico.AtualizarStatusOrdemDTO;
import br.com.mecaniqa.dto.ordemservico.OrdemServicoRequestDTO;
import br.com.mecaniqa.dto.ordemservico.OrdemServicoResponseDTO;
import br.com.mecaniqa.exception.DadosInvalidosException;
import br.com.mecaniqa.mapper.OrdemServicoMapper;
import br.com.mecaniqa.model.OrdemServico;
import br.com.mecaniqa.service.OrdemServicoService;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ordens-servico")
public class OrdemServicoController {

    private final OrdemServicoService service;

    public OrdemServicoController() {
        this(new OrdemServicoService());
    }

    public OrdemServicoController(OrdemServicoService service) {
        this.service = service;
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrdemServicoResponseDTO> atualizar(
            @PathVariable Long id, @RequestBody OrdemServicoRequestDTO dto) {
        try {
            return service.atualizar(id, dto.getDescricaoProblema(), dto.getValor())
                    .map(ordem -> ResponseEntity.ok(OrdemServicoMapper.paraResponseDTO(ordem)))
                    .orElseGet(() -> ResponseEntity.notFound().build());
        } catch (DadosInvalidosException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        return service.excluir(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<OrdemServicoResponseDTO> criar(
            @RequestBody OrdemServicoRequestDTO dto) {

        OrdemServico salva = service.criar(
                OrdemServicoMapper.paraModel(dto)
        );

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(OrdemServicoMapper.paraResponseDTO(salva));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<OrdemServicoResponseDTO> atualizarStatus(
            @PathVariable Long id,
            @RequestBody AtualizarStatusOrdemDTO dto) {

        try {
            return service.atualizarStatus(id, dto.getStatus())
                    .map(ordem -> ResponseEntity.ok(
                            OrdemServicoMapper.paraResponseDTO(ordem)
                    ))
                    .orElseGet(
                            () -> ResponseEntity.notFound().build()
                    );

        } catch (DadosInvalidosException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<OrdemServicoResponseDTO>> listar() {
        return ResponseEntity.ok(
                service.listar().stream()
                        .map(OrdemServicoMapper::paraResponseDTO)
                        .toList()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrdemServicoResponseDTO> buscarPorId(
            @PathVariable Long id) {

        return service.buscarPorId(id)
                .map(ordem -> ResponseEntity.ok(
                        OrdemServicoMapper.paraResponseDTO(ordem)
                ))
                .orElseGet(
                        () -> ResponseEntity.notFound().build()
                );
    }
}
