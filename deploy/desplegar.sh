#!/bin/bash
# este script corre en la ec2, lo copia y lo ejecuta el workflow en cada deploy
# borra los contenedores de antes y los vuelve a crear, la base parte vacia cada vez
# DB_PASSWORD y JWT_SECRET llegan como variables desde los secrets de github
set -e

sudo docker network create kiwi 2>/dev/null || true

# se borra todo lo anterior
sudo docker rm -f kiwi-api kiwi-db 2>/dev/null || true

# mysql en un contenedor, sin volumen asi que no guarda nada entre deploys
sudo docker run -d --name kiwi-db --network kiwi --restart unless-stopped \
  -e MYSQL_ROOT_PASSWORD="$DB_PASSWORD" \
  -e MYSQL_DATABASE=kiwiexpress \
  mysql:9.7

# la api con el jar nuevo. si parte antes que mysql se cae y docker la reinicia sola
sudo docker run -d --name kiwi-api --network kiwi --restart unless-stopped \
  -p 8080:8080 \
  -v /home/ec2-user/kiwiapi.jar:/app.jar \
  -e SPRING_DATASOURCE_URL=jdbc:mysql://kiwi-db:3306/kiwiexpress \
  -e DB_USER=root \
  -e DB_PASSWORD="$DB_PASSWORD" \
  -e JWT_SECRET="$JWT_SECRET" \
  eclipse-temurin:21-jre java -jar /app.jar

echo "api desplegada"
