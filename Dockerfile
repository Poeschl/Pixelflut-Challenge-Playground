FROM eclipse-temurin:17-alpine

WORKDIR /app
ADD build/libs/pixelflut-challenge-playground-*.jar /app/pixelflut-challenge-playground.jar

EXPOSE 4321

ENTRYPOINT ["java", "-jar", "/app/pixelflut-challenge-playground.jar"]
CMD ["--help"]

