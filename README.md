# KIWI-GANG-API

api del proyecto kiwiexpress, hecha en java con spring boot.
asignatura dsy1104 desarrollo fullstack ii, duoc uc.

el frontend en react esta en el otro repo: https://github.com/LiinooRF/KIWI-GANG

## equipo

- lino requena
- tomas aliaga
- ignacio martinez

## como vamos a trabajar

igual que en el otro repo:

- `main` es la version estable, no se sube nada directo
- `staging` es donde juntamos todo
- cada uno crea su rama desde staging y despues manda el pull request

## como correrlo en local

hay que tener java 21 y mysql. antes de partir hay que definir estas variables de entorno
(estan explicadas en src/main/resources/application-example.properties):

- `DB_USER` y `DB_PASSWORD`: el usuario y la clave de tu mysql
- `JWT_SECRET`: la clave para firmar los token, de 32 caracteres o mas

y despues:

```
./mvnw spring-boot:run
```

swagger queda en http://localhost:8080/swagger-ui.html

## despliegue en aws (learner lab)

todo queda en una sola ec2 con docker: mysql, la api y el frontend (el front se despliega
desde el otro repo). en cada deploy se borran los contenedores y se crean de nuevo, la base
parte vacia cada vez.

el workflow `.github/workflows/ci.yml` tiene dos jobs:

- `build`: compila y corre los tests, en cada pull request y en cada push a staging
- `deploy`: solo en push a staging, sube el jar a la ec2 y corre `deploy/desplegar.sh`

### preparar la ec2 (una sola vez)

1. en el learner lab crear una instancia amazon linux 2023 con el key pair `vockey`
2. descargar `labsuser.pem` desde aws details y entrar:

```
ssh -i labsuser.pem ec2-user@IP_PUBLICA
```

3. instalar docker:

```
sudo dnf install -y docker
sudo systemctl enable --now docker
sudo usermod -aG docker ec2-user
```

y salir y volver a entrar por ssh para que tome el permiso de docker.

la api queda en el puerto 8080 y el front en el 80.

### secrets en github

en settings > secrets and variables > actions de este repo:

- `EC2_HOST`: la ip publica de la instancia
- `EC2_KEY`: el contenido completo de `labsuser.pem`
- `DB_PASSWORD`: la clave que va a tener mysql, cualquiera
- `JWT_SECRET`: la clave para firmar los token, de 32 caracteres o mas

si faltan los secrets el job de deploy no falla, solo avisa y no despliega.

### ojo cuando se reinicia el lab

al volver a prender el lab la instancia cambia de ip publica, hay que actualizar `EC2_HOST`
en los dos repos y volver a correr los workflows desde la pestaña actions.
