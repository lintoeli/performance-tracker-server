# Imagen base
FROM openjdk:17-jdk

# Variables de entorno para la base de datos
ENV POSTGRES_USER=${POSTGRES_USER}
ENV POSTGRES_PASSWORD=${POSTGRES_PASSWORD}
ENV POSTGRES_DB=${POSTGRES_DB}

# Agregar archivo .jar de la aplicación
ADD target/performance-tracker-0.0.1-SNAPSHOT.jar perfomance-tracker.jar

# Exponer puerto de la aplicación
EXPOSE 8080

# Comando para ejecutar la aplicación
CMD ["java", "-jar", "perfomance-tracker.jar"]