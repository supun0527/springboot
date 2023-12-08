# Product Catalogue
This is resposible for maintaining all product configurations


## Endpoints
| Endpoint   | HTTP Method | Query Parameters                                                                                  | Description        |
|------------|-------------|---------------------------------------------------------------------------------------------------|--------------------|
| /v1/sample | POST        |                                                                                                   | sample description |

## Request and Response bodies

### POST /v1/sample
Sample request
```json
{
  "field1": "value1",
  "field2": "value2", // Optional
}

```

Sample response:
```json
{
  "id": 1,
  "field1": "value1",
  "field2": "value2",
  "createdAt": "2023-10-30T17:04:14.371411",
  "updatedAt": "2023-10-30T17:04:14.37142"
}
```

# Set up sonarqube for Dev and prod env 

- Launch AWS ec2 instance 
- Connect to EC2 Instance 
- install docker ane docker compose 
- Create docker compose file 
- Configure SonarQube for Microservices
     - Access SonarQube web interface at http://your-ec2-instance-ip:9000.
     - Log in with the default credentials (admin/admin).
     - Configure SonarQube as needed, create projects, and obtain project keys.
- Integrate sonarqube in microservice 
- run following command locally or step in the codepipeline 
```bash
mvn sonar:sonar
```
 