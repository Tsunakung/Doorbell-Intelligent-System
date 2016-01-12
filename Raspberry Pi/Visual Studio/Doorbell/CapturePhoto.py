import picamera
import thread
from subprocess import call
import sys
import glob
import time
import os
import MSql

class CapturePhoto:

    def capture(self):
        call (["pkill raspivid"], shell=True)
        call (["pkill vlc"], shell=True)
        conn = MSql()
        #conn = MySQLdb.connect('127.0.0.1','root','1234','doorbell')
        #cursor = conn.cursor()
        datetime = str(time.strftime("%d/%m/%y")+"-"+time.strftime("%H"+":"+"%M"))
        date = str(time.strftime("%d-%m-%Y"))

        if os.path.exists('/home/pi/Pictures/'+date):
            now = str(len(glob.glob('/home/pi/Pictures/'+date+'/'+date+'_*.jpg'))+1)
            imgid = date+"_"+now
            camera = picamera.PiCamera()
            pathimgid = "/home/pi/Pictures/"+date+"/"+imgid+".jpg"
            camera.capture(str(pathimgid))
            conn.insert("INSERT INTO transaction(time,img_id,user_accept,status) VALUES('"+datetime+"','"+imgid+"','','calling')")
            #cursor.execute("INSERT INTO transaction(time,img_id,user_accept,status) VALUES('"+datetime+"','"+imgid+"','','calling')")
            #conn.commit()
        else:
            os.makedirs('/home/pi/Pictures/'+date)
            now = str(len(glob.glob('/home/pi/Pictures/'+date+'/'+date+'_*.jpg'))+1)
            imgid = date+"_"+now
            camera = picamera.PiCamera()
            pathimgid = "/home/pi/Pictures/"+date+"/"+imgid+".jpg"
            camera.capture(str(pathimgid))
            conn.insert("INSERT INTO transaction(time,img_id,user_accept,status) VALUES('"+datetime+"','"+imgid+"','','calling')")
            #cursor.execute("INSERT INTO transaction(time,img_id,user_accept,status) VALUES('"+datetime+"','"+imgid+"','','calling')")
            #conn.commit()

        conn.close()
        call (["pkill raspivid"], shell=True)
        call (["pkill vlc"], shell=True)
