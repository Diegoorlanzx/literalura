package com.literalura.literalura.service.Consumo;

public interface IconvierteDatos {

    <T> T obtenerDatos(String json, Class<T> clase);
}
