const http = require('http');
const data = JSON.stringify({
  nombre_proyecto: 'Sistema de Gestión v2',
  duracion_meses: 6,
  tamano_equipo: 5,
  salario_promedio_mensual: 3000,
  infraestructura_mensual: 1000,
  licencias_mensuales: 501,
  contingencia_porcentaje: 13
});

const options = {
  hostname: 'localhost',
  port: 8081,
  path: '/api/v1/estimacion/calcular',
  method: 'POST',
  headers: {
    'Content-Type': 'application/json',
    'Content-Length': Buffer.byteLength(data)
  },
  timeout: 10000
};

const req = http.request(options, (res) => {
  console.log('statusCode', res.statusCode);
  res.setEncoding('utf8');
  res.on('data', (chunk) => process.stdout.write(chunk));
});

req.on('error', (e) => {
  console.error('ERR', e.message);
  process.exit(1);
});

req.write(data);
req.end();
