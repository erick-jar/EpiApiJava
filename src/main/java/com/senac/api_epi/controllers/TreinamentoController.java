package com.senac.api_epi.controllers;

import com.senac.api_epi.entities.Treinamento;
import com.senac.api_epi.entities.TreinamentoRealizado;
import com.senac.api_epi.service.TreinamentoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/treinamentos")
public class TreinamentoController {

    private final TreinamentoService service;
    private final String DIRETORIO_UPLOADS = "uploads/treinamentos/";

    public TreinamentoController(TreinamentoService service) {
        this.service = service;
    }

    // GET /treinamentos -> treinamentos disponíveis
    @GetMapping
    public List<Treinamento> listar() {
        return service.listar();
    }

    // POST /treinamentos?adminId=1 -> cadastra um treinamento com upload
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> cadastrar(
            @RequestParam Long adminId,
            @RequestParam String titulo,
            @RequestParam(required = false) String descricao,
            @RequestParam(required = false) Integer validadeMeses,
            @RequestParam(value = "arquivo", required = false) MultipartFile arquivo
    ) {
        try {
            String caminhoArquivoSalvo = null;

            if (arquivo != null && !arquivo.isEmpty()) {
                Path pastaDestino = Paths.get(DIRETORIO_UPLOADS);
                if (!Files.exists(pastaDestino)) {
                    Files.createDirectories(pastaDestino);
                }

                String nomeUnico = UUID.randomUUID() + "_" + arquivo.getOriginalFilename();
                Path caminhoCompleto = pastaDestino.resolve(nomeUnico);
                Files.copy(arquivo.getInputStream(), caminhoCompleto, StandardCopyOption.REPLACE_EXISTING);

                caminhoArquivoSalvo = "/arquivos/" + nomeUnico;
            }

            Treinamento treinamento = new Treinamento();
            treinamento.setTitulo(titulo);
            treinamento.setDescricao(descricao);
            treinamento.setValidadeMeses(validadeMeses);
            treinamento.setArquivoUrl(caminhoArquivoSalvo);

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(service.cadastrar(treinamento, adminId));

        } catch (SecurityException e) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (IOException e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao salvar o arquivo no servidor: " + e.getMessage());
        }
    }

    // POST /treinamentos/2/concluir?funcionarioId=3
    @PostMapping("/{id}/concluir")
    public ResponseEntity<?> concluir(@PathVariable Long id,
                                      @RequestParam Long funcionarioId) {
        try {
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(service.concluir(id, funcionarioId));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // GET /treinamentos/realizados/funcionario/3
    @GetMapping("/realizados/funcionario/{id}")
    public List<TreinamentoRealizado> listarRealizados(@PathVariable Long id) {
        return service.listarRealizados(id);
    }
}
