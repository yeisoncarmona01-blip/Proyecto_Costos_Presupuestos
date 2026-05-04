#include "rentabilidad.h"

// Utilidad = ingresos - costos
double calcular_utilidad(double ingresos, double costos) {
    return ingresos - costos;
}

// ROI = (utilidad / inversion) * 100
double calcular_roi(double utilidad, double inversion) {
    if (inversion == 0) return 0;
    return (utilidad / inversion) * 100;
}

// Margen = (utilidad / ingresos) * 100
double calcular_margen(double utilidad, double ingresos) {
    if (ingresos == 0) return 0;
    return (utilidad / ingresos) * 100;
}
