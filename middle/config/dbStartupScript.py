import mongoUtil
#Run this when you have your MongoDB up and running on local host port 2717. Will initilize with the proper Database name and Collections
#Database Name: "Capstone" ~ for now idk what to call it

#Collection Names:
#       - "User History"
#       - "User Bookmarks"

DB = mongoUtil.db_cluster("mongodb://localhost:2717")
DB.create_db("Capstone")
DB.create_collection("Capstone","History")
DB.set_database("Capstone")
DB.set_collection("History")
DB.insert_one_entry(11,"intial config",None)
DB.create_collection("Capstone","Bookmarks")
DB.set_collection("Bookmarks")
DB.insert_one_entry(11,"intial config",None)
print("Database Initialized")
