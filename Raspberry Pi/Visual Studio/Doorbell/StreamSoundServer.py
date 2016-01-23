import socket
import pyaudio
import wave

CHUNK = 1024
FORMAT = pyaudio.paInt16
CHANNELS = 1
RATE = 8000
p = pyaudio.PyAudio()

def _streamSound(clientsocket):
    streamIn = p.open(format=FORMAT,
                channels=CHANNELS,
                rate=RATE,
                input=True,
                frames_per_buffer=CHUNK)
    while True:
        dataIn = streamIn.read(CHUNK)
        clientsocket.send(dataIn)
    streamIn.stop_stream()
    streamIn.close()





host = socket.gethostname()                           
port = 8555
threads = []
socketserver = socket.socket(socket.AF_INET, socket.SOCK_STREAM)                                         
print 'Open socket {}:{}'.format(socket.gethostbyname(host), port)
socketserver.bind((host, port))                                  
socketserver.listen(10)     

while True:
    clientsocket,addr = socketserver.accept()
    print 'Connection from {}'.format(str(addr))
    _streamSound(clientsocket)

socketserver.close()
p.terminate()
print 'Close socket {}:{}'.format(socket.gethostbyname(host), port)