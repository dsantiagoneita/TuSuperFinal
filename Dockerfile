# Etapa de construcción (Build)
FROM maven:3.9.6-eclipse-temurin-21 AS build
# CREAMOS UN DIRECTORIO DE TRABAJO SEGURO
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Etapa de ejecución (Run)
FROM eclipse-temurin:21-jdk-alpine
# COPIAMOS EL JAR DESDE LA CARPETA /app/target
COPY --from=build /app/target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]