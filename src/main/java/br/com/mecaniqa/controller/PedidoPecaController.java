package br.com.mecaniqa.controller;

import br.com.mecaniqa.dto.pedidopeca.AdicionarItensPedidoRequestDTO;
import br.com.mecaniqa.dto.pedidopeca.AtualizarStatusPedidoRequestDTO;
import br.com.mecaniqa.dto.pedidopeca.ItemPedidoPecaRequestDTO;
import br.com.mecaniqa.dto.pedidopeca.PedidoPecaRequestDTO;
import br.com.mecaniqa.dto.pedidopeca.PedidoPecaResponseDTO;
import br.com.mecaniqa.mapper.PedidoPecaMapper;
import br.com.mecaniqa.model.ItemPedidoPeca;
import br.com.mecaniqa.model.Peca;
import br.com.mecaniqa.model.PedidoPeca;
import br.com.mecaniqa.repository.PecaRepository;
import br.com.mecaniqa.repository.PedidoPecaRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
@RequestMapping("/api/pedidos")
public class PedidoPecaController {
    private final PedidoPecaRepository pedidoRepository;
    private final PecaRepository pecaRepository;

    public PedidoPecaController() {
        pedidoRepository = PedidoPecaRepository.getInstance();
        pecaRepository = PecaRepository.getInstance();
    }

    @PostMapping
    public ResponseEntity<?> criarPedido(@RequestBody PedidoPecaRequestDTO dto) {
        PedidoPeca pedido = PedidoPecaMapper.paraModel(dto);
        if (dto.getItens() != null && !dto.getItens().isEmpty()) {
            if (possuiQuantidadeInvalida(dto.getItens())) return quantidadeInvalida();
            List<ItemPedidoPeca> itens = resolverItens(dto.getItens());
            if (itens == null) return ResponseEntity.notFound().build();
            itens.forEach(pedido::adicionarItem);
        }
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(PedidoPecaMapper.paraResponseDTO(pedidoRepository.salvar(pedido)));
    }

    @PostMapping("/{id}/itens")
    public ResponseEntity<?> adicionarItens(
            @PathVariable Long id, @RequestBody AdicionarItensPedidoRequestDTO dto) {
        if (pedidoRepository.buscarPorId(id).isEmpty()) return ResponseEntity.notFound().build();
        if (dto.getItens() == null || dto.getItens().isEmpty() || possuiQuantidadeInvalida(dto.getItens())) {
            return quantidadeInvalida();
        }
        List<ItemPedidoPeca> itens = resolverItens(dto.getItens());
        if (itens == null) return ResponseEntity.notFound().build();
        return pedidoRepository.adicionarItens(id, itens)
                .map(pedido -> ResponseEntity.ok(PedidoPecaMapper.paraResponseDTO(pedido)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<?> atualizarStatus(
            @PathVariable Long id, @RequestBody AtualizarStatusPedidoRequestDTO dto) {
        if (dto.getStatus() == null) return ResponseEntity.badRequest().body("O status é obrigatório.");
        return pedidoRepository.atualizarStatus(id, dto.getStatus())
                .map(pedido -> ResponseEntity.ok(PedidoPecaMapper.paraResponseDTO(pedido)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<PedidoPecaResponseDTO>> listar() {
        return ResponseEntity.ok(pedidoRepository.listar().stream()
                .map(PedidoPecaMapper::paraResponseDTO).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PedidoPecaResponseDTO> buscarPorId(@PathVariable Long id) {
        return pedidoRepository.buscarPorId(id)
                .map(pedido -> ResponseEntity.ok(PedidoPecaMapper.paraResponseDTO(pedido)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    private List<ItemPedidoPeca> resolverItens(List<ItemPedidoPecaRequestDTO> dtos) {
        List<ItemPedidoPeca> itens = new ArrayList<>();
        for (ItemPedidoPecaRequestDTO dto : dtos) {
            Optional<Peca> peca = pecaRepository.buscarPorCodigo(dto.getCodigoPeca());
            if (peca.isEmpty()) return null;
            itens.add(new ItemPedidoPeca(peca.get(), dto.getQuantidade()));
        }
        return itens;
    }

    private boolean possuiQuantidadeInvalida(List<ItemPedidoPecaRequestDTO> itens) {
        return itens.stream().anyMatch(item -> item.getCodigoPeca() == null
                || item.getQuantidade() == null || item.getQuantidade() <= 0);
    }

    private ResponseEntity<String> quantidadeInvalida() {
        return ResponseEntity.badRequest().body("Informe peças com código e quantidade maior que zero.");
    }
}
