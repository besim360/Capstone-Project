import mongoUtil
#Run this when you have your MongoDB up and running on local host port 2717. Will initilize with the proper Database name and Collections
#Database Name: "Capstone" ~ for now idk what to call it

#Collection Names:
#       - "User History"
#       - "User Bookmarks"

DB = mongoUtil.db_cluster("mongodb://localhost:2717")
DB.create_db("TechCommSearch")
DB.create_collection("TechCommSearch","History")
DB.set_database("TechCommSearch")
DB.set_collection("History")
DB.create_collection("TechCommSearch","Bookmarks")
DB.set_collection("Bookmarks")
DB.create_collection("TechCommSearch","Bibliographies")
DB.set_collection("Bibliographies")
print("Database Initialized")
