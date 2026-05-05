#include "lib_recursosH.h"

double costo_emp(double s, double p, double i) {
    return s + p + i;
}

double costo_eq(int n, double c) {
    return n * c;
}

double prod(double h, int t) {
    if (h == 0) return 0;
    return t / h;
}
