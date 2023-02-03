import pymongo
import uuid	
from datetime import date

class db_cluster(object):
	def __init__(self,conn_str):
		super(db_cluster, self).__init__()
		self.client_conn = self.connect(conn_str)
		#self.DB = self.get_db(db_name)
		#self.collection = self.get_collection(collection_name)

	def connect(self,conn_str):
		# set a 5-second connection timeout
		client = pymongo.MongoClient(conn_str)
		try:
			client.server_info()
			print("Connected to MongoDB Cluster")
		except Exception:
			print("Unable to connect to Mongo DB Cluster")

		return client

	def get_db(self,db_name):
		dbnames = self.client_conn.list_database_names()
		if db_name in dbnames:
			return self.client_conn[db_name]
		else:
			print("Database with name",db_name,"does not exsist")
			return False

	def create_db(self,db_name):
		dbnames = self.client_conn.list_database_names()
		if db_name in dbnames:
			db = self.client_conn[db_name]
			print(db_name,"DB already created")
			return db
		else:
			db = self.client_conn[db_name]
		return db

	def create_collection(self,db_name,collection_name):
		db = self.client_conn[db_name]
		print(db.list_collection_names())
		collection = db.create_collection(collection_name)
		print(db.list_collection_names())
		return collection

	#get collection
	def get_collection(self,collection_name):
		try:
			list_of_collections = self.DB.list_collection_names() 
			if collection_name in list_of_collections:
				return self.DB[collection_name]
			else:
				print("Collection with name",collection_name,"does not exsist")
		except Exception:
			print("Error getting collection")

	#insert entry into db		
	def insert_one_entry(self,uid,query,results):
		try:
			today = date.today()
			d1 = today.strftime("%d/%m/%Y")
			entry = {
			"user_id" : uid,
			"query" : query,
			"results":results,
			"_id":str(uuid.uuid4()),
			"date" : d1
			}
			print(entry)
			collect_id = self.collection.insert_one(entry)
		except Exception:
			print("Error creating entry")

	#get entry by user id		
	def get_entry_by_uid(self,uid):
		result = self.collection.find({"user_id": uid})
		return result

	#get all entries in collection
	def get_all_entries(self):
		results = self.collection.find({})
		return results
	def set_database(self,database_name):
		self.DB = self.get_db(database_name)

	def set_collection(self,collection_name):
		self.collection = self.get_collection(collection_name)
		
	#Delete entry in DB given the UID, DB name and collection name  
	def delete_entry(self,entry_id):
		collection = self.collection
		myquery = { "_id" : entry_id }
		print(entry_id)
		r = collection.delete_one(myquery)
		if r.deleted_count == 0:
			print("Entry was not found in order to be deleted")
		else:
			print("Entry deleted succesfully")



