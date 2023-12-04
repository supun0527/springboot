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