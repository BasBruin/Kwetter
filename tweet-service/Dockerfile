FROM openjdk:21-slim

EXPOSE 8082

WORKDIR /app

COPY target/tweet-service-0.0.1-SNAPSHOT.jar /app/tweetservice.jar

CMD ["java", "-jar", "tweetservice.jar"]