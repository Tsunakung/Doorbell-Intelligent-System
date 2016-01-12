import socket                                         
import threading
import os
from wifi import Cell,Scheme

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

class _ClientManageWifi(_ClientThread):

    def handler(self):
        if receive == 'scan':
            print 'Connection from {}'.format(str(self.addr))
            print 'Status: {}'.format(receive)
            file = open('/etc/wpa_supplicant/wpa_supplicant.conf', 'r')
            currentWifi = file.readlines()
            dataSplit = currentWifi[4].split('ssid="')[1]
            currentSSID = dataSplit.split('"')[0]
            file.close()
            data = Cell.all('wlan0')
            self.clientsocket.send(str(len(data)) + '\r\n')
            self.clientsocket.send(currentSSID + '\r\n')
            for i in range(len(data)):
                splitSSid = str(data[i]).split('=')[1]
                self.clientsocket.send(str(splitSSid.split(')')[0]) + '\r\n')
            print 'Exit ScanWifi'
            
        if 'modify' in receive:
            print 'Status: {}'.format(receive)
            ssid = receive.split(':')[1]
            password = receive.split(':')[2]
            editFile = open('/etc/wpa_supplicant/wpa_supplicant.conf','w')
            editFile.write('ctrl_interface=DIR=/var/run/wpa_supplicant GROUP=netdev\n')
            editFile.write('update_config=1\n')
            editFile.write('\nnetwork={\n     ssid="')
            editFile.write(ssid)
            editFile.write('"\n     psk="')
            editFile.write(password)
            editFile.write('"\n}')
            editFile.close()
            print 'Exit Modify Wifi'
            os.system("sudo reboot")

class SocketServer(threading.Thread):

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
            elif type == 'ManageWifi':
                newThread = _ClientManageWifi(clientsocket, addr, threads)

            if newThread != None:
                newThread.start()
                threads.append(newThread)

        for t in threads:
            t.join()

        socketserver.close()
