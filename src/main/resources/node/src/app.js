const express = require('express');
const http = require('http');
const socketIo = require('socket.io');
const path = require('path');
const cors = require('cors');

const app = express();
const server = http.createServer(app);
const io = socketIo(server);
app.use(cors());

// 정적 파일 경로 설정
app.use(express.static(path.join(__dirname, 'public')));

let activeRooms = {};
let unreadMessages = {};

// 채팅 HTML 파일을 서빙하는 라우트
app.get('/chat/:sender/:receiver', (req, res) => {
    res.sendFile(path.join(__dirname, 'public', 'chat.html'));
});

// 채팅 방 목록 페이지를 서빙하는 라우트
app.get('/rooms/:user', (req, res) => {
    res.sendFile(path.join(__dirname, 'public', 'rooms.html'));
});

// 채팅 방 목록 API 엔드포인트
app.get('/api/rooms/:user', (req, res) => {
    const user = req.params.user;
    const userRooms = Object.keys(activeRooms).filter(roomId => roomId.includes(user));
    const roomsWithUnreadCounts = userRooms.map(roomId => ({
        roomId,
        unreadCount: unreadMessages[roomId] && unreadMessages[roomId][user] ? unreadMessages[roomId][user] : 0
    }));
    res.json({ rooms: roomsWithUnreadCounts });
});

// 읽지 않은 메시지 수 API 엔드포인트
app.get('/api/unread/:user', (req, res) => {
    const user = req.params.user;
    const userRooms = Object.keys(activeRooms).filter(roomId => roomId.includes(user));
    let totalUnread = 0;
    userRooms.forEach(roomId => {
        if (unreadMessages[roomId] && unreadMessages[roomId][user]) {
            totalUnread += unreadMessages[roomId][user];
        }
    });
    res.json({ unreadCount: totalUnread });
});

// Socket.IO 연결 관리
io.on('connection', (socket) => {
    console.log('User connected');

    // 방에 조인하는 이벤트
    socket.on('joinRoom', ({ sender, receiver }) => {
        const roomId = [sender, receiver].sort().join('-');
        socket.join(roomId);
        socket.roomId = roomId;
        if (!activeRooms[roomId]) {
            activeRooms[roomId] = 0;
            unreadMessages[roomId] = {};
        }
        activeRooms[roomId] += 1;
        if (!unreadMessages[roomId][sender]) {
            unreadMessages[roomId][sender] = 0;
        }
    });

    // 메시지를 전송하는 이벤트
    socket.on('SEND', (msg) => {
        const roomId = socket.roomId;
        const sender = msg.sender;
        const receiver = roomId.split('-').find(user => user !== sender);

        if (!unreadMessages[roomId][receiver]) {
            unreadMessages[roomId][receiver] = 0;
        }
        unreadMessages[roomId][receiver] += 1;

        io.to(roomId).emit('SEND', msg);
    });

    // 연결 종료 시 처리
    socket.on('disconnect', () => {
        if (socket.roomId) {
            activeRooms[socket.roomId] -= 1;
            if (activeRooms[socket.roomId] === 0) {
                delete activeRooms[socket.roomId];
                delete unreadMessages[socket.roomId];
            }
        }
    });
});

// 서버 리스닝
const PORT = process.env.PORT || 4000;
server.listen(PORT, () => {
    console.log(`Server is running on port ${PORT}`);
});
