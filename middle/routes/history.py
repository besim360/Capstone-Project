import json
from fastapi import APIRouter, HTTPException, Depends, status
from bson.objectid import ObjectId
from models.history import History
from config.servers import mongo_conn
from schemas.user_history import historysEntity
from pydantic import Json
from routes.auth import get_auth

history = APIRouter()

#Used for testing, wont be needed in actual implementation
@history.get('/history/')
async def get_user_history():
    return historysEntity(mongo_conn.TechCommSearch.History.find())

#Get all history with user's UID
@history.get('/history/{uid}')
async def get_user_history(uid, identity: Json = Depends(get_auth)):
    if(identity['sub'] != uid):
        raise HTTPException(
            status_code=status.HTTP_401_UNAUTHORIZED,
            detail='user id does not match token',
            headers={"WWW-Authenticate": "Bearer"}
        )
    return historysEntity(mongo_conn.TechCommSearch.History.find({"uid":uid}))

@history.delete('/history/{uid}/{record_id}')
async def delete_history_record(uid, record_id, identity: Json = Depends(get_auth)):
    if(identity['sub'] != uid):
        raise HTTPException(
            status_code=status.HTTP_401_UNAUTHORIZED,
            detail='user id does not match token',
            headers={"WWW-Authenticate": "Bearer"}
        )
    deleteQuery = {
        '_id': ObjectId(record_id)
    }
    mongo_conn.TechCommSearch.History.delete_one(deleteQuery)
    return historysEntity(mongo_conn.TechCommSearch.History.find({"uid":uid}))

#Add user search history to DB
@history.post('/history/')
async def create_history(history: History):
    t = history.dict()
    mongo_conn.TechCommSearch.History.insert_one(t)
