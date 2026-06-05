package com.denkitronik.softcostservice.delivery.rest;

import com.denkitronik.softcostservice.domain.estimacion.EstimacionPresupuestoRequest;
import com.denkitronik.softcostservice.domain.estimacion.EstimacionPresupuestoResponse;
import com.denkitronik.softcostservice.domain.estimacion.IEstimacionPresupuestoService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/estimacion")
@CrossOrigin(origins = "*")
public class EstimacionPresupuestoRestController {

  @Autowired
  private IEstimacionPresupuestoService service;

  @PostMapping("/calcular")
  public ResponseEntity<?> calcular(
    @Valid @RequestBody EstimacionPresupuestoRequest request,
    BindingResult result) {

    if (result.hasErrors()) {
      return ResponseEntity.badRequest().body(result.getAllErrors());
    }

    EstimacionPresupuestoResponse response = service.calcular(request);
    return new ResponseEntity<>(response, HttpStatus.CREATED);
  }

  @GetMapping("/native/status")
  public ResponseEntity<?> nativeStatus() {
    boolean ok = service.isNativeAvailable();
    return ResponseEntity.ok(java.util.Map.of(
      "connected", ok,
      "message", ok ? "Librería nativa disponible" : "Librería nativa NO disponible"
    ));
  }
}