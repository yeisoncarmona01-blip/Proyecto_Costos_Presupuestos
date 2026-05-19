package com.denkitronik.softcostservice.delivery.rest;

import com.denkitronik.softcostservice.domain.entities.ProyectoCosto;
import com.denkitronik.softcostservice.domain.services.IProyectoCostoService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.validation.BindingResult;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/softcost")
@CrossOrigin(origins = "*")
public class ProyectoCostoRestController {

  @Autowired
  private IProyectoCostoService service;

  @GetMapping
  public ResponseEntity<?> listar() {
    return ResponseEntity.ok(service.findAll());
  }

  @GetMapping("/page/{page}")
  public ResponseEntity<Page<ProyectoCosto>> listarPageable(
    @PathVariable Integer page) {

    return ResponseEntity.ok(
      service.findAll(PageRequest.of(page, 4)));
  }

  @GetMapping("/{id}")
  public ResponseEntity<?> ver(@PathVariable Long id) {

    return ResponseEntity.ok(service.findById(id));
  }

  @PostMapping
  public ResponseEntity<?> crear(
    @Valid @RequestBody ProyectoCosto proyectoCosto,
    BindingResult result) {

    if (result.hasErrors()) {
      return ResponseEntity.badRequest().body(result.getAllErrors());
    }

    return new ResponseEntity<>(
      service.save(proyectoCosto),
      HttpStatus.CREATED);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> eliminar(@PathVariable Long id) {

    service.delete(id);

    return ResponseEntity.noContent().build();
  }
}
