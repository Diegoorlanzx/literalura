package com.literalura.literalura.model;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "libros")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Libro {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(unique = true)
    @JsonAlias("title")
    private String titulo;


    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "libro_id")
    @JsonAlias("authors")

    private List<Autor> autor;

    //@ElementCollection
    //@CollectionTable(name = "libros_idiomas", joinColumns = @JoinColumn(name = "libro_id"))
    //@Column(name = "idioma")
    @JsonAlias("languages")
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> idiomas;




    // Getters
    public Long getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public List<Autor> getAutor() {
        return autor;
    }

    public List<String> getIdiomas() {
        return idiomas;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setAutor(List<Autor> autor) {
        this.autor = autor;
    }

    public void setIdiomas(List<String> idiomas) {
        this.idiomas = idiomas;
    }
    @Override
    public String toString() {
        String autoresr = autor != null ? autor.stream().map(Autor::toString).collect(Collectors.joining(", ")) : "No disponible";
        return "Título: " + titulo + "\n" +
                "Idiomas: " + idiomas + "\n" +
                "Autores: " + autoresr + "\n";
    }
}
