const app = require('express')();
const http = require('http').createServer(app);
const io = require('socket.io')(http);

app.get('/', (req, res) => {
    res.sendFile(__dirname + '/chat.html');
});

let socketList = [];

io.on('connection', (socket) => {
    socketList.push(socket);
    console.log('User Join');

    socket.on('SEND', (msg) => {
        socketList.forEach(function(item) {
            if (item != socket) {
                item.emit('SEND', msg);
            }
        });
        // io.emit('response_message', msg); // Uncomment this line if you want to emit to all users
    });

    socket.on('disconnect', () => {
        socketList.splice(socketList.indexOf(socket), 1);
        console.log('user disconnected');
    });
});

// TEST CODE GOES HERE
(async function(){
    // Add your test code here
})();

http.listen(4000, () => {
    console.log('Connected at 4000'); // Fixed the port number
});
