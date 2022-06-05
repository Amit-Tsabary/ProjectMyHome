import pyfirmata
import time


# a class that manages the communication with the arduino device
class ArduinoCommunication:

    def __init__(self, port):
        self.port = port
        self.board = pyfirmata.Arduino(self.port)
        time.sleep(1.3)

        self.iterator = pyfirmata.util.Iterator(self.board)
        self.iterator.start()
        time.sleep(0.5)

    def turn_on(self, pin):
        time.sleep(0.2)
        self.board.digital[pin].write(1)

    def turn_off(self, pin):
        time.sleep(0.2)  # wait 0.5 seconds
        self.board.digital[pin].write(0)

