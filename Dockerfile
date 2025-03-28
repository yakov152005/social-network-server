FROM maven:3.8.4-openjdk-17 as build
WORKDIR /SocialNetwork
COPY pom.xml ./
RUN mvn dependency:go-offline -B
COPY src ./src
RUN mvn clean package -DskipTests


FROM openjdk:17-jdk-alpine
VOLUME /tmp
COPY --from=build /SocialNetwork/target/SocialNetwork.jar SocialNetwork.jar
ENTRYPOINT ["java", "-jar", "/SocialNetwork.jar"]
