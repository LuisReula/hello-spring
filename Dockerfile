FROM openjdk:11 AS base
WORKDIR /Desktop/hello-spring
COPY ./ ./
RUN ./gradlew assemble


FROM openjdk:18-alpine
WORKDIR /Desktop/hello-spring
COPY --from=base /Desktop/hello-spring/build/libs/demo-0.0.1-SNAPSHOT.jar ./
CMD java -jar demo-0.0.1-SNAPSHOT.jar