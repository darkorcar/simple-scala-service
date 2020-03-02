# Simple scala service

## Build

`sbt clean compile`

## Run server
`sbt server/run`

## Swagger docs

http://localhost:8080/docs/

## API summary

Method | Path | Description 
--- | --- | --- 
GET | http://localhost:8080/_info/version | BuildInfo and Version data
GET | http://localhost:8080/api/v1/greetings?page-number=0&page-size=15 | List greetings (paging is optional) 
POST| http://localhost:8080/api/v1/greetings | Create Greeting. Body: `{"greet": "Good morning!"}`
