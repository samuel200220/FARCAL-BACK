# Étape 1 : Build du projet
FROM maven:3.9.6-eclipse-temurin-23 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn clean package -DskipTests

# Étape 2 : Exécution de l’application
FROM eclipse-temurin:23-jdk
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

# Variable d'environnement pour le port
ENV PORT=8080

# Expose le port pour Render
EXPOSE 8080

# Commande de lancement
ENTRYPOINT ["java", "-jar", "app.jar"]
