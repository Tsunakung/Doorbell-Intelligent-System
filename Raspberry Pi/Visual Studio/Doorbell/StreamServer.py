import os
import thread
import threading
import time
import socket
import RPi.GPIO as GPIO
import picamera
import sys
from subprocess import call
import glob

class StreamServer(threading.Thread):

    def __call__(self):
        return

    def __init__(self):
        GPIO.setmode(GPIO.BCM)
        GPIO.setup(17,GPIO.IN)

    def streaming_start (command,delay):
        time.sleep(delay)
        os.system(command)

    def prepare_stream(ip):    
	    try:
            thread.start_new_thread(streaming_start,("raspivid -o - -n -t 9999999 -w 320 -h 200 --hflip | cvlc -vvv stream:///dev/stdin --sout '#standard{access=http,mux=ts,dst="+ip+":8554}' :demux=h264",2))
            thread.start_new_thread(streaming_start,("cvlc -vvv alsa://hw:1,0 --sout '#transcode{acodec=mpga,ab=128}:standard{access=http,mux=ts,dst="+ip+":8555}'",4))
        except:
            print "Error"

    def auto_start():
        try:
            thread.start_new_thread(streaming_start,("sudo python /home/pi/StartSocketForManageWifi.py",4))
        except:
            print "Error AutoStart"

    def take_photo():
        try:
             thread.start_new_thread(streaming_start,("python /home/pi/CodePython/testPythonTakePhoto.py",4))
        except:
            print "Error TakePhoto"

    def run(self):
        auto_start()
        state = "stop"
        prev_input = 0
        ipaddr = ""
        s = socket.socket(socket.AF_INET,socket.SOCK_DGRAM)
        while True:
            time.sleep(5)
            gw = os.popen("ip -4 route show default").read().split()
            button = GPIO.input(17)
            if gw==[]:
                if state=="start":
                    call (["pkill raspivid"], shell=True)
                    call (["pkill vlc"], shell=True)
                    state = "stop"
                    print "IN IF ONE"
                    #print "Disconnect : "+state
                    time.sleep(1)

            if((not prev_input) and button ):
                call (["pkill raspivid"], shell=True)
                call (["pkill vlc"], shell=True)
                print "Start Thread Take Picture"
                take_photo()
                time.sleep(5)
                #os.system('sudo pkill -f "sudo python /home/pi/CodePython/testPythonTakePhoto.py"')
                s.connect((gw[2],0))
                ipaddr = s.getsockname()[0]
                prepare_stream(ipaddr)
                print "Exit Take Picture"

            if gw!=[]:
                if state=="stop" :
                    s.connect((gw[2],0))
                    ipaddr = s.getsockname()[0]
                    call (["pkill raspivid"], shell=True)
                    call (["pkill vlc"], shell=True)
                    prepare_stream(ipaddr)
                    state = "start"
                    print "IN IF THREE"
                    #print "Connect :"+state
            prev_input = button