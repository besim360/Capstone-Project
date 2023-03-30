from fastapi import APIRouter
import httpx
import json
keycloak = APIRouter()
realm = "wsutcsr"
client = "wsutcsc"
#Authenticate the user with username and password, and receive their bearer token.
@keycloak.post("/authenticate-user")
def authenticateUser (username : str, password : str):
    request = httpx.post(f"http://localhost:8080/realms/{realm}/protocol/openid-connect/token",
    headers={'Content-Type', 'application/json'},
    data={'username' : username, 
        'password' : password, 
        'grant_type' : 'password', 
        'client_id' : client})
    data = json.loads(request.text)
    return data


#Retrieve all current users.
@keycloak.get("/retrieve-all-users")
def getCurrentUsers(token : str):
    request = httpx.get(f"http://localhost:8080/admin/realms/{realm}/users", 
    headers= {'Authorization': 'Bearer ' + token})
    request = json.loads(request.text)
    return request


#Retrieve specified user based on their email, and return their Keycloak ID.
@keycloak.get("/retrieve-user")
def retrieveUser(email : str, token : str):
    request = httpx.get(f"http://localhost:8080/admin/realms/{realm}/users",
    headers={'Authorization': 'Bearer ' + token},
    params={'email' : email})
    request = json.loads(request.text)
    return request


#Set the password for specified user (forgot password functionality).
@keycloak.put("/set-password")
def setUserPassword(email : str, newPassword : str, token : str):
    userObject = retrieveUser(email, token)
    userid = userObject[0]["id"]
    request = httpx.put(f"http://localhost:8080/admin/realms/{realm}/users/{userid}/reset-password",
    headers={'Authorization': 'Bearer ' + token},
    json={"value":newPassword})
    return request.status_code


#Create a user with specified paramaters.
@keycloak.post("/create-user")
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


# #Delete specified user account.
@keycloak.delete("/delete-user")
def deleteUser(email : str, token : str):
    userObject = retrieveUser(email, token)
    userid = userObject[0]["id"]
    request = httpx.delete(f"http://localhost:8080/admin/realms/{realm}/users/{userid}",
    headers={'Authorization': 'Bearer ' + token})
    return request.status_code
    

#Function to promote a regular user to Admin user - to implement.
@keycloak.post("/promote-user")
def promoteUser(email : str, token : str):
    userid = retrieveUser(email, token)
    request = httpx.post(f"http://localhost:8080/admin/realms/{realm}/users/{userid}//role-mkeycloakings/realm",
    headers={'Authorization': 'Bearer ' + token},
    json={"id": "roleId,",
        "name": "roleName"})
    return request.status_code