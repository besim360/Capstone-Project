import json
from fastapi import APIRouter, HTTPException, Depends, status
from bson.objectid import ObjectId
from models.bookmark import FolderRequestModel, BookmarkRequestModel, BookmarkResponseModel
from config.servers import mongo_conn
from schemas.user_bookmarks import request_handler
from pydantic import Json
from routes.auth import get_auth

bookmarks = APIRouter()

"""
    Utitily function that checks that the passed in UID matches the keycloak token identity
"""
def check_auth(uid, identity: Json):
    if(identity['sub'] != uid):
        raise HTTPException(
            status_code=status.HTTP_401_UNAUTHORIZED,
            detail='user id does not match token',
            headers={"WWW-Authenticate": "Bearer"}
        )

"""
    Gets a list of bookmarks for a user
"""
@bookmarks.get('/bookmarks/{uid}')
async def get_user_bookmarks(uid, identity: Json = Depends(get_auth)):
    check_auth(uid, identity)
    req = request_handler(mongo_conn.TechCommSearch.Bookmarks.find({"uid":uid}))
    isRecord = len(req) > 0
    if isRecord: return req[0]
    return []

"""
    Creates a new base bookmarks object with no bookmarks for a user
"""
@bookmarks.post('/bookmarks/base/{uid}')
async def create_user_bookmarks(uid, identity: Json = Depends(get_auth)):
    check_auth(uid, identity)
    bm = mongo_conn.TechCommSearch.Bookmarks.find({"uid":uid})
    user_bookmarks = list(bm)
    if len(user_bookmarks) == 0:
        t = {"uid": uid, "bookmarkFolders": []}
        mongo_conn.TechCommSearch.Bookmarks.insert_one(t)
    req = request_handler(mongo_conn.TechCommSearch.Bookmarks.find({"uid":uid}))
    isRecord = len(req) > 0
    if isRecord: return req[0]
    
    return []

"""
    Create a folder in a users bookmark folders
"""
@bookmarks.post('/bookmarks/folder/{uid}')
async def create_new_folder(uid, folder: FolderRequestModel, identity: Json = Depends(get_auth)):
    check_auth(uid, identity)
    t = folder.dict()
    t["_id"] = ObjectId()
    bm = mongo_conn.TechCommSearch.Bookmarks.find({"uid":uid})
    flattened_request = request_handler(bm)
    if len(flattened_request) == 0:
        raise HTTPException(
            status_code=status.HTTP_400_BAD_REQUEST,
            detail='User does not have associated bookmarks object',
            headers={"WWW-Authenticate": "Bearer"}
        )
    mongo_conn.TechCommSearch.Bookmarks.update_one(
        { "_id": ObjectId(flattened_request[0]['id']) },
        { '$push': {'bookmarkFolders': t}}
    )
    return request_handler(mongo_conn.TechCommSearch.Bookmarks.find({"uid":uid}))[0]

"""
    Create a bookmark in a users folder
"""
@bookmarks.post('/bookmarks/bookmark/folder/{fid}/{uid}')
async def create_new_bookmark(uid, fid, bookmark: BookmarkRequestModel, identity: Json = Depends(get_auth)):
    check_auth(uid, identity)
    t = bookmark.dict()
    t["_id"] = ObjectId()
    bm = mongo_conn.TechCommSearch.Bookmarks.find({"uid":uid})
    flattened_request = request_handler(bm)
    if len(flattened_request) == 0:
        raise HTTPException(
            status_code=status.HTTP_400_BAD_REQUEST,
            detail='User does not have associated bookmarks object',
            headers={"WWW-Authenticate": "Bearer"}
        )
    folders = flattened_request[0]["bookmarkFolders"]
    folder = list(filter(lambda x: x["id"] == fid, folders))
    folderExists = len(folder) > 0
    if folderExists == True:
        mongo_conn.TechCommSearch.Bookmarks.update_one(
            { "_id": ObjectId(flattened_request[0]['id']), "bookmarkFolders._id": ObjectId(fid) },
            { '$push': {'bookmarkFolders.$.bookmarks': t}}
        )
    else:
        raise HTTPException(
            status_code=status.HTTP_400_BAD_REQUEST,
            detail='Folder does not exist',
            headers={"WWW-Authenticate": "Bearer"}
        )
    return request_handler(mongo_conn.TechCommSearch.Bookmarks.find({"uid":uid}))[0]

"""
    Update a bookmark in a users folder
"""
@bookmarks.put('/bookmarks/bookmark/folder/{fid}/{uid}')
async def update_bookmark(uid, fid, bookmark: BookmarkResponseModel, identity: Json = Depends(get_auth)):
    check_auth(uid, identity)
    t = bookmark.dict()

    bm = mongo_conn.TechCommSearch.Bookmarks.find({"uid": uid})
    flattened_request = request_handler(bm)
    if len(flattened_request) == 0:
        raise HTTPException(
            status_code=status.HTTP_400_BAD_REQUEST,
            detail='User does not have associated bookmarks object',
            headers={"WWW-Authenticate": "Bearer"}
        )
    folders = flattened_request[0]["bookmarkFolders"]
    folder = list(filter(lambda x: x["id"] == fid, folders))
    folderExists = len(folder) > 0
    if not folderExists:
        raise HTTPException(
            status_code=status.HTTP_400_BAD_REQUEST,
            detail='Folder does not exist',
            headers={"WWW-Authenticate": "Bearer"}
        )
    
    bookmark = list(filter(lambda x: x["id"] == bookmark.id, folder[0]['bookmarks']))
    bookmarkExists = len(bookmark)
    if not bookmarkExists:
        raise HTTPException(
            status_code=status.HTTP_400_BAD_REQUEST,
            detail='Failed to update bookmark, it does not exist',
            headers={"WWW-Authenticate": "Bearer"}
        )
    mongo_conn.TechCommSearch.Bookmarks.update_one(
        { "_id": ObjectId(flattened_request[0]['id'])},
        { '$set': {
            'bookmarkFolders.$[folder].bookmarks.$[bookmark].label': t['label'],
            'bookmarkFolders.$[folder].bookmarks.$[bookmark].title': t['title'],
            'bookmarkFolders.$[folder].bookmarks.$[bookmark].authors': t['authors'],
            'bookmarkFolders.$[folder].bookmarks.$[bookmark].sourceAbbrev': t['sourceAbbrev'],
            'bookmarkFolders.$[folder].bookmarks.$[bookmark].sourceLong': t['sourceLong'],
            'bookmarkFolders.$[folder].bookmarks.$[bookmark].volNum': t['volNum'],
            'bookmarkFolders.$[folder].bookmarks.$[bookmark].date': t['date'],
            'bookmarkFolders.$[folder].bookmarks.$[bookmark].startYear': t['startYear'],
            'bookmarkFolders.$[folder].bookmarks.$[bookmark].endYear': t['endYear'],
            'bookmarkFolders.$[folder].bookmarks.$[bookmark].pages': t['pages'],
            'bookmarkFolders.$[folder].bookmarks.$[bookmark].subjects': t['subjects'],
            'bookmarkFolders.$[folder].bookmarks.$[bookmark].doi': t['doi']
        }},
        array_filters=[
            {"folder._id": ObjectId(fid)},
            {"bookmark._id": ObjectId(bookmark[0]['id'])}
        ]
    )
    return request_handler(mongo_conn.TechCommSearch.Bookmarks.find({"uid":uid}))

"""
    Delete a bookmark in a users folder
"""
@bookmarks.delete('/bookmarks/bookmark/{bid}/{fid}/{uid}')
async def delete_bookmark(uid, fid, bid, identity: Json = Depends(get_auth)):
    check_auth(uid, identity)
    bm = mongo_conn.TechCommSearch.Bookmarks.find({"uid": uid})
    flattened_request = request_handler(bm)
    if len(flattened_request) == 0:
        raise HTTPException(
            status_code=status.HTTP_400_BAD_REQUEST,
            detail='User does not have associated bookmarks object',
            headers={"WWW-Authenticate": "Bearer"}
        )
    folders = flattened_request[0]["bookmarkFolders"]
    folder = list(filter(lambda x: x["id"] == fid, folders))
    folderExists = len(folder) > 0
    if not folderExists:
        raise HTTPException(
            status_code=status.HTTP_400_BAD_REQUEST,
            detail='Folder does not exist',
            headers={"WWW-Authenticate": "Bearer"}
        )
    
    bookmark = list(filter(lambda x: x["id"] == bid, folder[0]['bookmarks']))
    
    bookmarkExists = len(bookmark)
    if not bookmarkExists:
        raise HTTPException(
            status_code=status.HTTP_400_BAD_REQUEST,
            detail='Failed to delete bookmark, it does not exist',
            headers={"WWW-Authenticate": "Bearer"}
        )
    mongo_conn.TechCommSearch.Bookmarks.update_one(
        { "_id": ObjectId(flattened_request[0]['id'])},
        { '$pull': {
            'bookmarkFolders.$[folder].bookmarks': {"_id": ObjectId(bid)},
        }},
        array_filters=[
            {"folder._id": ObjectId(fid)}
        ]
    )
    return request_handler(mongo_conn.TechCommSearch.Bookmarks.find({"uid":uid}))

"""
    Delete a folder
"""
@bookmarks.delete('/bookmarks/folder/{fid}/{uid}')
async def delete_folder(uid, fid, identity: Json = Depends(get_auth)):
    check_auth(uid, identity)
    bm = mongo_conn.TechCommSearch.Bookmarks.find({"uid": uid})
    flattened_request = request_handler(bm)
    if len(flattened_request) == 0:
        raise HTTPException(
            status_code=status.HTTP_400_BAD_REQUEST,
            detail='User does not have associated bookmarks object',
            headers={"WWW-Authenticate": "Bearer"}
        )
    folders = flattened_request[0]["bookmarkFolders"]
    folder = list(filter(lambda x: x["id"] == fid, folders))
    folderExists = len(folder) > 0
    if not folderExists:
        raise HTTPException(
            status_code=status.HTTP_400_BAD_REQUEST,
            detail='Folder does not exist',
            headers={"WWW-Authenticate": "Bearer"}
        )
    mongo_conn.TechCommSearch.Bookmarks.update_one(
        { "_id": ObjectId(flattened_request[0]['id'])},
        { '$pull': {
            'bookmarkFolders': {"_id": ObjectId(fid)},
        }},
    )
    return request_handler(mongo_conn.TechCommSearch.Bookmarks.find({"uid":uid}))

