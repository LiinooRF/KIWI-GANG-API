#!/bin/bash
# este script corre EN LA EC2 y lo llama el workflow del backend.
# deja la api nueva corriendo con su base de datos mysql.

set -e

# si la maquina es nueva y no tiene docker, se instala solo
command -v docker >/dev/null || sudo dnf install -y docker
sudo systemctl enable --now docker

# la red interna donde se ven los contenedores
sudo docker network create kiwi 2>/dev/null || true

# la base de datos se crea solo la primera vez, despues no se toca
# para no perder los usuarios y las ordenes en cada deploy
if ! sudo docker ps -a --format '{{.Names}}' | grep -q '^kiwi-db$'; then
  sudo docker run -d --name kiwi-db --network kiwi --restart unless-stopped \
    -e MYSQL_ROOT_PASSWORD="$DB_PASSWORD" \
    -e MYSQL_DATABASE=kiwiexpress \
    mysql:9.7
fi

# la api si se renueva en cada deploy: fuera el contenedor viejo, arriba el nuevo
sudo docker rm -f kiwi-api 2>/dev/null || true
sudo docker run -d --name kiwi-api --network kiwi --restart unless-stopped \
  -p 8080:8080 \
  -v /home/ec2-user/kiwiapi.jar:/app.jar \
  -e SPRING_DATASOURCE_URL=jdbc:mysql://kiwi-db:3306/kiwiexpress \
  -e DB_USER=root \
  -e DB_PASSWORD="$DB_PASSWORD" \
  -e JWT_SECRET="$JWT_SECRET" \
  eclipse-temurin:21-jre java -jar /app.jar

echo "api desplegada en el puerto 8080"
