from typing import Dict

def PersonEntity(item) -> Dict:
    return {
        "firstName": item["firstName"],
        "lastName": item["lastName"],
        "middleInitial": item["middleInitial"]
    }

def flatten_persons(entity) -> list:
    return[PersonEntity(item) for item in entity]

def CitationEntity(item) -> Dict:
    if "author" in item:
        author = PersonEntity(item["author"])
    else:
        author = {"firstName": "", "lastName": "", "middleInitial": ""}
    
    return {
        "id": str(item["_id"]),
        "title": str(item["title"]),
        "authors": str(item["authors"]),
        "volNum": str(item["volNum"]),
        "date": str(item["date"]),
        "startYear": str(item["startYear"]),
        "endYear": str(item["endYear"]),
        "pages": str(item["pages"]),
        "doi": str(item["doi"]),
        "author": author,
        "contributors": str(item['contributors']),
        "version": str(item["version"]),
        "number": str(item["number"]),
        "pages": str(item["pages"]),
        "publicationlocale": str(item["publicationlocale"]),
        "format": str(item["format"]),
        "accessDate": str(item["accessDate"]),
        "publisher": str(item["publisher"]),
        "type": str(item["type"]),
        "chapter": str(item["chapter"]),
        "editors": str(item['editors']),
        "translators": str(item['translators'])
    }

def flatten_citations(entity) -> list:
    return[CitationEntity(item) for item in entity]

def BibliographyEntity(item) -> Dict:
    if "citations" in item:
        citations = flatten_citations(item["citations"])
    else:
        citations = []
    return {
        "id": str(item["_id"]),
        "label": str(item["label"]),
        "citations": citations,
    }

def flatten_bibliographies(entity) -> list:
    return [BibliographyEntity(item) for item in entity]

def UserBibliographies(item) -> Dict:

    iCpy = item

    hasElement = len(list(iCpy)) > 0
    if hasElement == True:
        if "bibliographies" in item:
            bibliographies = flatten_bibliographies(item["bibliographies"])
        else:
            bibliographies = []
        return {
            "id" : str(item["_id"]),
            "uid": str(item["uid"]),
            "bibliographies": bibliographies
        }
    else:
        return {}