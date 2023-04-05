from pydoc import doc
from pydantic import BaseModel
from typing import List,Dict

class SubjectRequestModel(BaseModel):
    subjectCode : str
    generalTopic : str
    topics : str

class BookmarkRequestModel(BaseModel):
    label : str
    title : str
    authors : str
    sourceAbbrev : str
    sourceLong : str
    volNum : str
    date : str
    startYear : str
    endYear : str
    pages : str
    subjects: List[SubjectRequestModel]
    doi : str

class FolderRequestModel(BaseModel):
    label: str
    bookmarks: List[BookmarkRequestModel]

class UserBookmarksRequestModel(BaseModel):
    uid: str
    bookmarkFolders: List[FolderRequestModel]




class SubjectResponseModel(BaseModel):
    subjectCode : str
    generalTopic : str
    topics : str

class BookmarkResponseModel(BaseModel):
    id: str
    label : str
    title : str
    authors : str
    sourceAbbrev : str
    sourceLong : str
    volNum : str
    date : str
    startYear : str
    endYear : str
    pages : str
    subjects: List[SubjectResponseModel]
    doi : str

class FolderResponseModel(BaseModel):
    id: str
    label: str
    bookmarks: List[BookmarkResponseModel]

class UserBookmarksResponseModel(BaseModel):
    id: str
    uid: str
    bookmarkFolders: List[FolderResponseModel]