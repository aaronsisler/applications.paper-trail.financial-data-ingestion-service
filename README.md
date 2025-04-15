# applications.paper-trail.financial-data-ingestion-service

## TODOs

- Have a way to check what the status of the Sonar run was to block the PR

## Definition of Done

<details>
  <summary>This is what is needed to close out a feature branch and created/merge a PR.</summary>

- Contract created/updated
- Dependencies added to pom(s) are commented with what their usage is
- Layers are created/updated and follows naming conventions:
    - Controller
    - Service
    - Repository
    - DAO
    - DTO
- Features and tests are added/updated
- API collection (Bruno) is updated and committed to api-client repository
- Bump the version of the app in the pom
- Update the [change log](./CHANGELOG.md)

</details>

## Op Docs

| Endpoint |                                             |
|---------:|:--------------------------------------------| 
|   Health | http://localhost:8080/actuator/health       |
|     Info | http://localhost:8080/actuator/info         |
|  Swagger | http://localhost:8080/swagger-ui/index.html |

## Helpful

Needed for the initial creation of the Shell[]() script

```bash
chmod +x integration-tests/init-scripts/init-localstack-setup.sh
```

```bash
awslocal sqs receive-message --endpoint-url http://sqs.us-east-1.localhost.localstack.cloud:4566 --queue-url http://sqs.us-east-1.localhost.localstack.cloud:4566/000000000000/ACCOUNT_TRANSACTION_INGESTION_DATAFLOW
```

```bash
awslocal sqs send-message --queue-url http://sqs.us-east-1.localhost.localstack.cloud:4566/000000000000/ACCOUNT_TRANSACTION_INGESTION_DATAFLOW --message-body "Hello World"
```
