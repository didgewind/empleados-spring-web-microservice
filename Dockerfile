FROM openjdk:8-jdk-alpine
COPY target/empleados-web-0.0.1-SNAPSHOT.war empleados-web-0.0.1-SNAPSHOT.war
EXPOSE 3333
ENTRYPOINT ["java", "-DEUREKA_SERVER=http://eureka-service:1111/eureka","-jar","/empleados-web-0.0.1-SNAPSHOT.war"]