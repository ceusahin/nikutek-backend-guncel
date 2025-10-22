# Temel Java 17 imajı
FROM eclipse-temurin:17-jdk

# Çalışma dizinini ayarla
WORKDIR /app

# Jar dosyasını kopyala
COPY target/*.jar app.jar

# Port (Spring Boot varsayılanı 8080)
EXPOSE 8080

# Uygulamayı çalıştır
ENTRYPOINT ["java", "-jar", "app.jar"]