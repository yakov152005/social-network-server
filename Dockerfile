FROM maven:3.8.4-openjdk-22 as build
WORKDIR /SocialNetwork
COPY . .
RUN mvn clean package -DskipTests


ENV DB_URL=jdbc:mysql://root:VjpjomXubruhyFjbQCGgawFSBwtKyGvI@junction.proxy.rlwy.net:37113/railway
ENV DB_HOST=junction.proxy.rlwy.net
ENV DB_USERNAME=root
ENV DB_PASSWORD=VjpjomXubruhyFjbQCGgawFSBwtKyGvI

FROM openjdk:17-jdk-alpine
VOLUME /tmp
COPY --from=build /SocialNetwork/target/SocialNetwork.jar SocialNetwork.jar
ENTRYPOINT ["java", "-jar", "/SocialNetwork.jar"]