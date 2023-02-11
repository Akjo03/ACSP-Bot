FROM eclipse-temurin:17-jdk-alpine
WORKDIR "/csc-bot"
COPY "src" "src"
COPY "pom.xml" "pom.xml"
COPY "mvnw" "mvnw"
COPY "mvnw.cmd" "mvnw.cmd"
COPY ".mvn" ".mvn"
RUN ["chmod","+x","mvnw"]
RUN ["./mvnw","clean","package","-DskipTests"]
RUN ["cp","target/csc-bot-0.1.0.jar","csc-bot-0.1.0.jar"]
COPY ".env" ".env"
COPY "data/bot_config.json" "data/bot_config.json"
ENTRYPOINT ["java","-jar","csc-bot-0.1.0.jar"]