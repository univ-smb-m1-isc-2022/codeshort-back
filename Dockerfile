FROM openjdk:17-jre-slim

COPY ./target/codeshort-backend-0.0.1-SNAPSHOT.jar .

EXPOSE 8080

CMD ["sh","-c","java -XX:InitialRAMPercentage=50 -XX:MaxRAMPercentage=70  -XshowSettings $JAVA_OPTS -jar codeshort-backend-0.0.1-SNAPSHOT.jar"]