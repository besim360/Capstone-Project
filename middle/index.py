from fastapi import FastAPI, Depends
from routes.history import history
from routes.keycloak import keycloak
from fastapi.middleware.cors import CORSMiddleware
from routes.auth import get_auth

app = FastAPI()

origins = ["*"]

app.add_middleware(
    CORSMiddleware,
    allow_origins=origins,
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

app.include_router(history, dependencies=[Depends(get_auth)])
app.include_router(keycloak)
