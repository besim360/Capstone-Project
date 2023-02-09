from fastapi import APIRouter
from models.history import History
from config.servers import mongo_conn
from schemas.user_history import historysEntity
history = APIRouter()

#Used for testing, wont be needed in actual implementation
@history.get('/history/')
async def get_user_history():
    return historysEntity(mongo_conn.Capstone.History.find())

#Get all history with user's UID
@history.get('/history/{uid}')
async def get_user_history(uid):
    return historysEntity(mongo_conn.Capstone.History.find({"uid":uid}))

#Add user search history to DB
@history.post('/history/')
async def creat_history(history: History):
    t = history.dict()
    mongo_conn.Capstone.History.insert_one(t)