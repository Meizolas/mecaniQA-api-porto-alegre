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

/**
 * Controller REST para a camada de Pedido de Peças.
 * Expõe endpoints que atendem às User Stories US03, US04 e US05.
 *
 * <ul>
 *   <li>US03 — Criar Pedido:        POST  /api/pedidos-pecas</li>
 *   <li>US04 — Adicionar Itens:     POST  /api/pedidos-pecas/{id}/itens</li>
 *   <li>US05 — Modificar Status:    PATCH /api/pedidos-pecas/{id}/status</li>
 * </ul>
 *
 * Segue o padrão Singleton estático do projeto (sem @Autowired).
 */
@RestController
@RequestMapping("/api/pedidos-pecas")
public class PedidoPecaController {

    private final PedidoPecaRepository pedidoRepository;
    private final PecaRepository pecaRepository;

    public PedidoPecaController() {
        this.pedidoRepository = PedidoPecaRepository.getInstance();
        this.pecaRepository = PecaRepository.getInstance();
    }

    // -------------------------------------------------------------------------
    // US03 — Criar Pedido de Peças
    // -------------------------------------------------------------------------

    /**
     * Cria um novo Pedido de Peças.
     * O pedido é iniciado com status {@code ORCANDO} automaticamente.
     * O corpo da requisição pode conter opcionalmente uma lista inicial de itens.
     *
     * <p>Retorna {@code 422 Unprocessable Entity} se alguma peça informada
     * não existir no cadastro.</p>
     *
     * @param dto dados de criação do pedido
     * @return {@code 201 Created} com o pedido salvo como DTO de resposta
     */
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

    // -------------------------------------------------------------------------
    // US04 — Adicionar Peças a um Pedido Existente
    // -------------------------------------------------------------------------

    /**
     * Adiciona novas peças a um pedido já aberto (US04).
     * Cada item da requisição informa o ID da peça existente e a quantidade.
     *
     * <p>Retorna {@code 404 Not Found} se o pedido não existir.</p>
     * <p>Retorna {@code 422 Unprocessable Entity} se alguma peça informada
     * não existir no cadastro.</p>
     *
     * @param id  identificador do pedido
     * @param dto lista de itens a adicionar
     * @return {@code 200 OK} com o pedido atualizado como DTO de resposta
     */
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

    // -------------------------------------------------------------------------
    // US05 — Atualizar Status do Pedido
    // -------------------------------------------------------------------------

    /**
     * Atualiza o status de um Pedido de Peças (US05).
     *
     * <p>Retorna {@code 404 Not Found} se o pedido não existir.</p>
     *
     * @param id  identificador do pedido
     * @param dto novo status a aplicar
     * @return {@code 200 OK} com o pedido atualizado como DTO de resposta
     */
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

    // -------------------------------------------------------------------------
    // Consultas auxiliares
    // -------------------------------------------------------------------------

    /**
     * Lista todos os pedidos de peças cadastrados.
     *
     * @return {@code 200 OK} com a lista de pedidos como DTOs de resposta
     */
    @GetMapping
    public ResponseEntity<List<PedidoPecaResponseDTO>> listar() {
        List<PedidoPecaResponseDTO> resposta = new ArrayList<>();
        for (PedidoPeca pedido : pedidoRepository.listar()) {
            resposta.add(PedidoPecaMapper.paraResponseDTO(pedido));
        }
        return ResponseEntity.ok(resposta);
    }

    /**
     * Busca um pedido pelo seu identificador.
     *
     * @param id identificador do pedido
     * @return {@code 200 OK} com o pedido, ou {@code 404 Not Found}
     */
    @GetMapping("/{id}")
    public ResponseEntity<PedidoPecaResponseDTO> buscarPorId(@PathVariable Long id) {
        return pedidoRepository.buscarPorId(id)
                .map(p -> ResponseEntity.ok(PedidoPecaMapper.paraResponseDTO(p)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // -------------------------------------------------------------------------
    // Método auxiliar privado
    // -------------------------------------------------------------------------

    /**
     * Resolve uma lista de {@link ItemPedidoPecaRequestDTO} em entidades
     * {@link ItemPedidoPeca} buscando cada peça pelo código no repositório.
     *
     * @param dtos lista de DTOs a resolver
     * @return lista de entidades resolvidas, ou {@code null} se alguma peça não for encontrada
     */
    private List<ItemPedidoPeca> resolverItens(List<ItemPedidoPecaRequestDTO> dtos) {
        List<ItemPedidoPeca> itens = new ArrayList<>();

        for (ItemPedidoPecaRequestDTO itemDTO : dtos) {
            Optional<Peca> pecaEncontrada = pecaRepository.buscarPorCodigo(itemDTO.getCodigoPeca());

            if (pecaEncontrada.isEmpty()) {
                return null; // Sinaliza que uma peça não foi encontrada
            }

            ItemPedidoPeca item = new ItemPedidoPeca(pecaEncontrada.get(), itemDTO.getQuantidade());
            itens.add(item);
        }

        return itens;
    }
}
