package com.denkitronik.clienteservice.delivery.rest;

import com.denkitronik.clienteservice.delivery.exception.ClienteNotFoundException;
import com.denkitronik.clienteservice.domain.entities.Cliente;
import com.denkitronik.clienteservice.domain.entities.Region;
import com.denkitronik.clienteservice.domain.services.IClienteService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ClienteRestController.class)
@Import(ClienteRestControllerTest.MethodSecurityTestConfig.class)
@DisplayName("ClienteRestController — pruebas de capa web con MockMvc")
class ClienteRestControllerTest {

    @TestConfiguration
    @EnableMethodSecurity
    static class MethodSecurityTestConfig {}

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IClienteService clienteService;

    @MockBean
    private JwtDecoder jwtDecoder;

    @Autowired
    private ObjectMapper objectMapper;

    private Cliente cliente;
    private Region region;
    private static final String BASE = "/api/v1/cliente-service";

    @BeforeEach
    void setUp() {
        region = new Region();
        region.setId(4L);
        region.setNombre("Europa");

        cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNombre("Ada");
        cliente.setApellido("Lovelace");
        cliente.setEmail("ada@babbage.uk");
        cliente.setRegion(region);
    }

    @Test
    @DisplayName("GET /clientes con ROLE_USER -> 200 con lista")
    void listarClientes_conRoleUser_debeRetornar200() throws Exception {
        when(clienteService.findAll()).thenReturn(List.of(cliente));

        mockMvc.perform(get(BASE + "/clientes")
                        .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_USER"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Ada"))
                .andExpect(jsonPath("$[0].email").value("ada@babbage.uk"));
    }

    @Test
    @DisplayName("GET /clientes sin token -> 401")
    void listarClientes_sinToken_debeRetornar401() throws Exception {
        mockMvc.perform(get(BASE + "/clientes"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("POST /clientes con ROLE_ADMIN y datos validos -> 201")
    void crearCliente_conRoleAdmin_debeRetornar201() throws Exception {
        when(clienteService.save(any(Cliente.class))).thenReturn(cliente);

        mockMvc.perform(post(BASE + "/clientes")
                        .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_ADMIN")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cliente)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre").value("Ada"));
    }

    @Test
    @DisplayName("POST /clientes con ROLE_USER -> 403 Forbidden")
    void crearCliente_conRoleUser_debeRetornar403() throws Exception {
        mockMvc.perform(post(BASE + "/clientes")
                        .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_USER")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cliente)))
                .andExpect(status().isForbidden());
    }
}