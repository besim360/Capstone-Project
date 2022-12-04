from fastapi import FastAPI
import httpx
import json

app = FastAPI()

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
