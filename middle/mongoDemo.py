import pymongo
from pymongo.mongo_client import MongoClient
from pymongo.server_api import ServerApi	

class db_cluster(object):
	def __init__(self,conn_str):
		super(db_cluster, self).__init__()
		self.client_conn = self.connect(conn_str)

	def connect(self,conn_str):
		# set a 5-second connection timeout
		client = pymongo.MongoClient(conn_str, serverSelectionTimeoutMS=5000)
		try:
			client.server_info()
			print("Connected to MongoDB Cluster")
		except Exception:
			print("Unable to connect to Mongo DB Cluster")

		return client

	def get_db(self,db_name):
		dbnames = self.client_conn.list_database_names()
		if db_name in dbnames:
			db = self.client_conn[db_name]
			return db
		else:
			print("Database with name",db_name,"does not exsist")
			return False

	def get_collection(self,db_name,collection_name):
		try:
			db = self.get_db(db_name)
			list_of_collections = db.list_collection_names() 
			if collection_name in list_of_collections:
				collection = db[collection_name]
				return collection
			else:
				print("Collection with name",collection_name,"does not exsist")
		except Exception:
			print("Error getting collection")

	def insert_one_entry(self,db_name,collection_name,entry):
		try:
			collect = self.get_collection(db_name,collection_name)
			collect_id = collect.insert_one(entry)
		except Exception:
			print("Error creating entry")
	#def insert_many_entries(self,db_name,collection_name,entries)


if __name__ == '__main__':
	db_cluster = db_cluster("")
	player = {
	"_id"		 : "2",
	"First_Name" : "Jusuf",
	"Last_Name"  : "Nurkic"
	}
	db_cluster.insert_one_entry('NBA','Rockets',player)
		



