## Sample spring boot application to send image to aws s3

|            | Ref Links                                                                                 |
| ---------- | ----------------------------------------------------------------------------------------- |
| About S3   | https://aws.amazon.com/s3/                                                                |
| Free tier  | https://aws.amazon.com/free/                                                              |
| Regions    | https://docs.aws.amazon.com/AWSEC2/latest/UserGuide/using-regions-availability-zones.html |


### Environment Variables:

| Key                 | Value                                   |
| ------------------- | --------------------------------------- |
| ACCESS_KEY          | Access key from AWS account             |
| SECRET_ACCESS_KEY   | Secret access key from AWS account      |

### To run:

```
mvn spring-boot:run -Dspring-boot.run.arguments="ACCESS_KEY=_key_here_,SECRET_ACCESS_KEY=_key_here_"
```
