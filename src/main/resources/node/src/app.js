const express = require('express');
const http = require('http');
const socketIo = require('socket.io');
const path = require('path');
const cors = require('cors');

const app = express();
const server = http.createServer(app);
const io = socketIo(server);
app.use(cors());

app.use(express.static(path.join(__dirname, 'src')));

app.get('/chat/:sender/:receiver', (req, res) => {
    res.sendFile(path.join(__dirname, 'chat.html'));
});

app.get('/rooms/:user', (req, res) => {
    res.sendFile(path.join(__dirname, 'rooms.html'));
});

const activeRooms = {};

io.on('connection', (socket) => {
    console.log('User connected');

    socket.on('joinRoom', ({ sender, receiver }) => {
        const roomId = [sender, receiver].sort().join('-');
        socket.join(roomId);
        socket.roomId = roomId;
        if (!activeRooms[roomId]) {
            activeRooms[roomId] = 0;
        }
        activeRooms[roomId] += 1;
        console.log(`User ${sender} joined room ${roomId}`);
    });

    socket.on('SEND', (msg) => {
        console.log(`Message received: ${msg.text}`);
        io.to(socket.roomId).emit('SEND', msg);
    });

    socket.on('disconnect', () => {
        if (socket.roomId) {
            activeRooms[socket.roomId] -= 1;
            if (activeRooms[socket.roomId] === 0) {
                delete activeRooms[socket.roomId];
            }
            console.log(`User disconnected from room ${socket.roomId}`);
        }
    });
});

server.listen(4000, () => {
    console.log('Connected at 4000');
});
