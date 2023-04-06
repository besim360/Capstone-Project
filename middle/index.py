from fastapi import FastAPI
from routes.history import history
from routes.keycloak import keycloak
from routes.bookmark import bookmarks
from routes.bibliography import bibliographies
from fastapi.middleware.cors import CORSMiddleware

app = FastAPI()

origins = ["*"]

app.add_middleware(
    CORSMiddleware,
    allow_origins=origins,
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)


app.include_router(bibliographies)
app.include_router(bookmarks)
app.include_router(history)
app.include_router(keycloak)
