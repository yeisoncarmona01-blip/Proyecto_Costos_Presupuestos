const http = require('http');

const payload = {
  "nombre_proyecto": "Sistema de Gestión Empresarial",
  "cliente": "Empresa ABC S.A.",
  "fecha": "20/5/2026",
  "costos_directos": 45000,
  "costos_indirectos": 12000,
  "costo_total": 57000,
  "duracion_meses": 4,
  "tamano_equipo": 6,
  "costo_mensual": 14250,
  "costo_por_persona": 9500
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
