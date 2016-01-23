import socket
import pyaudio
import wave

CHUNK = 4096
FORMAT = pyaudio.paInt16
CHANNELS = 1
RATE = 44100
p = pyaudio.PyAudio()

def _streamSound(clientsocket):
    streamOut = p.open(format=FORMAT,
                    channels=CHANNELS,
                    output_device_index=2,
                    rate=RATE,
                    output=True,
                    frames_per_buffer=CHUNK)
    isPlay = True
    dataOut = clientsocket.recv(CHUNK)
    while isPlay and dataOut:
        try:
            streamOut.write(dataOut)
            dataOut = clientsocket.recv(CHUNK)
        except socket.error as msg:
            isPlay = False
            print 'Socket.error{}',msg
        except IOError as ex:
            dataOut = None
            streamOut = p.open(format=FORMAT,
                            channels=CHANNELS,
                            rate=RATE,
                            output_device_index=2,
                            output=True,
                            frames_per_buffer=CHUNK)
            print 'IOError{}',ex
    streamOut.close()


host = "192.168.1.33"
port = 8556
threads = []
socketserver = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
print 'Open socket {}:{}'.format(socket.gethostbyname(host), port)
socketserver.bind((host, port))
socketserver.listen(10)

while True:
    clientsocket,addr = socketserver.accept()
    print 'Connection from {}'.format(str(addr))
    _streamSound(clientsocket)
    print 'Disconnect'
    clientsocket.close()

socketserver.close()
p.terminate()
print 'Close socket {}:{}'.format(socket.gethostbyname(host), port)