import socket
import pyaudio
import wave

CHUNK = 512
FORMAT = pyaudio.paInt16
CHANNELS = 1
RATE = 44100
p = pyaudio.PyAudio()

def _streamSound(clientsocket):
    streamIn = p.open(format=FORMAT,
                channels=CHANNELS,
                rate=RATE,
                input_device_index=2,
                input=True,
                frames_per_buffer=CHUNK)
    isPlay = True
    while isPlay:

        try:
            #print "isPlay: "+str(isPlay)
            dataIn = streamIn.read(CHUNK)
            clientsocket.send(dataIn)
            #isPlay = clientsocket.recv(16).decode("UTF-8").rstrip() == 'Play'
        #except IOError as ex:
        #    dataIn = None
        except socket.error as msg:
            isPlay = False
            print 'Socket.error{}',msg
        except IOError as ex:
            dataIn = None
            streamIn = p.open(format=FORMAT,
                channels=CHANNELS,
                rate=RATE,
                input_device_index=2,
                input=True,
                frames_per_buffer=CHUNK)
            print 'IOError{}',ex
	streamIn.stop_stream()
    streamIn.close()
	
host = "192.168.1.33"
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
    #clientsocket.close()
    print 'Disconnect'
    clientsocket.close()

socketserver.close()
p.terminate()
print 'Close socket {}:{}'.format(socket.gethostbyname(host), port)