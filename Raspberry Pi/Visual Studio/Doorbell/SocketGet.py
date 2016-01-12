import socket                                         
import threading

class _ClientThread(threading.Thread):

    def __init__(self, pclientsocket, paddr, pthread):
        threading.Thread.__init__(self)
        self.clientsocket = pclientsocket
        self.addr = paddr
        self.thread = pthread

    def handler(self):
        return

    def run(self):
        self.handler()
        self.clientsocket.close()
        self.thread.remove(self)

class _ClientPing(_ClientThread):

    def handler(self):
        self.clientsocket.send("true".encode("UTF-8"))

class _ClientPassword(_ClientThread):

    def handler(self):
        getPassword = self.clientsocket.recv(1024).decode("UTF-8").rstrip()
        realPassword = getPassword()
        print 'Check Password {}'.format(password)
        if password == realPassword:
            self.clientsocket.send("true".encode("UTF-8"))
        else:
            self.clientsocket.send("false".encode("UTF-8"))

    def getPassword(self):
        return '1234';

class SocketGet(threading.Thread):

    def run(self):
        host = socket.gethostname()                           
        port = 8552
        threads = []
        socketserver = socket.socket(socket.AF_INET, socket.SOCK_STREAM)                                         
        print 'Open socket {}:{}'.format(socket.gethostbyname(host), port)
        socketserver.bind((host, port))                                  
        socketserver.listen(10)     

        while True:
            clientsocket,addr = socketserver.accept()
            print 'Connection from {}'.format(str(addr))
            type = clientsocket.recv(1024).decode("UTF-8").rstrip()
            print 'Type {}'.format(type)
            newThread = None

            if type == 'Ping':
                newThread = _ClientPing(clientsocket, addr, threads)
            elif type == 'Password':
                newThread = _ClientPassword(clientsocket, addr, threads)

            if newThread != None:
                newThread.start()
                threads.append(newThread)

        for t in threads:
            t.join()

        socketserver.close()

start = SocketGet()
start.start()
start.join()