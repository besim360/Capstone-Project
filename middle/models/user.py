from pydoc import doc
from pydantic import BaseModel
from typing import List

class User(BaseModel):
    realm : str
    client : str
    username : str
    firstname : str
    lastname : str
    email : str
    emailVerified : bool
    roles : List[str]
    auth_id : str