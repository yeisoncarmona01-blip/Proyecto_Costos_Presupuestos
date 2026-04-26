#include <stdio.h>
#include <stdlib.h>
#include <math.h>
#include <string.h>

// Estructura para almacenar los coeficientes COCOMO
typedef struct {
    float a;
    float b;
    float c;
    float d;
    char nombre[50];
} TipoProyecto;

// Estructura para los resultados
typedef struct {
    float esfuerzo;        // Person-Months (PM)
    float tiempo;          // Meses
    float personal;        // Personas promedio
} Resultados;

// Función para obtener los coeficientes según el tipo de proyecto
TipoProyecto obtenerCoeficientes(int tipo) {
    TipoProyecto proyectos[3];
    
    // Orgánico (simple, pequeño equipo)
    proyectos[0].a = 2.4;
    proyectos[0].b = 1.05;
    proyectos[0].c = 2.5;
    proyectos[0].d = 0.38;
    strcpy(proyectos[0].nombre, "Orgánico");
    
    // Semi-acoplado (medio)
    proyectos[1].a = 3.0;
    proyectos[1].b = 1.12;
    proyectos[1].c = 2.5;
    proyectos[1].d = 0.35;
    strcpy(proyectos[1].nombre, "Semi-acoplado");
    
    // Rígido (grande, complejos, requisitos estrictos)
    proyectos[2].a = 3.6;
    proyectos[2].b = 1.20;
    proyectos[2].c = 2.5;
    proyectos[2].d = 0.32;
    strcpy(proyectos[2].nombre, "Rígido");
    
    if (tipo >= 0 && tipo <= 2) {
        return proyectos[tipo];
    } else {
        return proyectos[0]; // Por defecto, orgánico
    }
}

// Función para calcular COCOMO básico
Resultados calcularCOCOMO(float kloc, int tipo) {
    Resultados res;
    TipoProyecto tp = obtenerCoeficientes(tipo);
    
    // Esfuerzo = a × (KLOC)^b
    res.esfuerzo = tp.a * pow(kloc, tp.b);
    
    // Tiempo = c × (Esfuerzo)^d
    res.tiempo = tp.c * pow(res.esfuerzo, tp.d);
    
    // Personal promedio = Esfuerzo / Tiempo
    res.personal = res.esfuerzo / res.tiempo;
    
    return res;
}

// Función para mostrar el menú de tipos de proyecto
int mostrarMenuTipos() {
    printf("\n--- Tipos de Proyecto COCOMO ---\n");
    printf("0. Orgánico (simple, pequeño equipo)\n");
    printf("1. Semi-acoplado (medio)\n");
    printf("2. Rígido (grande, complejos, requisitos estrictos)\n");
    printf("\nSeleccione tipo de proyecto (0-2): ");
    
    int tipo;
    scanf("%d", &tipo);
    
    if (tipo < 0 || tipo > 2) {
        printf("Tipo inválido. Se utilizará Orgánico por defecto.\n");
        return 0;
    }
    return tipo;
}

// Función para mostrar los coeficientes
void mostrarCoeficientes(TipoProyecto tp) {
    printf("\n--- Coeficientes para %s ---\n", tp.nombre);
    printf("a = %.2f\n", tp.a);
    printf("b = %.2f\n", tp.b);
    printf("c = %.2f\n", tp.c);
    printf("d = %.2f\n", tp.d);
}

// Función para mostrar los resultados
void mostrarResultados(float kloc, Resultados res, TipoProyecto tp) {
    printf("       RESULTADOS COCOMO BÁSICO\n");
    printf("Tipo de Proyecto: %s\n", tp.nombre);
    printf("Tamaño (KLOC): %.2f\n", kloc);
    printf("Esfuerzo (PM):        %.2f Person-Months\n", res.esfuerzo);
    printf("Tiempo de Desarrollo: %.2f meses\n", res.tiempo);
    printf("Personal Promedio:    %.2f personas\n", res.personal);
    printf("\nFórmulas utilizadas:\n");
    printf("  Esfuerzo = a × (KLOC)^b\n");
    printf("  Tiempo = c × (Esfuerzo)^d\n");
    printf("  Personal = Esfuerzo / Tiempo\n");
    
}

// Función para ejecutar el estimador interactivo
void estimadorInteractivo() {
    printf("║   ESTIMADOR COCOMO BÁSICO v1.0        ║\n");
    printf("║   Métodos de Estimación de Costos     ║\n");
    
    // Obtener tipo de proyecto
    int tipo = mostrarMenuTipos();
    TipoProyecto tp = obtenerCoeficientes(tipo);
    
    // Mostrar coeficientes
    mostrarCoeficientes(tp);
    
    // Solicitar tamaño del proyecto
    printf("\nIngrese el tamaño del proyecto en KLOC (Kilo Lines of Code): ");
    float kloc;
    scanf("%f", &kloc);
    
    if (kloc <= 0) {
        printf("Error: El tamaño debe ser mayor que 0.\n");
        return;
    }
    
    // Calcular COCOMO
    Resultados resultados = calcularCOCOMO(kloc, tipo);
    
    // Mostrar resultados
    mostrarResultados(kloc, resultados, tp);
}

// Función para mostrar ejemplos comparativos
void mostrarEjemplos() {
    printf("║    EJEMPLOS COMPARATIVOS COCOMO       ║\n");
    
    float kloc_test = 10.0;
    printf("\nPara un proyecto de %.2f KLOC:\n", kloc_test);
    printf("\n%-20s | %15s | %15s | %15s\n", "Tipo", "Esfuerzo (PM)", "Tiempo (meses)", "Personal");
    printf("%-20s | %15s | %15s | %15s\n", "----", "----", "----", "----");
    
    for (int i = 0; i < 3; i++) {
        Resultados res = calcularCOCOMO(kloc_test, i);
        TipoProyecto tp = obtenerCoeficientes(i);
        printf("%-20s | %15.2f | %15.2f | %15.2f\n", 
               tp.nombre, res.esfuerzo, res.tiempo, res.personal);
    }
}

int main() {
    int opcion;
    
    do {
        printf("║   MENÚ PRINCIPAL - COCOMO BÁSICO      ║\n");
        printf("1. Estimador interactivo\n");
        printf("2. Ver ejemplos comparativos\n");
        printf("3. Salir\n");
        printf("\nSeleccione opción (1-3): ");
        
        scanf("%d", &opcion);
        
        // Limpiar buffer de entrada
        while (getchar() != '\n');
        
        switch (opcion) {
            case 1:
                estimadorInteractivo();
                break;
            case 2:
                mostrarEjemplos();
                break;
            case 3:
                printf("\n¡Hasta luego!\n\n");
                break;
            default:
                printf("Opción inválida. Intente de nuevo.\n");
        }
    } while (opcion != 3);
    
    return 0;
}
