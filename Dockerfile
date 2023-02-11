FROM eclipse-temurin:17-jdk-alpine
WORKDIR "/csc-bot"
COPY "target/*.jar" "csc-bot-0.1.0.jar"
COPY ".env" ".env"
COPY "data" "data"
ENTRYPOINT ["java","-jar","csc-bot-0.1.0.jar"]