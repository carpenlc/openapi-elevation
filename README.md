# elevation_services - OpenAPI generated server

Spring Boot Server 


## Overview  
This server was generated by the [OpenAPI Generator](https://openapi-generator.tech) project.
By using the [OpenAPI-Spec](https://openapis.org), you can easily generate a server stub.
This is an example of building a OpenAPI-enabled server in Java using the SpringBoot framework.

The underlying library integrating OpenAPI to SpringBoot is [springfox](https://github.com/springfox/springfox)

Start your server as a simple java application

You can view the api documentation in swagger-ui by pointing to  
http://localhost:8081/

Change default port value in application.properties# openapi-elevation

## Generating the Spring Boot server stubs

wget http://central.maven.org/maven2/org/openapitools/openapi-generator-cli/4.1.0/openapi-generator-cli-4.1.0.jar -O openapi-generator-cli.jar

```
# java -jar /export/home/carpenlc/openapi/openapi-generator/modules/openapi-generator-cli/target/openapi-generator-cli.jar generate \
  --generator-name spring \
  -i ./src/main/resources/elevation_services.yaml \
  --invoker-package mil.nga.elevation_services \
  --model-package mil.nga.elevation_services.model \
  --api-package  mil.nga.elevation_services.api \
  -o /export/home/carpenlc/java/openapi-generated
```
  
The current production environment runs with an Oracle back-end.  Oracle does not make it easy to utilize their JDBC drivers with Maven.  As such run the following 
command to load the Oracle JDBC drivers into the local Git repository:
```
# mvn install:install-file -Dfile=./lib/ojdbc8.jar -DgroupId=com.oracle -DartifactId=ojdbc8 -Dversion=12.2.0.1 -Dpackaging=jar
```

The stubs that are generated by OpenAPI are no quite correct.  The generated interfaces need to be modified in order to
allow for return types of either Error, or the appropriate return value.  The ResponseEntity return types need to be 
changed to ResponseEntity<Object>. 

