import MySQLdb

class MSql:

    dbName = 'doorbell'

    tableName = 'log'

    fieldTime = 'time'
    fieldUserAccept = 'user_accept'
    fieldStatus = 'status'

    def __init__(self):
        self.db = MySQLdb.connect('127.0.0.1','root','1234', dbName)

    def select(self, cmd):
        cur = self.db.cursor()
        cur.execute(cmd)
        return cur;

    def update(self, cmd):
        cur = self.db.cursor()
        cur.execute(cmd)
        db.commit()

    def insert(self, cmd):
        cur = self.db.cursor()
        cur.execute(cmd)
        db.commit()

    def close(self):
        self.db.close()
