#include <stdio.h>
int main() {
  int cantSimple, cantMedio, cantComplejo;
  float costoSimple, costoMedio, costoComplejo;
  float totalSimple, totalMedio, totalComplejo;
  float totalGeneral;
  int totalModulos;
  printf("=== CALCULO DE COSTOS POR MODULOS ===\n\n");
  printf("MODULO SIMPLE\n");
  printf("Cantidad: ");
  scanf("%d", &cantSimple);
  printf("Costo Unitario: ");
  scanf("%f", &costoSimple);
  printf("\nMODULO MEDIO\n");
  printf("Cantidad: ");
  scanf("%d", &cantMedio);
  printf("Costo Unitario: ");
  scanf("%f", &costoMedio);
  printf("\nMODULO COMPLEJO\n");
  printf("Cantidad: ");
  scanf("%d", &cantComplejo);
  printf("Costo Unitario: ");
  scanf("%f", &costoComplejo);
  totalSimple = cantSimple * costoSimple;
  totalMedio = cantMedio * costoMedio;
  totalComplejo = cantComplejo * costoComplejo;
  totalGeneral = totalSimple + totalMedio + totalComplejo;
  totalModulos = cantSimple + cantMedio + cantComplejo;
  printf("\n=== RESULTADOS ===\n");
  printf("Total de Modulos: %d\n", totalModulos);
  printf("\nDesglose:\n");
  printf("Simple: %d\n", cantSimple);
  printf("Medio: %d\n", cantMedio);
  printf("Complejo: %d\n", cantComplejo);
  printf("\nCosto Total Estimado: $%.2f\n", totalGeneral);
  return 0;
}