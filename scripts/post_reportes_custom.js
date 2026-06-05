const http = require('http');

const payload = {
  "nombre_proyecto": "Gestión Empresarial",
  "cliente": "Empresa TLSHOP",
  "fecha": "27/5/2026",
  "costos_directos": 2000,
  "costos_indirectos": 10000,
  "costo_total": 12000,
  "duracion_meses": 4,
  "tamano_equipo": 6,
  "costo_mensual": 3000,
  "costo_por_persona": 2000
};

const data = JSON.stringify(payload);

const options = {
  hostname: 'localhost',
  port: 8083,
  path: '/api/v1/reportes/generar',
  method: 'POST',
  headers: {
    'Content-Type': 'application/json',
    'Content-Length': Buffer.byteLength(data)
  }
};

const req = http.request(options, (res) => {
  let body = '';
  console.log(`STATUS: ${res.statusCode}`);
  console.log('HEADERS:', JSON.stringify(res.headers));
  res.setEncoding('utf8');
  res.on('data', (chunk) => { body += chunk; });
  res.on('end', () => {
    console.log('Response body:');
    try {
      console.log(JSON.parse(body));
    } catch (e) {
      console.log(body);
    }
  });
});

req.on('error', (e) => {
  console.error(`problem with request: ${e.message}`);
});

req.write(data);
req.end();
