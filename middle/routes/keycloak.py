from fastapi import APIRouter, Request
import httpx
import json
import uvicorn
import jwt

keycloak = APIRouter()
client="wsutcsc"
realm="wsutcsr"

#Authenticate the user with username and password, and receive their bearer token.
@keycloak.post("/authenticate-user/")
def authenticateUser (email : str, password : str):
    request = httpx.post(f"http://localhost:8080/realms/{realm}/protocol/openid-connect/token", 
    data={'username' : email, 
        'password' : password, 
        'grant_type' : 'password', 
        'client_id' : client})
    data = json.loads(request.text)
    bearer_token = data["access_token"]
    public_key = "-----BEGIN PUBLIC KEY-----\nMIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAxSR112675T/+eZBYIi5q/OCKfhMGTmA9VjMEjDHCaG/eKxmCLQUQXNtS3yNvCBQG/L55eIS4GwmYPYgj0FW3eQeu6A+KAJuSsLvQTRlEKgdgOqRl7j7c29yuShr1zYEQVfUr4y4XLbAU2WQ+y5EQU3VmNO9ebgFWBEjwWQtdLgSlgaQg4JdlaWvKQIFxzd9mJcC4YmYnUir+q46lh8RUDUglqwoTQmSTnkZ3nIn/N+SQM8L5l9d4/o9FMo+FDXVXjzoDAqMTDWPrCy02sTZKiLslCxkW1enPB+BBDJ4o+XCcbaOD5Twd/ON7YZ3IpQRsiLS8CcYSEZCE8LvUrH1hRQIDAQAB\n-----END PUBLIC KEY-----"
    decoded_bearer_token = jwt.decode(bearer_token, public_key, audience="account",algorithms="RS256")
    
    #uncomment below for extracted user ID 
    #decoded_bearer_token["sub"]
    
    return data

#Retrieve all current users.
@keycloak.get("/retrieve-all-users/")
def getCurrentUsers(request: Request):
    token = request.headers.get('Authorization')
    request = httpx.get("http://localhost:8080/admin/realms/cs420/users", 
    headers= {'Authorization': 'Bearer ' + token})
    text = json.loads(request.text)
    return request.status_code, text


#Retrieve specified user based on their email, and return their Keycloak ID.
@keycloak.get("/retrieve-user/")
def retrieveUser(email : str, request: Request):
    token = request.headers.get('Authorization')
    request = httpx.get("http://localhost:8080/admin/realms/cs420/users",
    headers={'Authorization': 'Bearer ' + token},
    params={'email' : email})
    text = json.loads(request.text)
    return request.status_code, text


#Set the password for specified user (forgot password functionality).
@keycloak.put("/set-password/")
def setUserPassword(email : str, newPassword : str, request : Request):
    token = request.headers.get('Authorization')
    userObject = retrieveUser(email, token)
    userid = userObject[1][0]["id"]
    request = httpx.put("http://localhost:8080/admin/realms/cs420/users/"+userid+"/reset-password",
    headers={'Authorization': 'Bearer ' + token},
    json={"value":newPassword})
    return request.status_code


#Create a user with specified paramaters.
@keycloak.post("/create-user/")
def createUser(email : str, firstName : str, lastName : str, password : str):
    token = request.headers.get('Authorization')
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


#Delete specified user account.
@keycloak.delete("/delete-user/")
def deleteUser(email : str, request : Request):
    token = request.headers.get('Authorization')
    userObject = retrieveUser(email, token)
    userid = userObject[1][0]["id"]
    request = httpx.delete("http://localhost:8080/admin/realms/cs420/users/"+userid,
    headers={'Authorization': 'Bearer ' + token})
    return request.status_code
    

#Function to promote a regular user to Admin user.
@keycloak.post("/promote-user/")
def promoteUser(email : str, request : Request):
    token = request.headers.get('Authorization')
    userObject = retrieveUser(email, token)
    userid = userObject[1][0]["id"]
    request = httpx.put("http://localhost:8080/admin/realms/cs420/users/"+userid+"/groups/1261360f-6256-4ca2-bbc2-a1f07ceef150",
    headers={'Authorization': 'Bearer ' + token})
    return request.status_code


#Function to demote an Admin user to regular user.
@keycloak.post("/demote-user/")
def demoteUser(email : str, request : Request):
    token = request.headers.get('Authorization')
    userObject = retrieveUser(email, token)
    userid = userObject[1][0]["id"]
    request = httpx.delete("http://localhost:8080/admin/realms/cs420/users/"+userid+"/groups/1261360f-6256-4ca2-bbc2-a1f07ceef150",
    headers={'Authorization': 'Bearer ' + token})
    return request.status_code


#Change status of the user (enabled/disabled)
@keycloak.post("/change-user-status/")
def changeStatus(email : str, request : Request, status : bool):
    token = request.headers.get('Authorization')
    userObject = retrieveUser(email, token)
    userid = userObject[1][0]["id"]
    request = httpx.put("http://localhost:8080/admin/realms/cs420/users/"+userid,
    headers={'Authorization': 'Bearer ' + token}, json={"enabled":status})
    return request.status_code
