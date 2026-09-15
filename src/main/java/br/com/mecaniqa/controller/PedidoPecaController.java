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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/pedidos-pecas")
public class PedidoPecaController {

    private final PedidoPecaRepository pedidoRepository;
    private final PecaRepository pecaRepository;

    public PedidoPecaController() {
        this.pedidoRepository = PedidoPecaRepository.getInstance();
        this.pecaRepository = PecaRepository.getInstance();
    }

    @PostMapping
    public ResponseEntity<?> criarPedido(@RequestBody PedidoPecaRequestDTO dto) {
        PedidoPeca pedido = new PedidoPeca();

        if (dto.getItens() != null && !dto.getItens().isEmpty()) {
            List<ItemPedidoPeca> itens = resolverItens(dto.getItens());
            if (itens == null) {
                return ResponseEntity
                        .unprocessableEntity()
                        .body("Uma ou mais peças informadas não foram encontradas.");
            }
            for (ItemPedidoPeca item : itens) {
                pedido.adicionarItem(item);
            }
        }

        PedidoPeca pedidoSalvo = pedidoRepository.salvar(pedido);
        PedidoPecaResponseDTO resposta = PedidoPecaMapper.paraResponseDTO(pedidoSalvo);

        return ResponseEntity.status(HttpStatus.CREATED).body(resposta);
    }

    @PostMapping("/{id}/itens")
    public ResponseEntity<?> adicionarItens(
            @PathVariable Long id,
            @RequestBody AdicionarItensPedidoRequestDTO dto) {

        List<ItemPedidoPeca> itens = resolverItens(dto.getItens());
        if (itens == null) {
            return ResponseEntity
                    .unprocessableEntity()
                    .body("Uma ou mais peças informadas não foram encontradas.");
        }

        Optional<PedidoPeca> pedidoAtualizado = pedidoRepository.adicionarItens(id, itens);

        return pedidoAtualizado
                .map(p -> ResponseEntity.ok(PedidoPecaMapper.paraResponseDTO(p)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<?> atualizarStatus(
            @PathVariable Long id,
            @RequestBody AtualizarStatusPedidoRequestDTO dto) {

        Optional<PedidoPeca> pedidoAtualizado =
                pedidoRepository.atualizarStatus(id, dto.getNovoStatus());

        return pedidoAtualizado
                .map(p -> ResponseEntity.ok(PedidoPecaMapper.paraResponseDTO(p)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<PedidoPecaResponseDTO>> listar() {
        List<PedidoPecaResponseDTO> resposta = new ArrayList<>();
        for (PedidoPeca pedido : pedidoRepository.listar()) {
            resposta.add(PedidoPecaMapper.paraResponseDTO(pedido));
        }
        return ResponseEntity.ok(resposta);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PedidoPecaResponseDTO> buscarPorId(@PathVariable Long id) {
        return pedidoRepository.buscarPorId(id)
                .map(p -> ResponseEntity.ok(PedidoPecaMapper.paraResponseDTO(p)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    private List<ItemPedidoPeca> resolverItens(List<ItemPedidoPecaRequestDTO> dtos) {
        List<ItemPedidoPeca> itens = new ArrayList<>();

        for (ItemPedidoPecaRequestDTO itemDTO : dtos) {
            Optional<Peca> pecaEncontrada = pecaRepository.buscarPorCodigo(itemDTO.getCodigoPeca());

            if (pecaEncontrada.isEmpty()) {
                return null;
            }

            ItemPedidoPeca item = new ItemPedidoPeca(pecaEncontrada.get(), itemDTO.getQuantidade());
            itens.add(item);
        }

        return itens;
    }
}
