
from pydoc import doc
from pydantic import BaseModel
from typing import List,Dict

class Subject(BaseModel):
    subjectCode: str
    generalTopic: str
    topics: str

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
    subjects: List[Subject]
    doi : str

class History(BaseModel):
    uid : str
    query : str
    queryDate : str
    results : List[Document]

