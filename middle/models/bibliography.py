from pydoc import doc
from pydantic import BaseModel
from typing import List,Dict

class Person(BaseModel):
    firstName: str
    lastName: str
    middleInitial: str

class Citation(BaseModel):
    title: str
    authors: str
    volNum: str
    date: str
    startYear: str
    endYear: str
    pages: str
    doi: str
    author: Person
    contributors: List[Person]
    version: str
    number: str
    publicationlocale: str
    format: str
    accessDate: str
    publisher: str
    type: str
    chapter: str
    editors: List[Person]
    translators: List[Person]


class Bibliography(BaseModel):
    label: str
    citations: List[Citation]

class UserBibliographies(BaseModel):
    uid: str
    bibliographies: List[Bibliography]