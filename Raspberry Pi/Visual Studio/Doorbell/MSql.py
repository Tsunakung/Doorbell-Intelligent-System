import MySQLdb

class MSql:

    def __init__(self):
        self.db = MySQLdb.connect(host="localhost", user="root", passwd="1234", db="doorbell")

    def select(self, cmd):
        cur = db.cursor()
        cur.execute(cmd)
        return cur;

    def update(self, cmd):
        cur = db.cursor()
        cur.execute(cmd)
        db.commit()

    def insert(self, cmd):
        cur = db.cursor()
        cur.execute(cmd)
        db.commit()

    def close(self):
        db.close()
