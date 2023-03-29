from fastapi import FastAPI
import httpx
import json
import uvicorn

app = FastAPI()
realm = "wsutcsr"
#Authenticate the user with username and password, and receive their bearer token.
@app.post("/authenticate-user")
def authenticateUser (email : str, password : str):
    request = httpx.post(f"http://localhost:8080/realms/{realm}/protocol/openid-connect/token", 
    data={'username' : email, 
        'password' : password, 
        'grant_type' : 'password', 
        'client_id' : 'rest-client'})
    data = json.loads(request.text)
    return data


#Retrieve all current users.
@app.get("/retrieve-all-users")
def getCurrentUsers(token : str):
    request = httpx.get(f"http://localhost:8080/admin/realms/{realm}/users", 
    headers= {'Authorization': 'Bearer ' + token})
    text = json.loads(request.text)
    return request.status_code, text


#Retrieve specified user based on their email, and return their Keycloak ID.
@app.get("/retrieve-user")
def retrieveUser(email : str, token : str):
    request = httpx.get(f"http://localhost:8080/admin/realms/{realm}/users",
    headers={'Authorization': 'Bearer ' + token},
    params={'email' : email})
    text = json.loads(request.text)
    return request.status_code, text


#Set the password for specified user (forgot password functionality).
@app.put("/set-password")
def setUserPassword(email : str, newPassword : str, token : str):
    userObject = retrieveUser(email, token)
    userid = userObject[1][0]["id"]
    request = httpx.put(f"http://localhost:8080/admin/realms/{realm}/users/{userid}/reset-password",
    headers={'Authorization': 'Bearer ' + token},
    json={"value":newPassword})
    return request.status_code


#Create a user with specified paramaters.
@app.post("/create-user")
def createUser(email : str, firstName : str, lastName : str, password : str, token : str):
    request = httpx.post(f"http://localhost:8080/admin/realms/{realm}/users",
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


#Delete specified user account.
@app.delete("/delete-user")
def deleteUser(email : str, token : str):
    userObject = retrieveUser(email, token)
    userid = userObject[1][0]["id"]
    request = httpx.delete(f"http://localhost:8080/admin/realms/{realm}/users/{userid}",
    headers={'Authorization': 'Bearer ' + token})
    return request.status_code
    

#Function to promote a regular user to Admin user.
@app.post("/promote-user")
def promoteUser(email : str, token : str):
    userObject = retrieveUser(email, token)
    userid = userObject[1][0]["id"]
    request = httpx.put(f"http://localhost:8080/admin/realms/{realm}/users/{userid}/groups/1261360f-6256-4ca2-bbc2-a1f07ceef150",
    headers={'Authorization': 'Bearer ' + token})
    return request.status_code


#Function to demote an Admin user to regular user.
@app.post("/demote-user")
def demoteUser(email : str, token : str):
    userObject = retrieveUser(email, token)
    userid = userObject[1][0]["id"]
    request = httpx.delete(f"http://localhost:8080/admin/realms/{realm}/users/{userid}/groups/1261360f-6256-4ca2-bbc2-a1f07ceef150",
    headers={'Authorization': 'Bearer ' + token})
    return request.status_code


#Change status of the user (enabled/disabled)
@app.post("/change-user-status")
def changeStatus(email : str, token : str, status : bool):
    userObject = retrieveUser(email, token)
    userid = userObject[1][0]["id"]
    request = httpx.put(f"http://localhost:8080/admin/realms/{realm}/users/{userid}",
    headers={'Authorization': 'Bearer ' + token}, json={"enabled":status})
    return request.status_code

if __name__ ==  "__main__":
    uvicorn.run(app, port = 8000, host = "0.0.0.0")
