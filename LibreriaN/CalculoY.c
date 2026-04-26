#include <stdio.h>
#include <string.h>

#define MAX_NOMBRE 120

static void limpiar_buffer(void) {
	int c;
	while ((c = getchar()) != '\n' && c != EOF) {
	}
}

int main(void) {
	char nombreProyecto[MAX_NOMBRE];
	int duracionMeses;
	int tamanoEquipo;
	double salarioPromedioMensual;
	double infraestructuraMensual;
	double licenciasMensuales;
	double contingenciaPorcentaje;

	double costoPersonal;
	double costoInfraestructura;
	double costoLicencias;
	double subtotal;
	double contingenciaMonto;
	double totalPresupuesto;

	printf("= Estimacion de Presupuestos =\n\n");

	printf("Nombre del proyecto: ");
	if (fgets(nombreProyecto, sizeof(nombreProyecto), stdin) == NULL) {
		printf("No fue posible leer el nombre del proyecto.\n");
		return 1;
	}
	nombreProyecto[strcspn(nombreProyecto, "\n")] = '\0';

	printf("Duracion (meses): ");
	if (scanf("%d", &duracionMeses) != 1 || duracionMeses <= 0) {
		printf("La duracion debe ser un entero positivo.\n");
		return 1;
	}

	printf("Tamano del equipo: ");
	if (scanf("%d", &tamanoEquipo) != 1 || tamanoEquipo <= 0) {
		printf("El tamano del equipo debe ser un entero positivo.\n");
		return 1;
	}

	printf("Salario promedio mensual ($): ");
	if (scanf("%lf", &salarioPromedioMensual) != 1 || salarioPromedioMensual < 0) {
		printf("El salario promedio mensual debe ser un numero valido.\n");
		return 1;
	}

	printf("Infraestructura mensual ($): ");
	if (scanf("%lf", &infraestructuraMensual) != 1 || infraestructuraMensual < 0) {
		printf("El costo de infraestructura mensual debe ser un numero valido.\n");
		return 1;
	}

	printf("Licencias mensuales ($): ");
	if (scanf("%lf", &licenciasMensuales) != 1 || licenciasMensuales < 0) {
		printf("El costo de licencias mensuales debe ser un numero valido.\n");
		return 1;
	}

	printf("Contingencia (porcentaje %%): ");
	if (scanf("%lf", &contingenciaPorcentaje) != 1 || contingenciaPorcentaje < 0) {
		printf("La contingencia debe ser un porcentaje valido.\n");
		return 1;
	}

	limpiar_buffer();

	costoPersonal = (double)duracionMeses * (double)tamanoEquipo * salarioPromedioMensual;
	costoInfraestructura = (double)duracionMeses * infraestructuraMensual;
	costoLicencias = (double)duracionMeses * licenciasMensuales;

	subtotal = costoPersonal + costoInfraestructura + costoLicencias;
	contingenciaMonto = subtotal * (contingenciaPorcentaje / 100.0);
	totalPresupuesto = subtotal + contingenciaMonto;

	printf("\n= Desglose del Presupuesto =\n");
	printf("Proyecto: %s\n", nombreProyecto[0] ? nombreProyecto : "(sin nombre)");
	printf("Duracion: %d meses\n", duracionMeses);
	printf("Equipo: %d personas\n\n", tamanoEquipo);

	printf("Costo personal:        $%.2f\n", costoPersonal);
	printf("Infraestructura:       $%.2f\n", costoInfraestructura);
	printf("Licencias:             $%.2f\n", costoLicencias);
	printf("Subtotal:              $%.2f\n", subtotal);
	printf("Contingencia (%.2f%%):  $%.2f\n", contingenciaPorcentaje, contingenciaMonto);
	printf("TOTAL PRESUPUESTO:     $%.2f\n", totalPresupuesto);

	if (totalPresupuesto > 0.0) {
		printf("\nDistribucion de costos sobre total:\n");
		printf("Personal:        %.2f%%\n", (costoPersonal / totalPresupuesto) * 100.0);
		printf("Infraestructura: %.2f%%\n", (costoInfraestructura / totalPresupuesto) * 100.0);
		printf("Licencias:       %.2f%%\n", (costoLicencias / totalPresupuesto) * 100.0);
		printf("Contingencia:    %.2f%%\n", (contingenciaMonto / totalPresupuesto) * 100.0);
	}

	return 0;
}
