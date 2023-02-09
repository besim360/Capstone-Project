
from pydoc import doc
from pydantic import BaseModel
from typing import List,Dict

class Document(BaseModel):
    id : str
    title : str
    authors : str
    sourceAbbrev : str
    sourceLong : str
    volNum : str
    date : str
    startYear : str
    endYear : str
    pages : str
    subjectCodes : str
    topics : str
    doi : str

class History(BaseModel):
    uid : str
    query : str
    results : List[Document]

