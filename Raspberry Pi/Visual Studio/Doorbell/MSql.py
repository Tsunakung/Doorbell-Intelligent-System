import MySQLdb

class MSql:

    def __init__(self):
        self.db = MySQLdb.connect('127.0.0.1','root','1234','doorbell')

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
