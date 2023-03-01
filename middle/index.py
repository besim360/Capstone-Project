from fastapi import FastAPI
from routes.history import history
from routes.keycloak import keycloak

app = FastAPI()

app.include_router(history)
app.include_router(keycloak)
