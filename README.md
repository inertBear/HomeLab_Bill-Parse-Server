# bill-parser

This microservice parses bills recieved via a rest call. It is intended to be used as an integration into Home Assistant.

- "dev mode" command:
    - mvn quarkus:dev
- helpful links in "dev mode"
    - http://localhost:8080/q/dev-ui/
    - http://localhost:8080/q/swagger-ui

- docker build command:
    - docker buildx build -t bill-parser -f src/main/docker/Dockerfile.native .
