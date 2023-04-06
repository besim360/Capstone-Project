from typing import Dict

def userBookmarksEntity(item) -> Dict:
    foldersPresent = "bookmarkFolders" in item
    if foldersPresent == True: 
        folders = flatten_folders(item["bookmarkFolders"])
    else:
        folders = []
    return {
        "id": str(item["_id"]),
        "uid": str(item["uid"]),
        "bookmarkFolders": folders
    }

def folderEntity(item) -> Dict:
    bookmarksPresent = "bookmarks" in item
    if bookmarksPresent == True:
        bookmarks = flatten_bookmarks(item["bookmarks"])
    else:
        bookmarks = []
    return {
        "id" : str(item["_id"]),
        "label" : str(item["label"]),
        "bookmarks" : bookmarks
    }

def bookmarkEntity(item) -> Dict:
    subjectsPresent = "subjects" in item
    if subjectsPresent: 
        subjects = flatten_subjects(item["subjects"])
    else:
        subjects = []
    print(item)
    return {
        "id" : str(item["_id"]),
        "label": str(item["label"]),
        "title": str(item["title"]),
        "authors": str(item["authors"]),
        "sourceAbbrev": str(item["sourceAbbrev"]),
        "sourceLong": str(item["sourceLong"]),
        "volNum": str(item["volNum"]),
        "date": str(item["date"]),
        "startYear": str(item["startYear"]),
        "endYear": str(item["endYear"]),
        "pages": str(item["pages"]),
        "subjects": subjects,
    }

def request_handler(entity) -> list:
    # -- Have to do this since the entity is a cursor it will be consumed on first call so copy it to a list first then do stuff.
    entity = list(entity)
    isEntity = len(entity) > 0
    if isEntity == True:
        return[userBookmarksEntity(item) for item in entity]
    else:
        return []

def subjectEntity(item) -> Dict:
    return {
        "subjectCode": str(item["subjectCode"]),
        "generalTopic": str(item["generalTopic"]),
        "topics": str(item["generalTopic"]),
    }

def flatten_folders(entity) -> list:
    return[folderEntity(item) for item in entity]

def flatten_bookmarks(entity) -> list:
    return[bookmarkEntity(item) for item in entity]

def flatten_subjects(entity) -> list:
    return[subjectEntity(item) for item in entity]
