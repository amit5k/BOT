# importing library 

import RPi.GPIO as GPIO
from time import sleep
import threading
import socket
from socket import *

# Creating server

HOST = ''
PORT = 21567
BUFSIZE =1574
ADDR = (HOST,PORT)

tcpSerSock = socket(AF_INET, SOCK_STREAM)
tcpSerSock.bind(ADDR)
tcpSerSock.listen(5)

# position value(90,0,180)
# pwm of servo motor can differ, so you can change value of mid, left, right as your requirement

mid = 1.5
left = 1.0
right =2.0
slope = (right -left)/8
pos = mid


GPIO.setmode(GPIO.BOARD)
GPIO.setup(3, GPIO.OUT)
pwm=GPIO.PWM(3, 50)

pwm.start(mid)

while True:
        print 'Waiting for connection'
        tcpCliSock,addr = tcpSerSock.accept()
        print '...connected from :', addr
        try:
                while True:
                        data = ''
                        data = tcpCliSock.recv(BUFSIZE)
                        if not data:
                                break
                        x,y,z = data.split('@')
                        print x

                        if int(x)>30 and pos<right :
                            pos=pos+slope
                            print pos
                            pwm.ChangeDutyCycle(pos)  
                            
                            
                            
                        elif int(x)<-30 and pos>left:
                                pos=pos-slope
                                print pos
                                pwm.ChangeDutyCycle(pos)
                        else                         :    
                                print "none"   
                        
                           
        except KeyboardInterrupt:
               pwm.stop()
               GPIO.cleanup()
               tcpSerSock.close();                















        

               





                            

 
          
        



                            
