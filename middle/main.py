from fastapi import FastAPI
import mongoDemo
from pydantic import BaseModel
from typing import List, Optional
import json
import httpx

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

#Authenticate the user with username and password, and receive their bearer token.
@app.post("/authenticate-user")
def authenticateUser (username : str, password : str):
    request = httpx.post("http://localhost:8080/realms/cs420/protocol/openid-connect/token", 
    data={'username' : username, 
        'password' : password, 
        'grant_type' : 'password', 
        'client_id' : 'rest-client'})
    data = json.loads(request.text)
    return data


#Retrieve all current users.
@app.get("/retrieve-all-users")
def getCurrentUsers(token : str):
    request = httpx.get("http://localhost:8080/admin/realms/cs420/users", 
    headers= {'Authorization': 'Bearer ' + token})
    request = json.loads(request.text)
    return request


#Retrieve specified user based on their email, and return their Keycloak ID.
@app.get("/retrieve-user")
def retrieveUser(email : str, token : str):
    request = httpx.get("http://localhost:8080/admin/realms/cs420/users",
    headers={'Authorization': 'Bearer ' + token},
    params={'email' : email})
    request = json.loads(request.text)
    return request


#Set the password for specified user (forgot password functionality).
@app.put("/set-password")
def setUserPassword(email : str, newPassword : str, token : str):
    userObject = retrieveUser(email, token)
    userid = userObject[0]["id"]
    request = httpx.put("http://localhost:8080/admin/realms/cs420/users/"+userid+"/reset-password",
    headers={'Authorization': 'Bearer ' + token},
    json={"value":newPassword})
    return request.status_code


#Create a user with specified paramaters.
@app.post("/create-user")
def createUser(email : str, firstName : str, lastName : str, password : str, token : str):
    request = httpx.post("http://localhost:8080/admin/realms/cs420/users",
    headers={'Authorization': 'Bearer ' + token},
    json={"email": email,
        "emailVerified": False,
        "enabled" : True,
        "firstName" : firstName,
        "lastName" : lastName,
        "credentials": [{
            "type": "password",
            "value": password
    }]})
    
    return request.status_code


# #Delete specified user account.
@app.delete("/delete-user")
def deleteUser(email : str, token : str):
    userObject = retrieveUser(email, token)
    userid = userObject[0]["id"]
    request = httpx.delete("http://localhost:8080/admin/realms/cs420/users/"+userid,
    headers={'Authorization': 'Bearer ' + token})
    return request.status_code
    

#Function to promote a regular user to Admin user - to implement.
@app.post("/promote-user")
def promoteUser(email : str, token : str):
    userID = retrieveUser(email, token)
    request = httpx.post("http://localhost:8080/admin/realms/cs420/users/"+userID+"/role-mappings/realm",
    headers={'Authorization': 'Bearer ' + token},
    json={"id": "roleId,",
        "name": "roleName"})
    return request.status_code
