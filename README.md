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

todo corre en una sola ec2: la api y mysql juntos.

1. en el learner lab crear una instancia amazon linux 2023 con el key pair `vockey`
2. descargar `labsuser.pem` desde aws details y entrar:

```
ssh -i labsuser.pem ec2-user@IP_PUBLICA
```

3. instalar java y mysql:

```
sudo dnf install -y java-21-amazon-corretto-headless
sudo dnf install -y https://repo.mysql.com/mysql97-community-release-el9-1.noarch.rpm
sudo sed -i 's/$releasever/9/g' /etc/yum.repos.d/mysql-community*.repo
sudo dnf install -y mysql-community-server
sudo systemctl enable --now mysqld
```

(el sed es porque el repo de mysql no reconoce la version de amazon linux)

4. mysql parte con una clave temporal para root, se ve con:

```
sudo grep 'temporary password' /var/log/mysqld.log
```

entrar con `mysql -u root -p` y crear el usuario de la api (mysql pide claves con mayuscula, numero y simbolo):

```
ALTER USER 'root'@'localhost' IDENTIFIED BY 'Root-Kiwi-2026';
CREATE USER 'kiwi'@'localhost' IDENTIFIED BY 'Api-Kiwi-2026';
GRANT ALL ON kiwiexpress.* TO 'kiwi'@'localhost';
```

5. crear el archivo `/home/ec2-user/kiwiapi.env` (queda solo en la ec2, nunca en el repo):

```
DB_USER=kiwi
DB_PASSWORD=Api-Kiwi-2026
JWT_SECRET=una-clave-larga-de-32-caracteres-o-mas
```

6. despues del primer deploy, para que la api parta sola si se reinicia la instancia:

```
sudo systemctl enable kiwiapi
```

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
