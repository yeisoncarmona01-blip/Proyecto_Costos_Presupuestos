#include <stdio.h>
#include "lib_recursosH.h"

int main() {
    double ce = costo_emp(2000, 500, 300);
    double cq = costo_eq(4, ce);
    double pr = prod(40, 20);

    printf("Costo emp: %.2f\n", ce);
    printf("Costo eq: %.2f\n", cq);
    printf("Prod: %.2f\n", pr);

    return 0;
}
