#!/bin/bash
# corre en la ec2, renueva el contenedor de la api
set -e

command -v docker >/dev/null || sudo dnf install -y docker
sudo systemctl enable --now docker

sudo docker network create kiwi 2>/dev/null || true

# la base solo se crea la primera vez, despues no se toca
if ! sudo docker ps -a --format '{{.Names}}' | grep -q '^kiwi-db$'; then
  sudo docker run -d --name kiwi-db --network kiwi --restart unless-stopped \
    -e MYSQL_ROOT_PASSWORD="$DB_PASSWORD" \
    -e MYSQL_DATABASE=kiwiexpress \
    mysql:9.7
fi

sudo docker rm -f kiwi-api 2>/dev/null || true
sudo docker run -d --name kiwi-api --network kiwi --restart unless-stopped \
  -p 8080:8080 \
  -v /home/ec2-user/kiwiapi.jar:/app.jar \
  -e SPRING_DATASOURCE_URL=jdbc:mysql://kiwi-db:3306/kiwiexpress \
  -e DB_USER=root \
  -e DB_PASSWORD="$DB_PASSWORD" \
  -e JWT_SECRET="$JWT_SECRET" \
  eclipse-temurin:21-jre java -jar /app.jar

echo "api lista en el 8080"
