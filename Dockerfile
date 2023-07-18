FROM openjdk:11
COPY build/libs/*-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
ENV TZ="Asia/Ho_Chi_Minh"