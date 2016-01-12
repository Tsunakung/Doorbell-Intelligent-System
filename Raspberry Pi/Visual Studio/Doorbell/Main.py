import SocketServer
import StreamServer

startSocket = SocketServer()
startSocket.start()

startStream = StreamServer()
startStream.start()

#ไม่ต้องมีเวลาทำงานจริง (มั้ง)
startSocket.join()
startStream.join()