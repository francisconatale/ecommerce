# Etapa 1: Build
FROM eclipse-temurin:17-jdk-alpine AS builder
WORKDIR /app

# Copiamos los archivos de configuracion de maven primero
COPY .mvn/ .mvn
COPY mvnw pom.xml ./

# Descargamos dependencias de maven (esto se cachea en la capa de docker a menos que cambie pom.xml)
# Convertimos el mvnw a formato unix por si acaso fue editado en windows
RUN sed -i 's/\r$//' mvnw
RUN chmod +x ./mvnw
RUN ./mvnw dependency:go-offline

# Copiamos el codigo fuente
COPY src ./src

# Compilamos y generamos el .jar
RUN ./mvnw clean package -DskipTests

# Etapa 2: Run (Imagen final limpia solo con JRE)
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar

# Variables de entorno por defecto (se sobreescriben en docker-compose)
ENV SPRING_PROFILES_ACTIVE=prod

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]