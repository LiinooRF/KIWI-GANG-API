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

### cualquier maquina nueva

el deploy funciona en cualquier ec2 que creemos, no hay que instalar nada a mano (docker lo
instala el workflow solo si falta):

1. en el learner lab crear una instancia amazon linux 2023 con el key pair `vockey`
2. en el security group abrir los puertos 22 (ssh), 80 (el front) y 8080 (la api)
3. poner la ip de la instancia en el secret `EC2_HOST` de los dos repos
4. correr los workflows desde la pestaña actions (o empujar algo a staging)

opcional pero recomendado: asociarle una elastic ip (consola ec2, abajo a la izquierda,
allocate y associate) para que la ip no cambie al reiniciar la maquina.

la api queda en el puerto 8080 y el front en el 80.

### secrets en github

en settings > secrets and variables > actions de este repo:

- `EC2_HOST`: la ip publica de la instancia
- `EC2_KEY`: el contenido completo de `labsuser.pem`
- `DB_PASSWORD`: la clave que va a tener mysql, cualquiera
- `JWT_SECRET`: la clave para firmar los token, de 32 caracteres o mas

si faltan los secrets el job de deploy no falla, solo avisa y no despliega.

### prender la maquina un dia normal

con la elastic ip asociada no hay que tocar nada en github:

1. start lab y esperar que quede en verde
2. en la consola ec2 -> instances -> seleccionar la instancia -> instance state -> start
3. esperar un minuto y listo, docker levanta los contenedores solo y queda todo en la misma ip

el front en http://LA_IP y la api en http://LA_IP:8080/swagger-ui.html

### si el lab borra todo

cuando el lab se termina de verdad borra todos los recursos. hay que repetir los pasos de
preparar la ec2 (crear la instancia, instalar docker, asociar una elastic ip nueva) y despues
actualizar `EC2_HOST` en los dos repos y correr los workflows una vez desde la pestaña
actions (run workflow), porque el front guarda la ip del api al compilar.
