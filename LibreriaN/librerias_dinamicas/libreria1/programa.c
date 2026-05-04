#include <stdio.h>
#include "rentabilidad.h"

int main() {
    double ingresos = 10000;
    double costos = 7000;

    double utilidad = calcular_utilidad(ingresos, costos);
    double roi = calcular_roi(utilidad, costos);
    double margen = calcular_margen(utilidad, ingresos);

    printf("Ingresos: %.2f\n", ingresos);
    printf("Costos: %.2f\n", costos);
    printf("Utilidad: %.2f\n", utilidad);
    printf("ROI: %.2f%%\n", roi);
    printf("Margen de ganancia: %.2f%%\n", margen);

    if (utilidad > 0) {
        printf("El proyecto ES rentable\n");
    } else {
        printf("El proyecto NO es rentable\n");
    }

    return 0;
}
