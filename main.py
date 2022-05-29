import random
import requests
from http.server import BaseHTTPRequestHandler, HTTPServer
import mysql.connector
import bcrypt
from ArduinoCommunication import ArduinoCommunication
from SendMail import SendMail

host_name = "10.0.0.35"
server_port = 8000
arduino_port = "/dev/cu.usbmodem2101"

ADMIN_EMAIL = "amit.tsabary@gmail.com"
ADMIN_PASS = "Amit2404"
ADMIN_FIRST_NAME = "Amit"
ADMIN_LAST_NAME = "Tsabary"
ADMIN_DATA = [ADMIN_FIRST_NAME, ADMIN_LAST_NAME, ADMIN_EMAIL, ADMIN_PASS]

gmail_user = 'myhome.authentication@gmail.com'
gmail_password = 'Myhomeproject'
subject_msg = "MyHome Authentication Message"
body_message = "Please enter the following code in the relevant field: "


class UserDatabase:
    connect_password = None

    myDb = None
    myServer = None

    def __init__(self, mydatabase=None):
        if UserDatabase.myServer is None:
            UserDatabase.myServer = mysql.connector.connect(
                host="localhost",
                user="root",
                password=UserDatabase.connect_password
            )

        if mydatabase is not None:
            if UserDatabase.myDb is None:
                UserDatabase.myDb = mysql.connector.connect(
                    host="localhost",
                    user="root",
                    password=UserDatabase.connect_password,
                    database=mydatabase
                )

        self.mycursor = UserDatabase.myServer.cursor()

    def create_database(self, mydatabase):
        self.mycursor.execute("CREATE DATABASE IF NOT EXISTS " + mydatabase)

    def create_table(self, table_name, column_details):
        self.mycursor = UserDatabase.myDb.cursor()
        self.mycursor.execute("SHOW TABLES")
        exists = False
        for table in self.mycursor:
            if table_name in table:
                exists = True
        if not exists:
            self.mycursor.execute(f'CREATE TABLE {table_name} ({column_details})')

    # print("1 record inserted, ID:", mycursor.lastrowid)


def login(data):
    username = data[1]
    password = data[2]
    if len(username) == 0 or len(password) == 0:
        return " Please fill all fields."
    mycursor = UserDatabase.myDb.cursor()
    mycursor.execute("SELECT email, password, first_name, email, ID, DA FROM users")
    myresult = mycursor.fetchall()
    can_login = False
    name = ""
    ID = 0
    email = ""
    DA = "true"
    for row in myresult:
        if row[0] == username and bcrypt.checkpw(password.encode("utf-8"), bytes(row[1]).rstrip(b'\x00')):
            can_login = True
            name = row[2]
            email = row[3]
            ID = row[4]
            DA = row[5]

    if can_login:
        print("correct login attempt")
        return f'true_{name}_{email}_{ID}_{DA}'
    else:
        print("not correct login attempt")
        return " Incorrect Email or Password."


def reset_status():
    mycursor = UserDatabase.myDb.cursor()
    mycursor.execute("UPDATE devices SET status='off'")


def create_account(data):
    first_name = data[0]
    last_name = data[1]
    username = data[2]
    password = data[3]

    mycursor = UserDatabase.myDb.cursor()
    mycursor.execute("SELECT email FROM users")
    myresult = mycursor.fetchall()

    account_exists = False
    for row in myresult:
        if row[0] == username:
            account_exists = True
            break

    field_empty = False
    for x in data:
        if len(x) == 0:
            field_empty = True
            break

    if field_empty:
        return "false_fieldEmpty"
    else:
        if not account_exists:
            mycursor = UserDatabase.myDb.cursor()
            sql = "INSERT INTO users (first_name, last_name, email, password, DA) VALUES (%s, %s, %s, %s, %s)"
            val = (first_name, last_name, username, bcrypt.hashpw(password.encode('utf-8'), bcrypt.gensalt()), "true")
            mycursor.execute(sql, val)
            UserDatabase.myDb.commit()
            return "true"
        else:
            return "false_emailExists"


def create_device(data):
    name = data[1]
    device_type = data[2]
    pin = data[3]

    mycursor = UserDatabase.myDb.cursor()
    sql = "INSERT INTO devices (name, type, PIN, status) VALUES (%s, %s, %s, %s)"
    val = (name, device_type, pin, "off")
    mycursor.execute(sql, val)
    UserDatabase.myDb.commit()


def get_devices():
    devices = ""
    mycursor = UserDatabase.myDb.cursor()
    mycursor.execute("SELECT * FROM devices")
    myresult = mycursor.fetchall()

    for row in myresult:
        for i in row:
            devices += str(i) + "_"
        devices += "_"

    return devices


def turn(status, pin):
    if status == "on":
        arduino_communication.turn_on(int(pin))
    elif status == "off":
        arduino_communication.turn_off(int(pin))
    mycursor = UserDatabase.myDb.cursor()
    sql = f"UPDATE devices SET status = '{status}' WHERE PIN = '{pin}'"
    mycursor.execute(sql)
    UserDatabase.myDb.commit()


def authenticate(email, id_to_authenticate):
    send_mail = SendMail(gmail_user, gmail_password, email, subject_msg, body_message)
    authentication_code = str(random.randint(0, 999999)).zfill(6)
    send_mail.send(authentication_code)

    mycursor = UserDatabase.myDb.cursor()
    sql = "UPDATE users SET code = %s WHERE ID = %s"
    var = (bcrypt.hashpw(authentication_code.encode('utf-8'), bcrypt.gensalt()), id_to_authenticate)
    mycursor.execute(sql, var)
    UserDatabase.myDb.commit()


def check_code(code, user_id):
    mycursor = UserDatabase.myDb.cursor()
    mycursor.execute(f"SELECT code, first_name, email, DA FROM users WHERE ID='{user_id}'")
    myresult = mycursor.fetchall()
    if bcrypt.checkpw(code.encode("utf-8"), bytes(myresult[0][0]).rstrip(b'\x00')):
        return "true_" + myresult[0][1] +"_" + myresult[0][2] +"_" + user_id + "_" + myresult[0][3]
    else:
        return "false"


def DA(active, user_id):
    mycursor = UserDatabase.myDb.cursor()
    sql = "UPDATE users SET DA = %s WHERE ID = %s"
    if active:
        var = ("true", user_id)
    else:
        var = ("false", user_id)

    mycursor.execute(sql, var)
    UserDatabase.myDb.commit()


def get_accounts():
    accounts = ""
    mycursor = UserDatabase.myDb.cursor()
    mycursor.execute("SELECT * FROM users")
    myresult = mycursor.fetchall()

    for row in myresult:
        for i in row:
            accounts += str(i) + "_"
        accounts += "_"

    return accounts


def get_rooms():
    rooms = ""
    mycursor = UserDatabase.myDb.cursor()
    mycursor.execute("SELECT * FROM rooms")
    myresult = mycursor.fetchall()

    for row in myresult:
        for i in row:
            rooms += str(i) + "__"
        rooms += "_"

    return rooms


def get_dev_by_id(data):
    devices = ""
    mycursor = UserDatabase.myDb.cursor()
    mycursor.execute("SELECT * FROM devices")
    myresult = mycursor.fetchall()

    for row in myresult:
        if str(row[0]) in data:
            for i in row:
                devices += str(i) + "_"
            devices += "_"
    return devices


def create_room(room_name, devices):
    mycursor = UserDatabase.myDb.cursor()
    sql = "INSERT INTO rooms (name, devices) VALUES (%s, %s)"
    val = (room_name, devices)
    mycursor.execute(sql, val)
    UserDatabase.myDb.commit()


def edit_room(room_id, room_name, devices):
    mycursor = UserDatabase.myDb.cursor()
    sql = "UPDATE rooms SET name = %s, devices = %s WHERE ID  = %s"
    val = (room_name, devices, room_id)
    mycursor.execute(sql, val)
    UserDatabase.myDb.commit()


def turn_all(device_IDs, way):
    mycursor = UserDatabase.myDb.cursor()
    mycursor.execute("SELECT ID, pin FROM devices")
    myresult = mycursor.fetchall()
    for row in myresult:
        for id in device_IDs:
            if str(row[0]) == id:
                if(way):
                    turn("on", row[1])
                else:
                    turn("off", row[1])


def delete_device(device_id):
    mycursor = UserDatabase.myDb.cursor()
    sql = "SELECT pin FROM devices WHERE ID = %s"
    val = (device_id,)
    mycursor.execute(sql, val)
    myresult = mycursor.fetchall()
    turn("off", myresult[0][0])

    mycursor = UserDatabase.myDb.cursor()
    sql = "DELETE FROM devices WHERE ID = %s"
    val = (device_id,)
    mycursor.execute(sql, val)
    UserDatabase.myDb.commit()

    rooms = ""
    mycursor = UserDatabase.myDb.cursor()
    mycursor.execute("SELECT * FROM rooms")
    myresult = mycursor.fetchall()

    for row in myresult:
        if row[2].__contains__(device_id+"_"):
            string = row[2].replace(device_id+"_", "")
            mycursor = UserDatabase.myDb.cursor()
            sql = "UPDATE rooms SET devices = %s WHERE ID = %s"
            val = (string, device_id)
            mycursor.execute(sql, val)
            UserDatabase.myDb.commit()
        rooms += "_"


def delete_room(room_id):
    mycursor = UserDatabase.myDb.cursor()
    sql = "DELETE FROM rooms WHERE ID = %s"
    val = (room_id,)
    mycursor.execute(sql, val)
    UserDatabase.myDb.commit()


def edit_account(params):
    mycursor = UserDatabase.myDb.cursor()
    sql = "UPDATE users SET first_name = %s, last_name = %s, password = %s WHERE email  = %s"
    val = (params[0], params[1], bcrypt.hashpw(params[3].encode('utf-8'), bcrypt.gensalt()), params[2])
    mycursor.execute(sql, val)
    UserDatabase.myDb.commit()


class MyServer(BaseHTTPRequestHandler):
    def do_GET(self):
        self.send_response(200)
        self.send_header("Content-type", "text/html")
        a = self.path
        self.end_headers()
        if self.path.__contains__("/turnOn_"):
            pin = self.path.split("_")[1]
            turn('on', pin)

        elif self.path.__contains__("/turnOff_"):
            pin = self.path.split("_")[1]
            turn('off', pin)
        elif self.path.__contains__("turnOnAll"):
            data = self.path.split("_")
            turn_all(data[1:], True)
        elif self.path.__contains__("turnOffAll"):
            data = self.path.split("_")
            turn_all(data[1:], False)
        elif self.path.__contains__("/login_"):
            data = self.path.split("_")
            self.wfile.write(bytes(login(data), "utf-8"))

        elif self.path.__contains__("/create_"):
            data = self.path.split("_")
            self.wfile.write(bytes(create_account(data[1:]), "utf-8"))

        elif self.path.__contains__("/createDevice_"):
            data = self.path.split("_")
            create_device(data)
        elif self.path.__contains__("/displayDevices"):
            self.wfile.write(bytes(get_devices(), "utf-8"))
        elif self.path.__contains__("/getDevices"):
            self.wfile.write(bytes(get_devices(), "utf-8"))
        elif self.path.__contains__("/authenticate_"):
            data = self.path.split("_")
            authenticate(data[1], data[2])
        elif self.path.__contains__("/checkCode_"):
            data = self.path.split("_")
            self.wfile.write(bytes(check_code(data[1], data[2]), "utf-8"))
        elif self.path.__contains__("DATrue_"):
            data = self.path.split("_")
            DA(True, data[1])
        elif self.path.__contains__("DAFalse_"):
            data = self.path.split("_")
            DA(False, data[1])
        elif self.path.__contains__("/displayAccounts"):
            self.wfile.write(bytes(get_accounts(), "utf-8"))
        elif self.path.__contains__("/getAccounts"):
            self.wfile.write(bytes(get_accounts(), "utf-8"))
        elif self.path.__contains__("/getRooms") or self.path.__contains__("/displayRooms"):
            self.wfile.write(bytes(get_rooms(), "utf-8"))
        elif self.path.__contains__("displayDevById_"):
            data = self.path.split("_")
            self.wfile.write(bytes(get_dev_by_id(data[1:]), "utf-8"))
        elif self.path.__contains__("createRoom_"):
            data = self.path.split("__")
            create_room(data[0].split("_")[1], data[1])
        elif self.path.__contains__("editRoom"):
            data = self.path.split("__")
            edit_room(data[0].split("_")[1], data[0].split("_")[2], data[1])
        elif self.path.__contains__("deleteDevice_"):
            data = self.path.split("_")
            delete_device(data[1])
        elif self.path.__contains__("deleteRoom_"):
            data = self.path.split("_")
            delete_room(data[1])
        elif self.path.__contains__("editAccount_"):
            data = self.path.split("_")
            edit_account(data[1:])
        else:
            print("ALMOSTTTTTT")


if __name__ == "__main__":
    UserDatabase.connect_password = "Amit2404"
    UserDatabase().create_database("mydatabase")
    dbHandler = UserDatabase("mydatabase")
    dbHandler.create_table("users", "ID int NOT NULL AUTO_INCREMENT, first_name VARCHAR(255), last_name VARCHAR(255), "
                                    "email VARCHAR(255), password VARBINARY(255), code VARBINARY(255), "
                                    "DA VARCHAR(255), PRIMARY KEY (ID)")

    dbHandler.create_table("devices", "ID int NOT NULL AUTO_INCREMENT, name VARCHAR(255), type VARCHAR(255), "
                                      "PIN int, status VARCHAR(255), PRIMARY KEY (ID)")

    dbHandler.create_table("rooms", "ID int AUTO_INCREMENT, name VARCHAR(255), devices VARCHAR(255), PRIMARY KEY (ID)")

    create_account(ADMIN_DATA)

    arduino_communication = ArduinoCommunication(arduino_port)
    reset_status()

    webServer = HTTPServer((host_name, server_port), MyServer)
    print("Server started http://%s:%s" % (host_name, server_port))
    try:
        webServer.serve_forever()
    except KeyboardInterrupt:
        pass

    webServer.server_close()
    print("Server stopped.")

print(requests.get('https://localhost:8000'))
