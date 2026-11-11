package com.duoc.kiwiapi.Config;

import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

//Lo define como un file de configuracion.
@Configuration 
//Estructura de definicion de la API en general.
@OpenAPIDefinition(
    info = @Info(
        title = "KiwiExpressAPI",
        version = "1.0.0",
        description = "Plataforma de Envios"
    )
)
public class OpenApiConfig {
    //Queda vacio, las definiciones de la api se hicieron arriba.
}
