/*
 * Sistema de Gestión de Costos de Proyecto
 * Permite administrar costos directos e indirectos, calcular totales y eliminar elementos.
 * Compilación: gcc costos_proyecto.c -o costos_proyecto
 * Uso: ./costos_proyecto
 */

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <ctype.h>

#define MAX_NOMBRE 100

// Estructura para representar un costo
typedef struct Costo {
    char nombre[MAX_NOMBRE];
    int tipo;           // 0 = Directo, 1 = Indirecto
    double monto;
    struct Costo* siguiente;
} Costo;

// Prototipos de funciones
void limpiar_buffer();
void mostrar_menu();
void agregar_costo(Costo** cabeza);
void eliminar_costo(Costo** cabeza);
void listar_costos(Costo* cabeza);
void calcular_totales(Costo* cabeza, double* total_directo, double* total_indirecto);
void liberar_lista(Costo** cabeza);

int main() {
    Costo* lista = NULL;
    int opcion;

    do {
        mostrar_menu();
        printf("Seleccione una opción: ");
        if (scanf("%d", &opcion) != 1) {
            printf("Entrada inválida. Por favor ingrese un número.\n");
            limpiar_buffer();
            opcion = 0;
            continue;
        }
        limpiar_buffer();

        switch (opcion) {
            case 1:
                agregar_costo(&lista);
                break;
            case 2:
                eliminar_costo(&lista);
                break;
            case 3:
                listar_costos(lista);
                break;
            case 4: {
                double dir = 0.0, ind = 0.0;
                calcular_totales(lista, &dir, &ind);
                printf("\n--- RESUMEN DE COSTOS ---\n");
                printf("Total Costos Directos:   %.2f\n", dir);
                printf("Total Costos Indirectos: %.2f\n", ind);
                printf("Total del Proyecto:      %.2f\n", dir + ind);
                printf("--------------------------\n");
                break;
            }
            case 5:
                printf("Saliendo del programa. ¡Hasta luego!\n");
                break;
            default:
                printf("Opción no válida. Intente de nuevo.\n");
        }
        printf("\n");
    } while (opcion != 5);

    liberar_lista(&lista);
    return 0;
}

// Limpia el buffer de entrada estándar
void limpiar_buffer() {
    int c;
    while ((c = getchar()) != '\n' && c != EOF);
}

// Muestra el menú principal
void mostrar_menu() {
    printf("================================\n");
    printf("  GESTIÓN DE COSTOS DEL PROYECTO\n");
    printf("================================\n");
    printf("1. Agregar costo\n");
    printf("2. Eliminar costo (por índice)\n");
    printf("3. Listar todos los costos\n");
    printf("4. Calcular totales (directos/indirectos)\n");
    printf("5. Salir\n");
    printf("================================\n");
}

// Agrega un nuevo costo al final de la lista
void agregar_costo(Costo** cabeza) {
    Costo* nuevo = (Costo*)malloc(sizeof(Costo));
    if (nuevo == NULL) {
        printf("Error de memoria. No se pudo agregar el costo.\n");
        return;
    }

    printf("\n--- AGREGAR COSTO ---\n");
    printf("Nombre del costo: ");
    fgets(nuevo->nombre, MAX_NOMBRE, stdin);
    // Eliminar salto de línea final
    nuevo->nombre[strcspn(nuevo->nombre, "\n")] = '\0';

    // Validar tipo
    char tipo_opcion;
    do {
        printf("Tipo (D = Directo, I = Indirecto): ");
        scanf("%c", &tipo_opcion);
        limpiar_buffer();
        tipo_opcion = toupper(tipo_opcion);
        if (tipo_opcion != 'D' && tipo_opcion != 'I') {
            printf("Opción inválida. Ingrese D o I.\n");
        }
    } while (tipo_opcion != 'D' && tipo_opcion != 'I');
    nuevo->tipo = (tipo_opcion == 'D') ? 0 : 1;

    printf("Monto (Ej: 1250.50): ");
    while (scanf("%lf", &nuevo->monto) != 1 || nuevo->monto < 0) {
        printf("Entrada inválida. Ingrese un número positivo: ");
        limpiar_buffer();
    }
    limpiar_buffer();

    nuevo->siguiente = NULL;

    // Insertar al final de la lista
    if (*cabeza == NULL) {
        *cabeza = nuevo;
    } else {
        Costo* actual = *cabeza;
        while (actual->siguiente != NULL) {
            actual = actual->siguiente;
        }
        actual->siguiente = nuevo;
    }
    printf("Costo '%s' agregado correctamente.\n", nuevo->nombre);
}

// Elimina un costo según su índice (posición) mostrando la lista numerada
void eliminar_costo(Costo** cabeza) {
    if (*cabeza == NULL) {
        printf("No hay costos registrados para eliminar.\n");
        return;
    }

    listar_costos(*cabeza); // Mostrar lista con índices

    int indice;
    printf("Ingrese el número del costo a eliminar: ");
    if (scanf("%d", &indice) != 1 || indice < 1) {
        printf("Índice inválido.\n");
        limpiar_buffer();
        return;
    }
    limpiar_buffer();

    Costo* actual = *cabeza;
    Costo* anterior = NULL;
    int pos = 1;

    while (actual != NULL && pos < indice) {
        anterior = actual;
        actual = actual->siguiente;
        pos++;
    }

    if (actual == NULL) {
        printf("No existe un costo con el índice %d.\n", indice);
        return;
    }

    // Remover nodo
    if (anterior == NULL) {
        // Eliminar el primer nodo
        *cabeza = actual->siguiente;
    } else {
        anterior->siguiente = actual->siguiente;
    }

    printf("Costo '%s' (%.2f) eliminado.\n", actual->nombre, actual->monto);
    free(actual);
}

// Lista todos los costos con índice secuencial, tipo, nombre y monto
void listar_costos(Costo* cabeza) {
    if (cabeza == NULL) {
        printf("No hay costos registrados.\n");
        return;
    }

    printf("\n--- LISTA DE COSTOS ---\n");
    printf(" # | Tipo      | Nombre                           | Monto\n");
    printf("-----------------------------------------------------------\n");
    Costo* actual = cabeza;
    int contador = 1;
    while (actual != NULL) {
        printf("%2d | %-9s | %-30s | %10.2f\n",
               contador,
               (actual->tipo == 0) ? "Directo" : "Indirecto",
               actual->nombre,
               actual->monto);
        actual = actual->siguiente;
        contador++;
    }
    printf("-----------------------------------------------------------\n");
}

// Calcula los totales de costos directos e indirectos
void calcular_totales(Costo* cabeza, double* total_directo, double* total_indirecto) {
    *total_directo = 0.0;
    *total_indirecto = 0.0;
    Costo* actual = cabeza;
    while (actual != NULL) {
        if (actual->tipo == 0)
            *total_directo += actual->monto;
        else
            *total_indirecto += actual->monto;
        actual = actual->siguiente;
    }
}

// Libera toda la memoria utilizada por la lista enlazada
void liberar_lista(Costo** cabeza) {
    Costo* actual = *cabeza;
    while (actual != NULL) {
        Costo* temp = actual;
        actual = actual->siguiente;
        free(temp);
    }
    *cabeza = NULL;
}