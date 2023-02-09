from typing import Dict
def serializeDict(a) ->dict:
    return{**{i:str(a[i])for i in a if i=="_id"},**{i:a[i] for i in a if i!="_id"}}

def serializeList(entity) ->list:
    return [serializeDict(a) for a in entity]
    
def historyEntity(item) -> Dict:
    return {
        "id" : str(item["_id"]),
        "uid" : str(item["uid"]),
        "query" : item["query"],
        "results" : item["results"]
    }

def historysEntity(entity) -> list:
    return[historyEntity(item) for item in entity]
