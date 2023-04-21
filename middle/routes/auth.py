from fastapi.security import OAuth2AuthorizationCodeBearer
from fastapi import Depends
from keycloak import KeycloakOpenID
from fastapi import Security, HTTPException, status
from pydantic import Json
oauth2_scheme = OAuth2AuthorizationCodeBearer(
    authorizationUrl="http://localhost:8080/",
    tokenUrl="http://localhost:8080/realms/wsutcsr/protocol/openid-connect/token"
)

keycloak_openid = KeycloakOpenID(
    server_url="http://localhost:8080/", # https://sso.example.com/auth/
    client_id="wsutcsc", # backend-client-id
    realm_name="wsutcsr", # example-realm
    # client_secret_key=settings.auth.client_secret, # your backend client secret
    verify=True
)

async def get_idp_public_key():
    return (
        "-----BEGIN PUBLIC KEY-----\n"
        f"{keycloak_openid.public_key()}"
        "\n-----END PUBLIC KEY-----"
    )

async def get_auth(token: str = Security(oauth2_scheme)) -> Json:
    try:
        return keycloak_openid.decode_token(
            token,
            key = await get_idp_public_key(),
            options={
                "verify_signature": True,
                "verify_aud": False,
                "exp": True
            }
        )
    except Exception as e:
        raise HTTPException(
            status_code=status.HTTP_401_UNAUTHORIZED,
            detail=str(e),
            headers={"WWW-Authenticate": "Bearer"}
        )

async def get_current_user(identity: Json = Depends(get_auth)):
   return identity
