from fastapi import FastAPI
import mongoDemo
from pydantic import BaseModel
import json

app = FastAPI()
global mongoDB
#fill in username and password 
mongoDB = mongoDemo.db_cluster("mongodb+srv://<username>:<password>@cluster0.8c102fm.mongodb.net/test","capstone","user_history")

class Item(BaseModel):
    doc_id: str

#get all history for user
@app.get("/get_user_history/{user_id}")
def get_players(user_id:str):
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
@app.post("/add/")
async def create_item(item: Item):
    mongoDB.insert_one_entry(item.doc_id)
    return item
#delete history entry
@app.delete("/delete/{user_id}")
async def delete_user_history(user_id:str):
    mongoDB.delete_entry(user_id)
