package br.com.controle_user.controller;

import br.com.controle_user.model.UsuarioModel;
import br.com.controle_user.repository.UsuarioRepository;
import br.com.controle_user.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;


@CrossOrigin(origins = "http://localhost:4200")
@Tag(name = "Controle de Usuário", description = "Aplicação Controle Usuários API")
@RestController
@RequestMapping(path = "/usuarios")
public class UsuarioController {

    private static final Logger logger = LoggerFactory.getLogger(UsuarioController.class);
    private UsuarioService usuarioService;

    @Autowired
    public UsuarioController(UsuarioService usuarioService){

        this.usuarioService = usuarioService;
    }

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Operation(
            summary = "Salvar usuario",
            description = "Salvar usuario.",
            tags = {"usuario", "post"})
    @ApiResponses({
            @ApiResponse(responseCode = "201", content = { @Content(schema = @Schema(implementation = UsuarioModel.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) })
    })
    @PostMapping
    public ResponseEntity<UsuarioModel> saveUsuario(@RequestBody UsuarioModel usuarioModel){
        logger.info("Chamando método saveUsuario com requisição: " + " " + usuarioModel);
        usuarioService.saveUsuario(usuarioModel);
        return new ResponseEntity<>(usuarioModel, HttpStatus.OK);
    }

    @Operation(
            summary = "Recuperar usuário por Id",
            description = "Obtenha um usuário especificando seu id. A resposta é um usuário com id, título, descrição e status publicado.",
            tags = {"usuario", "get"})
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = UsuarioModel.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
    @GetMapping(path = "/{id}")
    public ResponseEntity<Optional<UsuarioModel>> getUsuarioById(@PathVariable Long id){
       logger.info("Chamando o método getUsuarioById com a requisição: " + " " + id);
       Optional<UsuarioModel> usuarioModel;
       try{
           usuarioModel = usuarioService.getUsuarioById(id);
           return new ResponseEntity<Optional<UsuarioModel>>(usuarioModel, HttpStatus.OK);
       }catch (NoSuchElementException nsee){
           return new ResponseEntity<Optional<UsuarioModel>>(HttpStatus.NOT_FOUND);
       }
    }

    @Operation(
            summary = "Listar todos os usuários",
            description = "Retorna uma lista com todos os usuários cadastrados.",
            tags = {"usuario", "get"})
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(array = @ArraySchema(schema = @Schema(implementation = UsuarioModel.class)), mediaType = "application/json")
            }),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) })
    })
    @GetMapping
    public ResponseEntity<List<UsuarioModel>> getAllUsuarios() {
        logger.info("Chamando o método getAllUsuarios");
        try {
            List<UsuarioModel> usuarios = usuarioService.getAllUsuarios();
            return new ResponseEntity<>(usuarios, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Erro ao buscar usuários: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @Operation(
            summary = "Deletar usuário por Id",
            description = "Deleta um usuário especificando seu id.",
            tags = {"usuario", "delete"})
    @ApiResponses({
            @ApiResponse(responseCode = "204", content = { @Content(schema = @Schema(implementation = UsuarioModel.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Optional<UsuarioModel>> deleteUsuario(@PathVariable Long id){
        logger.info("Chamando método deleteUsuario com a requisição: " + " " + id);
        try {
            usuarioService.deleteUsuario(id);
            return new ResponseEntity<Optional<UsuarioModel>>(HttpStatus.OK);
        }catch (NoSuchElementException nsee) {
            return new ResponseEntity<Optional<UsuarioModel>>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(
            summary = "Alterar usuário por Id",
            description = "Altere um usuário especificando seu id.",
            tags = {"usuario", "put"})
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = UsuarioModel.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
    @PutMapping(value="/{id}")
    public ResponseEntity<Optional<UsuarioModel>> updatedUsuario(@PathVariable Long id, @RequestBody UsuarioModel newUsuarioModel){
        logger.info("Chamando método updatedUsuario com a requisição: " + " " + id + " " + newUsuarioModel);
        Optional<UsuarioModel> usuarioModel = usuarioService.updatedUsuarioModel(id, newUsuarioModel);
        return new ResponseEntity<Optional<UsuarioModel>>(usuarioModel, HttpStatus.OK);
    }

    @PutMapping(value= "/{id}/ativo")
    public ResponseEntity<?> atualizarStatusAtivo(@PathVariable Long id, @RequestParam boolean ativo) {
        Optional<UsuarioModel> usuarioOpt = usuarioRepository.findById(id);
        if (usuarioOpt.isPresent()) {
            UsuarioModel usuarioModel = usuarioOpt.get();
            usuarioModel.setAtivo(ativo);
            usuarioRepository.save(usuarioModel);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }


}
