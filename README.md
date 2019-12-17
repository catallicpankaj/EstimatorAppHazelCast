# Pointing Poker app sessionless.

Simulation of a web site devoted to making online planning poker sessions easier for distributed teams.
This adds an additional feature where only Intiator can get the Show Votes button, can be distinguished by api reponse of Get Users.

### Create Session for User: 

- POST-http://localhost:8080/cachedata/create-session?initiatorName=Pankaj

- Api-Response
```sh
{
    "sessionInitiatorData": {
        "generatedUniqueKey": "9fcf34fc-002c-4853-850a-46a43a7cf9bb",
        "initiatedBy": "Pankaj"
    }
}
```

### New User added to session
- PUT - https://localhost:8080/cachedata/update-session
- Params
```sh
sessionKey:9fcf34fc-002c-4853-850a-46a43a7cf9bb
newUserName:NewUser
```

### Get All users in a session
- GET - localhost:8080/cachedata/read
- Params
```sh
key:9fcf34fc-002c-4853-850a-46a43a7cf9bb
cacheName:initiatorCache
```
- Api Response
```sh
[
    {
        "d716406b-3b53-4435-a141-f51ddbcfe495": {
            "showVotes": "",
            "isInitiator": "true",
            "votedAs": "",
            "userName": "Pankaj"
        }
    },
    {
        "5820c4e0-db8f-4a7a-9833-7c0b227466f2": {
            "showVotes": "",
            "votedAs": "L",
            "userName": "NewUser"
        }
    }
]
```

### Update votes in session
- PUT - localhost:8080/cachedata/update-vote?sessionKey=ea9a3b16-9cf5-4a27-ba9e-9c6df1aecf1c&uuid=768bd2f8-cc4c-42a7-adcc-6a36bec74b3d&votedValue=L
- Params
```sh
sessionKey:9fcf34fc-002c-4853-850a-46a43a7cf9bb
uuid:5820c4e0-db8f-4a7a-9833-7c0b227466f2
votedValue:L
```
