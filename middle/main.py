from fastapi import FastAPI
import mongoDemo
from pydantic import BaseModel
from typing import List, Optional
import json

#Start server with uvicorn main:app --reload

#Use apis at http://127.0.0.1:8000/docs -- Has a really nice UI to test API calls

app = FastAPI()
global mongoDB
#fill in username and password 
mongoDB = mongoDemo.db_cluster("mongodb+srv://admin:admin@cluster0.8c102fm.mongodb.net/test","capstone","user_history")

class user_search(BaseModel):   #User ID comes from keycloak
    search_uid:str                     #ID for user_history entry
    query:str                   #Query string
    results:List[str]           #List of documents returned 

class user_bookmark(BaseModel):
    uid:str
    


#get all history for user
@app.get("/history/{user_id}")
def get_user_history(user_id:str):
    results = list()
    q = mongoDB.get_entry_by_uid(user_id)
    if type(q) is not dict:
        for index,r in enumerate(q):
            results.append(r)
        results_json = json.dumps(results)
    else:
        results_json = json.dumps(q)
    return results_json

#add history entry
@app.post("/history")
async def create_item(user_search: user_search):
    mongoDB.insert_one_entry(user_search.search_uid,user_search.query,user_search.results)
    return user_search

#delete history entry
@app.delete("/history/{user_id}")
async def delete_user_history(user_id:str):
    mongoDB.delete_entry(user_id)
