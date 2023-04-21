from typing import Dict

def PersonEntity(item) -> Dict:
    return {
        "firstName": str(item["firstName"]),
        "lastName": str(item["lastName"]),
        "middleInitial": str(item["middleInitial"])
    }

def flatten_persons(entity) -> list:
    return[PersonEntity(item) for item in entity]

def CitationEntity(item) -> Dict:
    if "author" in item:
        author = PersonEntity(item["author"])
    else:
        author = {"firstName": "", "lastName": "", "middleInitial": ""}

    if "contributors" in item:
        contributers = flatten_persons(item["contributors"])
    else:
        contributers = []

    if "editors" in item:
        editors = flatten_persons(item["editors"])
    else:
        editors = []
    
    if "translators" in item:
        translators = flatten_persons(item["translators"])
    else:
        translators = []
    
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
        "contributors": contributers,
        "version": str(item["version"]),
        "number": str(item["number"]),
        "pages": str(item["pages"]),
        "publicationlocale": str(item["publicationlocale"]),
        "format": str(item["format"]),
        "accessDate": str(item["accessDate"]),
        "publisher": str(item["publisher"]),
        "type": str(item["type"]),
        "chapter": str(item["chapter"]),
        "editors": editors,
        "translators": translators,
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