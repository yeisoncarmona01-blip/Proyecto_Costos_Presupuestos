package com.denkitronik.clienteservice.delivery.rest;

import com.denkitronik.clienteservice.domain.entities.Cliente;
import com.denkitronik.clienteservice.domain.entities.Region;
import com.denkitronik.clienteservice.domain.services.IClienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/${api.version}/cliente-service")
@CrossOrigin(origins = {"http://localhost:4200"})
@Tag(name = "Clientes", description = "CRUD de clientes y consulta de regiones")
@SecurityRequirement(name = "bearerAuth")
public class ClienteRestController {

    @Autowired
    private IClienteService clienteService;

    @Operation(summary = "Listar todos los clientes", description = "Devuelve la lista completa. Requiere rol USER o ADMIN.")
    @GetMapping("/clientes")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<List<Cliente>> listarClientes() {
        return ResponseEntity.ok(clienteService.findAll());
    }

    @Operation(summary = "Listar clientes paginado", description = "Devuelve pagina de 4 clientes. Requiere rol USER o ADMIN.")
    @GetMapping("/clientes/page/{page}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<Page<Cliente>> listarClientesPaginado(
            @Parameter(description = "Numero de pagina", example = "0") @PathVariable Integer page) {
        return ResponseEntity.ok(clienteService.findAll(PageRequest.of(page, 4)));
    }

    @Operation(summary = "Buscar cliente por ID", description = "Requiere rol USER o ADMIN.")
    @GetMapping("/clientes/{id}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<Cliente> buscarCliente(
            @Parameter(description = "ID del cliente", example = "1") @PathVariable Long id) {
        return ResponseEntity.ok(clienteService.findById(id));
    }

    @Operation(summary = "Crear un nuevo cliente", description = "Requiere rol ADMIN.")
    @PostMapping("/clientes")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> crearCliente(@Valid @RequestBody Cliente cliente,
                                          BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(construirErrores(result));
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(clienteService.save(cliente));
    }

    @Operation(summary = "Actualizar un cliente", description = "Requiere rol ADMIN.")
    @PutMapping("/clientes/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> actualizarCliente(@Valid @RequestBody Cliente cliente,
                                               BindingResult result,
                                               @Parameter(description = "ID del cliente", example = "1") @PathVariable Long id) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(construirErrores(result));
        }
        Cliente actual = clienteService.findById(id);
        actual.setNombre(cliente.getNombre());
        actual.setApellido(cliente.getApellido());
        actual.setEmail(cliente.getEmail());
        actual.setRegion(cliente.getRegion());
        actual.setFoto(cliente.getFoto());
        return ResponseEntity.status(HttpStatus.CREATED).body(clienteService.save(actual));
    }

    @Operation(summary = "Eliminar un cliente", description = "Requiere rol ADMIN.")
    @DeleteMapping("/clientes/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminarCliente(
            @Parameter(description = "ID del cliente", example = "1") @PathVariable Long id) {
        clienteService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Listar todas las regiones", description = "Requiere rol USER o ADMIN.")
    @GetMapping("/clientes/regiones")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<List<Region>> listarRegiones() {
        return ResponseEntity.ok(clienteService.findAllRegiones());
    }

    private Map<String, Object> construirErrores(BindingResult result) {
        Map<String, Object> errores = new HashMap<>();
        List<String> listaErrores = result.getFieldErrors()
                .stream()
                .map(fe -> "El campo '" + fe.getField() + "' " + fe.getDefaultMessage())
                .collect(Collectors.toList());
        errores.put("errors", listaErrores);
        return errores;
    }
}