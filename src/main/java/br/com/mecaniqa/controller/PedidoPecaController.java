package br.com.mecaniqa.controller;

import br.com.mecaniqa.dto.pedidopeca.AdicionarItensPedidoRequestDTO;
import br.com.mecaniqa.dto.pedidopeca.AtualizarStatusPedidoRequestDTO;
import br.com.mecaniqa.dto.pedidopeca.PedidoPecaRequestDTO;
import br.com.mecaniqa.dto.pedidopeca.PedidoPecaResponseDTO;
import br.com.mecaniqa.exception.DadosInvalidosException;
import br.com.mecaniqa.exception.PecaNaoEncontradaException;
import br.com.mecaniqa.mapper.PedidoPecaMapper;
import br.com.mecaniqa.model.PedidoPeca;
import br.com.mecaniqa.service.PedidoPecaService;
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
@RequestMapping("/api/pedidos")
public class PedidoPecaController {
    private final PedidoPecaService service;

    public PedidoPecaController() {
        this(new PedidoPecaService());
    }

    public PedidoPecaController(PedidoPecaService service) {
        this.service = service;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        return service.excluir(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<?> criarPedido(@RequestBody PedidoPecaRequestDTO dto) {
        try {
            PedidoPeca pedido = PedidoPecaMapper.paraModel(dto);
            PedidoPeca salvo = service.criar(pedido, dto.getItens());
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(PedidoPecaMapper.paraResponseDTO(salvo));
        } catch (DadosInvalidosException e) {
            return quantidadeInvalida();
        } catch (PecaNaoEncontradaException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{id}/itens")
    public ResponseEntity<?> adicionarItens(
            @PathVariable Long id, @RequestBody AdicionarItensPedidoRequestDTO dto) {
        try {
            return service.adicionarItens(id, dto.getItens())
                    .map(pedido -> ResponseEntity.ok(PedidoPecaMapper.paraResponseDTO(pedido)))
                    .orElseGet(() -> ResponseEntity.notFound().build());
        } catch (DadosInvalidosException e) {
            return quantidadeInvalida();
        } catch (PecaNaoEncontradaException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<?> atualizarStatus(
            @PathVariable Long id, @RequestBody AtualizarStatusPedidoRequestDTO dto) {
        try {
            return service.atualizarStatus(id, dto.getStatus())
                    .map(pedido -> ResponseEntity.ok(PedidoPecaMapper.paraResponseDTO(pedido)))
                    .orElseGet(() -> ResponseEntity.notFound().build());
        } catch (DadosInvalidosException e) {
            return ResponseEntity.badRequest().body("O status é obrigatório.");
        }
    }

    @GetMapping
    public ResponseEntity<List<PedidoPecaResponseDTO>> listar() {
        return ResponseEntity.ok(service.listar().stream()
                .map(PedidoPecaMapper::paraResponseDTO).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PedidoPecaResponseDTO> buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id)
                .map(pedido -> ResponseEntity.ok(PedidoPecaMapper.paraResponseDTO(pedido)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    private ResponseEntity<String> quantidadeInvalida() {
        return ResponseEntity.badRequest()
                .body("Informe peças com código e quantidade maior que zero.");
    }
}
