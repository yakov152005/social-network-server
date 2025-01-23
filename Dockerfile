FROM maven:3.8.4-openjdk-17 as build
WORKDIR /SocialNetwork
COPY . .
RUN mvn clean package -DskipTests


ENV DB_URL=jdbc:mysql://junction.proxy.rlwy.net:37113/railway
ENV DB_HOST=junction.proxy.rlwy.net
ENV DB_USERNAME=root
ENV DB_PASSWORD=VjpjomXubruhyFjbQCGgawFSBwtKyGvI
ENV DB_NAME=railway
ENV DB_PORT=37113

FROM openjdk:17-jdk-alpine
VOLUME /tmp
COPY --from=build /SocialNetwork/target/SocialNetwork.jar SocialNetwork.jar
ENTRYPOINT ["java", "-jar", "/SocialNetwork.jar"]