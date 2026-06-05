package com.denkitronik.softcostservice.domain.estimacion;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Structure;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class EstimacionPresupuestoServiceImpl implements IEstimacionPresupuestoService {

  private static final Logger log =
    LoggerFactory.getLogger(EstimacionPresupuestoServiceImpl.class);

  @Value("${estimacion.native.library-name:estimacion-service}")
  private String libraryName;

  @Value("${estimacion.native.library-path:}")
  private String libraryPath;

  @Override
  public EstimacionPresupuestoResponse calcular(EstimacionPresupuestoRequest request) {

    try {

      EstimacionNativeLibrary nativeLibrary = loadLibrary();
      ResultadoEstimacionNative resultado = new ResultadoEstimacionNative();
      resultado.write();

      int status = nativeLibrary.calcular_estimacion(
        request.getNombreProyecto(),
        request.getDuracionMeses(),
        request.getTamanoEquipo(),
        request.getSalarioPromedio(),
        request.getInfraestructuraMensual(),
        request.getLicenciasMensuales(),
        request.getContingenciaPorcentaje(),
        resultado);

      if (status != 0) {
        throw new EstimacionServiceException(
          "La librería nativa retornó un código de error: " + status);
      }

      resultado.read();

      return toResponse(resultado);

    } catch (UnsatisfiedLinkError | IllegalArgumentException e) {
      log.error("No fue posible cargar o ejecutar la librería de estimación", e);
      throw new EstimacionServiceException(
        "No fue posible conectar con la librería de estimación", e);
    }
  }

  @Override
  public boolean isNativeAvailable() {
    try {
      // Try to load the library — if fails, UnsatisfiedLinkError is thrown
      EstimacionNativeLibrary lib = loadLibrary();
      return lib != null;
    } catch (Throwable t) {
      log.warn("Librería nativa no disponible: {}", t.getMessage());
      return false;
    }
  }

  private EstimacionNativeLibrary loadLibrary() {

    if (libraryPath != null && !libraryPath.isBlank()) {
      log.info("Cargando librería nativa desde ruta: {}", libraryPath);
      return Native.load(libraryPath, EstimacionNativeLibrary.class);
    }

    log.info("Cargando librería nativa por nombre: {}", libraryName);
    return Native.load(libraryName, EstimacionNativeLibrary.class);
  }

  private EstimacionPresupuestoResponse toResponse(ResultadoEstimacionNative resultado) {

    EstimacionPresupuestoResponse response = new EstimacionPresupuestoResponse();
    response.setNombreProyecto(nativeString(resultado.nombreProyecto));
    response.setCostoPersonal(resultado.costoPersonal);
    response.setCostoInfraestructura(resultado.costoInfraestructura);
    response.setCostoLicencias(resultado.costoLicencias);
    response.setSubtotal(resultado.subtotal);
    response.setContingenciaMonto(resultado.contingenciaMonto);
    response.setTotalPresupuesto(resultado.totalPresupuesto);
    response.setPorcentajePersonal(resultado.porcentajePersonal);
    response.setPorcentajeInfraestructura(resultado.porcentajeInfraestructura);
    response.setPorcentajeLicencias(resultado.porcentajeLicencias);
    response.setPorcentajeContingencia(resultado.porcentajeContingencia);
    return response;
  }

  private String nativeString(byte[] buffer) {
    int length = 0;

    while (length < buffer.length && buffer[length] != 0) {
      length++;
    }

    return new String(buffer, 0, length).trim();
  }

  public interface EstimacionNativeLibrary extends Library {

    int calcular_estimacion(
      String nombreProyecto,
      int duracionMeses,
      int tamanoEquipo,
      double salarioPromedio,
      double infraestructuraMensual,
      double licenciasMensuales,
      double contingenciaPorcentaje,
      ResultadoEstimacionNative resultado);
  }

  @Structure.FieldOrder({
    "nombreProyecto",
    "costoPersonal",
    "costoInfraestructura",
    "costoLicencias",
    "subtotal",
    "contingenciaMonto",
    "totalPresupuesto",
    "porcentajePersonal",
    "porcentajeInfraestructura",
    "porcentajeLicencias",
    "porcentajeContingencia"
  })
  public static class ResultadoEstimacionNative extends Structure implements Structure.ByReference {

    public byte[] nombreProyecto = new byte[256];
    public double costoPersonal;
    public double costoInfraestructura;
    public double costoLicencias;
    public double subtotal;
    public double contingenciaMonto;
    public double totalPresupuesto;
    public double porcentajePersonal;
    public double porcentajeInfraestructura;
    public double porcentajeLicencias;
    public double porcentajeContingencia;

    @Override
    protected List<String> getFieldOrder() {
      return Arrays.asList(
        "nombreProyecto",
        "costoPersonal",
        "costoInfraestructura",
        "costoLicencias",
        "subtotal",
        "contingenciaMonto",
        "totalPresupuesto",
        "porcentajePersonal",
        "porcentajeInfraestructura",
        "porcentajeLicencias",
        "porcentajeContingencia");
    }
  }
}