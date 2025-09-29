const WebSocket = require('ws');

const port = process.env.PORT || 8123;
const server = new WebSocket.Server({ port }, () => {
  console.log(`CoinCraft WS bridge listening on :${port}`);
});

const clients = new Set();

server.on('connection', (ws) => {
  clients.add(ws);
  ws.on('message', (msg) => {
    for (const c of clients) {
      if (c.readyState === WebSocket.OPEN) {
        try { c.send(msg.toString()); } catch {}
      }
    }
  });
  ws.on('close', () => clients.delete(ws));
});


