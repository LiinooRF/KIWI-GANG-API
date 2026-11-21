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

el despliegue se hace TODO desde el repo del frontend con un solo workflow
(`deploy.yml` de KIWI-GANG): compila este backend, lo sube a la ec2 y lo deja
corriendo con mysql y la pagina, todo con docker. los secrets y el paso a paso
estan explicados en el README de ese repo.

este repo solo tiene el ci que compila y corre las pruebas en cada pull request.
