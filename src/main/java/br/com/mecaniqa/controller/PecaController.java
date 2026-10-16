package br.com.mecaniqa.controller;

import br.com.mecaniqa.dto.peca.PecaRequestDTO;
import br.com.mecaniqa.dto.peca.PecaResponseDTO;
import br.com.mecaniqa.exception.DadosInvalidosException;
import br.com.mecaniqa.exception.PecaNaoEncontradaException;
import br.com.mecaniqa.mapper.PecaMapper;
import br.com.mecaniqa.model.Peca;
import br.com.mecaniqa.service.PecaService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pecas")
public class PecaController {

    private final PecaService service;

    public PecaController() {
        service = new PecaService();
    }

    @PostMapping
    public ResponseEntity<PecaResponseDTO> cadastrar(
            @RequestBody PecaRequestDTO dto) {

        Peca peca = PecaMapper.paraModel(dto);
        Peca pecaSalva = service.cadastrar(peca);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(PecaMapper.paraResponseDTO(pecaSalva));
    }

    @GetMapping
    public ResponseEntity<List<PecaResponseDTO>> listar() {
        List<PecaResponseDTO> resposta = service.listar()
                .stream()
                .map(PecaMapper::paraResponseDTO)
                .toList();

        return ResponseEntity.ok(resposta);
    }

    @GetMapping("/{codigo}")
    public ResponseEntity<PecaResponseDTO> buscarPorCodigo(
            @PathVariable Long codigo) {

        try {
            Peca peca = service.buscarPorCodigo(codigo);

            return ResponseEntity.ok(
                    PecaMapper.paraResponseDTO(peca)
            );

        } catch (DadosInvalidosException e) {
            return ResponseEntity.badRequest().build();

        } catch (PecaNaoEncontradaException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{codigo}")
    public ResponseEntity<PecaResponseDTO> atualizar(
            @PathVariable Long codigo,
            @RequestBody PecaRequestDTO dto) {

        try {
            Peca novosDados = PecaMapper.paraModel(dto);
            Peca peca = service.atualizar(codigo, novosDados);

            return ResponseEntity.ok(
                    PecaMapper.paraResponseDTO(peca)
            );

        } catch (DadosInvalidosException e) {
            return ResponseEntity.badRequest().build();

        } catch (PecaNaoEncontradaException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{codigo}")
    public ResponseEntity<Void> excluir(
            @PathVariable Long codigo) {

        try {
            service.excluir(codigo);

            return ResponseEntity.noContent().build();

        } catch (DadosInvalidosException e) {
            return ResponseEntity.badRequest().build();

        } catch (PecaNaoEncontradaException e) {
            return ResponseEntity.notFound().build();
        }
    }
}