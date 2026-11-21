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

este repo despliega el backend solo: cada push a staging corre las pruebas y despues
sube el jar a la ec2 (tambien con el boton run workflow de la pestaña actions). el
frontend se despliega desde su propio repo, cada uno lo suyo.

en la ec2 la api queda en el puerto 8080 y mysql en un contenedor interno que no se
borra entre deploys, asi no se pierden los usuarios y las ordenes.

### preparar una ec2 (cualquiera sirve)

1. crear una instancia amazon linux 2023 con el key pair `vockey`
2. abrir los puertos 22 (ssh), 80 (la pagina) y 8080 (la api)
3. asociarle una elastic ip para que la ip no cambie al reiniciar (recomendado)

docker no hay que instalarlo, lo instala el deploy solo si falta.

### secrets en github (en este repo)

en settings > secrets and variables > actions:

- `EC2_HOST`: la ip publica de la instancia
- `EC2_KEY`: el contenido completo de `labsuser.pem`
- `DB_PASSWORD`: la clave que va a tener mysql, cualquiera
- `JWT_SECRET`: la clave para firmar los token del login, 32 caracteres o mas

si faltan los secrets el deploy no falla, solo avisa y no despliega.
