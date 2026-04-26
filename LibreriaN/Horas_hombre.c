/*
 * ============================================================
 *  ESTIMACIÓN DE HORAS HOMBRE
 *  Métodos: Estimación por Analogía, PERT y Simple
 * ============================================================
 */

#include <stdio.h>
#include <stdlib.h>

/* ---------- Estructuras ---------- */

typedef struct {
    int    numero;
    char   nombre[50];
    double horas_optimista;   /* Estimación optimista  (O) */
    double horas_probable;    /* Estimación más probable (M) */
    double horas_pesimista;   /* Estimación pesimista   (P) */
    double tarifa_hora;       /* USD/hora por tarea     */
} Tarea;

typedef struct {
    double horas_totales;
    double costo_total;
    double horas_pert;        /* Media PERT = (O + 4M + P) / 6 */
    double varianza;          /* σ² = ((P - O) / 6)²           */
    double desviacion;        /* σ  = (P - O) / 6              */
} Resultado;

/* ---------- Prototipos ---------- */

double calcular_pert      (double O, double M, double P);
double calcular_varianza  (double O, double P);
double calcular_desviacion(double O, double P);
void   mostrar_separador  (void);
void   mostrar_encabezado (void);

/* ---------- Funciones ---------- */

double calcular_pert(double O, double M, double P) {
    return (O + 4.0 * M + P) / 6.0;
}

double calcular_varianza(double O, double P) {
    double d = (P - O) / 6.0;
    return d * d;
}

double calcular_desviacion(double O, double P) {
    return (P - O) / 6.0;
}

void mostrar_separador(void) {
    printf("================================================================\n");
}

void mostrar_encabezado(void) {
    mostrar_separador();
    printf("         SISTEMA DE ESTIMACION DE HORAS HOMBRE (HH)\n");
    printf("         Metodos: Simple | PERT | Analogia\n");
    mostrar_separador();
}

/* ---------- Programa principal ---------- */

int main(void) {
    int     n_tareas;
    Tarea  *tareas;
    Resultado resultado;
    int     metodo;

    mostrar_encabezado();

    /* --- Selección de método --- */
    printf("\nSeleccione el metodo de estimacion:\n");
    printf("  [1] Estimacion Simple     (Hora Promedio unica)\n");
    printf("  [2] Estimacion PERT       (Optimista / Probable / Pesimista)\n");
    printf("  [3] Estimacion por Analogia (Referencia historica)\n");
    printf("\nOpcion: ");
    scanf("%d", &metodo);

    if (metodo < 1 || metodo > 3) {
        printf("\n[ERROR] Opcion invalida. Saliendo...\n");
        return EXIT_FAILURE;
    }

    /* --- Número de tareas --- */
    printf("\nIngrese el numero de tareas: ");
    scanf("%d", &n_tareas);

    if (n_tareas <= 0) {
        printf("\n[ERROR] El numero de tareas debe ser mayor a 0.\n");
        return EXIT_FAILURE;
    }

    tareas = (Tarea *)malloc(n_tareas * sizeof(Tarea));
    if (!tareas) {
        printf("\n[ERROR] Sin memoria suficiente.\n");
        return EXIT_FAILURE;
    }

    /* =====================================================
     *  MÉTODO 1 — ESTIMACIÓN SIMPLE
     * ===================================================== */
    if (metodo == 1) {
        printf("\n--- ESTIMACION SIMPLE ---\n");
        printf("Ingrese los datos para cada tarea:\n\n");

        resultado.horas_totales = 0.0;
        resultado.costo_total   = 0.0;

        for (int i = 0; i < n_tareas; i++) {
            tareas[i].numero = i + 1;
            printf("  Tarea #%d\n", i + 1);
            printf("    Nombre          : ");
            scanf(" %49[^\n]", tareas[i].nombre);
            printf("    Hora Promedio   : ");
            scanf("%lf", &tareas[i].horas_probable);
            printf("    Tarifa ($/hora) : ");
            scanf("%lf", &tareas[i].tarifa_hora);

            resultado.horas_totales += tareas[i].horas_probable;
            resultado.costo_total   += tareas[i].horas_probable * tareas[i].tarifa_hora;
            printf("\n");
        }

        /* Reporte */
        mostrar_separador();
        printf("                     REPORTE FINAL\n");
        mostrar_separador();
        printf("%-5s %-20s %12s %12s %14s\n",
               "N°", "Tarea", "HH", "Tarifa/h", "Costo ($)");
        mostrar_separador();

        for (int i = 0; i < n_tareas; i++) {
            double costo_tarea = tareas[i].horas_probable * tareas[i].tarifa_hora;
            printf("%-5d %-20s %12.2f %12.2f %14.2f\n",
                   tareas[i].numero,
                   tareas[i].nombre,
                   tareas[i].horas_probable,
                   tareas[i].tarifa_hora,
                   costo_tarea);
        }

        mostrar_separador();
        printf("%-26s %12.2f %28.2f\n",
               "TOTAL", resultado.horas_totales, resultado.costo_total);
        mostrar_separador();
        printf("\nRESUMEN:\n");
        printf("  Total de tareas       : %d\n",   n_tareas);
        printf("  Total Horas Hombre    : %.2f HH\n", resultado.horas_totales);
        printf("  Costo total estimado  : $ %.2f\n",  resultado.costo_total);
    }

    /* =====================================================
     *  MÉTODO 2 — ESTIMACIÓN PERT
     * ===================================================== */
    else if (metodo == 2) {
        printf("\n--- ESTIMACION PERT (Beta-PERT) ---\n");
        printf("Formula: HH = (O + 4*M + P) / 6\n\n");

        resultado.horas_totales = 0.0;
        resultado.costo_total   = 0.0;
        resultado.varianza      = 0.0;

        for (int i = 0; i < n_tareas; i++) {
            tareas[i].numero = i + 1;
            printf("  Tarea #%d\n", i + 1);
            printf("    Nombre                   : ");
            scanf(" %49[^\n]", tareas[i].nombre);
            printf("    Estimacion Optimista  (O): ");
            scanf("%lf", &tareas[i].horas_optimista);
            printf("    Estimacion Probable   (M): ");
            scanf("%lf", &tareas[i].horas_probable);
            printf("    Estimacion Pesimista  (P): ");
            scanf("%lf", &tareas[i].horas_pesimista);
            printf("    Tarifa ($/hora)          : ");
            scanf("%lf", &tareas[i].tarifa_hora);

            double hh_pert = calcular_pert(tareas[i].horas_optimista,
                                           tareas[i].horas_probable,
                                           tareas[i].horas_pesimista);
            resultado.horas_totales += hh_pert;
            resultado.costo_total   += hh_pert * tareas[i].tarifa_hora;
            resultado.varianza      += calcular_varianza(tareas[i].horas_optimista,
                                                         tareas[i].horas_pesimista);
            printf("\n");
        }

        resultado.desviacion = 0.0;
        /* Desviación estándar del proyecto = sqrt(suma varianzas) */
        double suma_var = resultado.varianza;
        /* Calculamos sqrt manualmente con Newton-Raphson simple */
        if (suma_var > 0) {
            double x = suma_var / 2.0;
            for (int k = 0; k < 20; k++)
                x = (x + suma_var / x) / 2.0;
            resultado.desviacion = x;
        }

        /* Reporte */
        mostrar_separador();
        printf("                     REPORTE PERT\n");
        mostrar_separador();
        printf("%-5s %-18s %8s %8s %8s %10s %12s\n",
               "N°", "Tarea", "O", "M", "P", "HH-PERT", "Costo ($)");
        mostrar_separador();

        for (int i = 0; i < n_tareas; i++) {
            double hh = calcular_pert(tareas[i].horas_optimista,
                                      tareas[i].horas_probable,
                                      tareas[i].horas_pesimista);
            printf("%-5d %-18s %8.2f %8.2f %8.2f %10.2f %12.2f\n",
                   tareas[i].numero, tareas[i].nombre,
                   tareas[i].horas_optimista,
                   tareas[i].horas_probable,
                   tareas[i].horas_pesimista,
                   hh,
                   hh * tareas[i].tarifa_hora);
        }

        mostrar_separador();
        printf("\nRESUMEN PERT:\n");
        printf("  Total de tareas          : %d\n",     n_tareas);
        printf("  Total HH estimadas (PERT): %.2f HH\n", resultado.horas_totales);
        printf("  Varianza del proyecto    : %.4f\n",    resultado.varianza);
        printf("  Desviacion estandar (σ)  : %.2f HH\n", resultado.desviacion);
        printf("  Rango optimista (μ-σ)    : %.2f HH\n",
               resultado.horas_totales - resultado.desviacion);
        printf("  Rango pesimista (μ+σ)    : %.2f HH\n",
               resultado.horas_totales + resultado.desviacion);
        printf("  Costo total estimado     : $ %.2f\n",  resultado.costo_total);
        mostrar_separador();
    }

    /* =====================================================
     *  MÉTODO 3 — ESTIMACIÓN POR ANALOGÍA
     * ===================================================== */
    else if (metodo == 3) {
        double factor_ajuste;
        printf("\n--- ESTIMACION POR ANALOGIA ---\n");
        printf("Ingrese el factor de ajuste (1.0 = sin cambios, >1 mas complejo): ");
        scanf("%lf", &factor_ajuste);

        if (factor_ajuste <= 0) {
            printf("[ERROR] El factor de ajuste debe ser positivo.\n");
            free(tareas);
            return EXIT_FAILURE;
        }

        printf("\nIngrese los datos historicos de cada tarea:\n\n");

        resultado.horas_totales = 0.0;
        resultado.costo_total   = 0.0;

        for (int i = 0; i < n_tareas; i++) {
            tareas[i].numero = i + 1;
            printf("  Tarea #%d\n", i + 1);
            printf("    Nombre                        : ");
            scanf(" %49[^\n]", tareas[i].nombre);
            printf("    HH historicas (referencia)    : ");
            scanf("%lf", &tareas[i].horas_probable);
            printf("    Tarifa ($/hora)               : ");
            scanf("%lf", &tareas[i].tarifa_hora);

            double hh_ajustada = tareas[i].horas_probable * factor_ajuste;
            resultado.horas_totales += hh_ajustada;
            resultado.costo_total   += hh_ajustada * tareas[i].tarifa_hora;
            printf("\n");
        }

        /* Reporte */
        mostrar_separador();
        printf("             REPORTE POR ANALOGIA (Factor=%.2f)\n", factor_ajuste);
        mostrar_separador();
        printf("%-5s %-20s %12s %12s %14s\n",
               "N°", "Tarea", "HH-Hist.", "HH-Ajust.", "Costo ($)");
        mostrar_separador();

        for (int i = 0; i < n_tareas; i++) {
            double hh_aj = tareas[i].horas_probable * factor_ajuste;
            printf("%-5d %-20s %12.2f %12.2f %14.2f\n",
                   tareas[i].numero, tareas[i].nombre,
                   tareas[i].horas_probable,
                   hh_aj,
                   hh_aj * tareas[i].tarifa_hora);
        }

        mostrar_separador();
        printf("\nRESUMEN:\n");
        printf("  Total de tareas          : %d\n",     n_tareas);
        printf("  Factor de ajuste         : %.2f\n",   factor_ajuste);
        printf("  Total HH estimadas       : %.2f HH\n", resultado.horas_totales);
        printf("  Costo total estimado     : $ %.2f\n",  resultado.costo_total);
        mostrar_separador();
    }

    free(tareas);
    printf("\nPrograma finalizado correctamente.\n\n");
    return EXIT_SUCCESS;
}