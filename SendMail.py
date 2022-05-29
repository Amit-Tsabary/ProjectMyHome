import smtplib

gmail_user = 'myhome.authentication@gmail.com'
gmail_password = 'Myhomeproject'
subject_msg = "MyHome Authentication Message"
body_message = "Please enter the following code in the relevant field: "


class SendMail:
    def __init__(self, send_from, from_password, send_to, subject, body):
        self.send_from = send_from
        self.from_password = from_password
        self.send_to = send_to
        self.subject = subject
        self.body = body

    def send(self, code):
        to = [self.send_to]

        email_text = """\
            From: %s
            To: %s
            Subject: %s

            %s
            """ % (self.send_from, ", ".join(to), self.subject, self.body)

        try:
            smtp_server = smtplib.SMTP_SSL('smtp.gmail.com', 465)
            smtp_server.ehlo()
            smtp_server.login(self.send_from, self.from_password)
            smtp_server.sendmail(self.send_from, to, email_text+str(code))
            smtp_server.close()
            print("Email sent successfully!")
        except Exception as ex:
            print("Something went wrong….", ex)
