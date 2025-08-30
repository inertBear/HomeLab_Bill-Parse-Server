# quarkus-rest-tester

This was built as an example of quarkus-rest implementation. It is intended to be used as an integration into Home Assistant.

- "dev mode" command:
    - mvn quarkus:dev
- helpful links in "dev mode"
    - http://localhost:8080/q/dev-ui/
    - http://localhost:8080/q/swagger-ui

- docker build command:
    - docker buildx build -t quarkus-rest-tester -f src/main/docker/Dockerfile.native .
