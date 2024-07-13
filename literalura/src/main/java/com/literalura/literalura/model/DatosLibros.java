package com.literalura.literalura.model;
import com.literalura.literalura.model.Datos;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import javax.annotation.processing.Generated;
import java.util.List;


@JsonIgnoreProperties(ignoreUnknown = true)
public record DatosLibros(
        @JsonAlias("id") int id,
        @Column(unique = true)
        @JsonAlias("title") String titulo,
        @JsonAlias("authors") List<DatosAutor> autor,
        @JsonAlias("languages") List<String> idiomas

) {
    @Override
    public int id() {
        return id;
    }

    @Override
    public String titulo() {
        return titulo;
    }

    @Override
    public List<DatosAutor> autor() {
        return autor;
    }

    @Override
    public List<String> idiomas() {
        return idiomas;
    }

}
