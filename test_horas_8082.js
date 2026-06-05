const http = require('http');
const qs = new URLSearchParams({ tareas: '10', promedio: '5', tarifa: '20000' }).toString();
const options = {
  hostname: 'localhost',
  port: 8082,
  path: '/api/v1/horas/calcular?' + qs,
  method: 'GET',
  timeout: 10000
};

const req = http.request(options, (res) => {
  console.log('statusCode', res.statusCode);
  let body = '';
  res.setEncoding('utf8');
  res.on('data', (chunk) => body += chunk);
  res.on('end', () => {
    console.log('body:', body);
  });
});

req.on('error', (e) => {
  console.error('ERR', e.message);
});

req.end();
