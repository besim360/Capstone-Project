import json
from fastapi import APIRouter, HTTPException, Depends, status
from bson.objectid import ObjectId
from models.bibliography import Bibliography, Citation
from config.servers import mongo_conn
from schemas.user_bibliographies import UserBibliographies as UserBibCreate
from pydantic import Json
import pydantic
from routes.auth import get_auth

bibliographies = APIRouter()

pydantic.json.ENCODERS_BY_TYPE[ObjectId]=str

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
    Gets the base bibliographies records for a user
"""
def retrieve_bibliography_record(uid):
    cur = list(mongo_conn.TechCommSearch.Bibliographies.find({"uid":uid}))
    if len(cur) > 0:
        user_bibliography_record = cur[0]

    else:
        user_bibliography_record = {}
    return user_bibliography_record

"""
    Gets a list of bibliographies for a user
"""
@bibliographies.get('/bibliographies/{uid}')
async def get_user_bibliographies(uid, identity: Json = Depends(get_auth)):
    check_auth(uid, identity)
    
    user_bibliography_record = retrieve_bibliography_record(uid)
    return user_bibliography_record

"""
    Creates a new base bibliographies object with no citation records for a user
"""
@bibliographies.post('/bibliographies/base/{uid}')
async def create_base_bibliography_record(uid):
    # , identity: Json = Depends(get_auth) check_auth(uid, identity)
    user_bibliography_record = retrieve_bibliography_record(uid)

    if not bool(user_bibliography_record):
        t = {"uid": uid, "bibliographies": []}
        mongo_conn.TechCommSearch.Bibliographies.insert_one(t)
    
    new_record = retrieve_bibliography_record(uid)

    return UserBibCreate(new_record)

"""
    Create a bibliography
"""
@bibliographies.post('/bibliographies/bibliography/{uid}')
async def create_bibliography(uid, bibliography: Bibliography, identity: Json = Depends(get_auth)):
    check_auth(uid, identity)
    t = bibliography.dict()
    t["_id"] = ObjectId()
    user_bibliography_record = retrieve_bibliography_record(uid)
    if not bool(user_bibliography_record):
        raise HTTPException(
            status_code=status.HTTP_400_BAD_REQUEST,
            detail='User does not have associated bibliographies object',
            headers={"WWW-Authenticate": "Bearer"}
        )
    mongo_conn.TechCommSearch.Bibliographies.update_one(
        { "_id": ObjectId(user_bibliography_record['_id']) },
        { '$push': {'bibliographies': t}}
    )

    new_record = retrieve_bibliography_record(uid)
    
    return UserBibCreate(new_record)

"""
    Create a citation in a bibliography
"""
@bibliographies.post('/bibliographies/bibliography/citation/{bid}/{uid}')
async def create_new_citation(uid, bid, citation: Citation, identity: Json = Depends(get_auth)):
    check_auth(uid, identity)
    t = citation.dict()
    t["_id"] = ObjectId()
    user_bibliography_record = retrieve_bibliography_record(uid)
    if not bool(user_bibliography_record):
        raise HTTPException(
            status_code=status.HTTP_400_BAD_REQUEST,
            detail='User does not have associated bibliographies object',
            headers={"WWW-Authenticate": "Bearer"}
        )

    bibliographyExists = len(list(filter(lambda x: x["_id"] == ObjectId(bid), user_bibliography_record['bibliographies']))) > 0
    if bibliographyExists:
        mongo_conn.TechCommSearch.Bibliographies.update_one(
            { "_id": ObjectId(user_bibliography_record['_id']), "bibliographies._id": ObjectId(bid) },
            { '$push': {'bibliographies.$.citations': t}}
        )
    else:
        raise HTTPException(
            status_code=status.HTTP_400_BAD_REQUEST,
            detail='Bibliography does not exist',
            headers={"WWW-Authenticate": "Bearer"}
        )
    
    new_record = retrieve_bibliography_record(uid)
    
    return UserBibCreate(new_record)

"""
    Update a bookmark in a users folder
"""
@bibliographies.put('/bibliographies/bibliography/citation/{cid}/{bid}/{uid}')
async def update_citation(uid, bid, cid, citation: Citation, identity: Json = Depends(get_auth)):
    check_auth(uid, identity)
    t = citation.dict()
    user_bibliography_record = retrieve_bibliography_record(uid)
    if not bool(user_bibliography_record):
        raise HTTPException(
            status_code=status.HTTP_400_BAD_REQUEST,
            detail='User does not have associated bibliographies object',
            headers={"WWW-Authenticate": "Bearer"}
        )
    bibliography = list(filter(lambda x: x["_id"] == ObjectId(bid), user_bibliography_record['bibliographies']))
    bibliographyExists = len(bibliography) > 0
    if not bibliographyExists:
        raise HTTPException(
            status_code=status.HTTP_400_BAD_REQUEST,
            detail='Bibliography does not exist',
            headers={"WWW-Authenticate": "Bearer"}
        )
    
    citationExists = len(list(filter(lambda x: x["_id"] == ObjectId(cid), bibliography[0]["citations"])))
    if not citationExists:
            raise HTTPException(
                status_code=status.HTTP_400_BAD_REQUEST,
                detail='Failed to update citation, it does not exist',
                headers={"WWW-Authenticate": "Bearer"}
            )
    mongo_conn.TechCommSearch.Bibliographies.update_one(
        { "_id": ObjectId(user_bibliography_record['_id'])},
        { '$set': {
            'bibliographies.$[bibliography].citations.$[citation].title': t['title'],
            'bibliographies.$[bibliography].citations.$[citation].authors': t['authors'],
            'bibliographies.$[bibliography].citations.$[citation].volNum': t['volNum'],
            'bibliographies.$[bibliography].citations.$[citation].date': t['date'],
            'bibliographies.$[bibliography].citations.$[citation].startYear': t['startYear'],
            'bibliographies.$[bibliography].citations.$[citation].endYear': t['endYear'],
            'bibliographies.$[bibliography].citations.$[citation].pages': t['pages'],
            'bibliographies.$[bibliography].citations.$[citation].doi': t['doi'],
            'bibliographies.$[bibliography].citations.$[citation].author': t['author'],
            'bibliographies.$[bibliography].citations.$[citation].contributors': t['contributors'],
            'bibliographies.$[bibliography].citations.$[citation].version': t['version'],
            'bibliographies.$[bibliography].citations.$[citation].number': t['number'],
            'bibliographies.$[bibliography].citations.$[citation].publicationlocale': t['publicationlocale'],
            'bibliographies.$[bibliography].citations.$[citation].format': t['format'],
            'bibliographies.$[bibliography].citations.$[citation].accessDate': t['accessDate'],
            'bibliographies.$[bibliography].citations.$[citation].publisher': t['publisher'],
            'bibliographies.$[bibliography].citations.$[citation].type': t['type'],
            'bibliographies.$[bibliography].citations.$[citation].chapter': t['chapter'],
            'bibliographies.$[bibliography].citations.$[citation].editors': t['editors'],
            'bibliographies.$[bibliography].citations.$[citation].translators': t['translators'],
            'bibliographies.$[bibliography].citations.$[citation].fullString': t['fullString'],
        }},
        array_filters=[
            {"bibliography._id": ObjectId(bid)},
            {"citation._id": ObjectId(cid)}
        ]
    )
    
    new_record = retrieve_bibliography_record(uid)
    
    return UserBibCreate(new_record)

"""
    Delete a citation in a bibliography
"""
@bibliographies.delete('/bibliographies/bibliography/{cid}/{bid}/{uid}')
async def delete_citation(cid, bid, uid, identity: Json = Depends(get_auth)):
    check_auth(uid, identity)
    user_bibliography_record = retrieve_bibliography_record(uid)
    if not bool(user_bibliography_record):
        raise HTTPException(
            status_code=status.HTTP_400_BAD_REQUEST,
            detail='User does not have associated bibliographies object',
            headers={"WWW-Authenticate": "Bearer"}
        )
    bibliography = list(filter(lambda x: x["_id"] == ObjectId(bid), user_bibliography_record['bibliographies']))
    bibliographyExists = len(bibliography) > 0
    if not bibliographyExists:
        raise HTTPException(
            status_code=status.HTTP_400_BAD_REQUEST,
            detail='Bibliography does not exist',
            headers={"WWW-Authenticate": "Bearer"}
        )
    
    citationExists = len(list(filter(lambda x: x["_id"] == ObjectId(cid), bibliography[0]["citations"])))
    if not citationExists:
        raise HTTPException(
            status_code=status.HTTP_400_BAD_REQUEST,
            detail='Failed to update citation, it does not exist',
            headers={"WWW-Authenticate": "Bearer"}
        )


    mongo_conn.TechCommSearch.Bibliographies.update_one(
        { "_id": ObjectId(user_bibliography_record['_id'])},
        { '$pull': {
            'bibliographies.$[bibliography].citations': {"_id": ObjectId(cid)},
        }},
        array_filters=[
            {"bibliography._id": ObjectId(bid)}
        ]
    )

    new_record = retrieve_bibliography_record(uid)
    
    return UserBibCreate(new_record)

"""
    Delete a bibliography
"""
@bibliographies.delete('/bibliographies/bibliography/{bid}/{uid}')
async def delete_bibliography(bid, uid, identity: Json = Depends(get_auth)):
    check_auth(uid, identity)
    user_bibliography_record = retrieve_bibliography_record(uid)
    if not bool(user_bibliography_record):
        raise HTTPException(
            status_code=status.HTTP_400_BAD_REQUEST,
            detail='User does not have associated bibliographies object',
            headers={"WWW-Authenticate": "Bearer"}
        )
    bibliography = list(filter(lambda x: x["_id"] == ObjectId(bid), user_bibliography_record['bibliographies']))
    bibliographyExists = len(bibliography) > 0
    if not bibliographyExists:
        raise HTTPException(
            status_code=status.HTTP_400_BAD_REQUEST,
            detail='Bibliography does not exist',
            headers={"WWW-Authenticate": "Bearer"}
        )
    
    mongo_conn.TechCommSearch.Bibliographies.update_one(
        { "_id": ObjectId(user_bibliography_record['_id'])},
        { '$pull': {
            'bibliographies': {"_id": ObjectId(bid)},
        }},
    )

    new_record = retrieve_bibliography_record(uid)
    
    return UserBibCreate(new_record)