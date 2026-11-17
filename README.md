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

el workflow `.github/workflows/ci.yml` tiene dos jobs:

- `build`: compila y corre los tests, en cada pull request y en cada push a staging
- `deploy`: solo en push a staging, sube el jar a la ec2 por scp y reinicia la api

### preparar la ec2 (se hace una sola vez)

1. entrar al learner lab, darle a start lab y esperar que quede en verde
2. en ec2 crear una instancia amazon linux 2023, t2.micro o t3.micro, con el key pair `vockey`
3. en el security group abrir el puerto 22 (ssh) y el 8080 (la api)
4. en aws details descargar la llave `labsuser.pem` y entrar a la instancia:

```
ssh -i labsuser.pem ec2-user@IP_PUBLICA
```

5. instalar java y mysql (mariadb) en la instancia:

```
sudo dnf install -y java-21-amazon-corretto-headless mariadb105-server
sudo systemctl enable --now mariadb
sudo mysql -e "CREATE USER 'kiwi'@'localhost' IDENTIFIED BY 'una-clave'; GRANT ALL ON kiwiexpress.* TO 'kiwi'@'localhost';"
```

6. crear el archivo `/home/ec2-user/kiwiapi.env` con las variables (este archivo queda solo en la ec2, nunca en el repo):

```
DB_USER=kiwi
DB_PASSWORD=una-clave
JWT_SECRET=una-clave-larga-de-32-caracteres-o-mas
```

7. hacer que el servicio parta solo cuando se prenda la instancia:

```
sudo systemctl enable kiwiapi
```

(el archivo del servicio lo copia el deploy, esta en `deploy/kiwiapi.service`)

### secrets en github

en settings > secrets and variables > actions del repo:

- `EC2_KEY`: el contenido completo de `labsuser.pem`
- `EC2_HOST`: la ip publica de la instancia

si faltan los secrets el job de deploy no falla, solo avisa y no despliega.

### ojo cuando se reinicia el lab

el lab se apaga solo despues de unas horas y al volver a prenderlo la instancia cambia de ip publica.
la llave `labsuser.pem` es la misma, asi que solo hay que actualizar `EC2_HOST` con la ip nueva
(se ve en la consola de ec2) y volver a correr el workflow desde la pestaña actions.

el lab no deja crear roles iam, si en algun lado pide un rol hay que poner `LabRole`.
