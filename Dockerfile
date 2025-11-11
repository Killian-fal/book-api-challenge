FROM amazoncorretto:21-alpine-jdk AS build
WORKDIR /app
COPY . .
RUN ./gradlew clean build -x test

FROM build AS runner

COPY --from=build /app/build/libs/*.jar app.jar

EXPOSE 7000
EXPOSE 5005

CMD ["java", "-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005", "-jar", "app.jar"]