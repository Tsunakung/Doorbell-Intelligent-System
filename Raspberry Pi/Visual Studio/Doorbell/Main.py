import SocketServer
import StreamServer

startSocket = SocketServer.SocketServer()
startSocket.start()

startStream = StreamServer.StreamServer()
startStream.start()

#ไม่ต้องมีเวลาทำงานจริง (มั้ง)
startSocket.join()
startStream.join()