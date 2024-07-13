package com.literalura.literalura.Principal;

import com.literalura.literalura.model.Autor;
import com.literalura.literalura.model.Datos;

import com.literalura.literalura.model.Libro;
import com.literalura.literalura.repository.AutorRepository;
import com.literalura.literalura.repository.LibrosRepository;
import com.literalura.literalura.service.Consumo.ConsumoAPI;
import com.literalura.literalura.service.Consumo.ConvierteDatos;

import java.util.*;

public class Principal {

    private Scanner teclado = new Scanner(System.in);
    private ConsumoAPI consumoApi = new ConsumoAPI();
    private static final String URL_BASE = "https://gutendex.com/books/";
    private ConvierteDatos conversor = new ConvierteDatos();
    private List<Datos>datosLibros = new ArrayList<Datos>();

    private LibrosRepository librosRepository;
    private AutorRepository autorRepository;

    public Principal(LibrosRepository librosRepository, AutorRepository autorRepository) {
        this.librosRepository = librosRepository;
        this.autorRepository = autorRepository;
    }



    public void muestraElMenu() {

        var opcion = -1;

        while (opcion != 0) {
            var menu = """
                    1 - bucar libro                   
                    2 - libros registrados
                    3 - Autores registrados
                    4 - buscar libro por idioma
                    5 - lista de autores vivos por año
                    0 - salir
                    """;

            System.out.println(menu);
            opcion = teclado.nextInt();
            teclado.nextLine();

            switch (opcion) {
                case 1:
                    buscarlibrosweb();
                    break;
               // case 2:
                 //   buscarAutor();
                 //   break;

                case 2:
                    llamarRepository();
                    break;
                case 3:
                    llamarRepositoryAutor();
                    break;
                case 4:
                    buscarLibrosIdioma();
                    break;
                case 5:
                    listaDeAutoresVivos();
                    break;

                case 0:

                    System.out.println("cerrando la aplicacion");
                    break;
                default:
                    System.out.println("opcion invalida");
            }
        }
    }


    public Datos buscarLibros() {
        System.out.println("Escribe el nombre del libro");
        var nombreLibro = teclado.nextLine();


        var json = consumoApi.obtenerDatos(URL_BASE + "?search=" + nombreLibro.replace(" ", "+"));
        // System.out.println(json);


        // Convertir el JSON a un objeto Datos
        Datos datos = conversor.obtenerDatos(json, Datos.class);

        //System.out.println(datos);

        // Obtener los resultados usando el getter
        List<Libro> libros = datos.getResultados();
        autorRepository.saveAll(libros.get(0).getAutor());
        // Mostrar los detalles de cada libro encontrado
        libros.forEach(libro -> {
            System.out.println("Título: " + libro.getTitulo());
            System.out.println("Idiomas: " + libro.getIdiomas());

            // Mostrar los autores del libro
            libro.getAutor().forEach(autor -> {
                System.out.println("Autor: " + autor.getNombre());
                System.out.println("Año de nacimiento: " + autor.getAnoNacimiento());
                System.out.println("Año de fallecimiento: " + autor.getAnoFallecimiento());
            });

            System.out.println(); // Separador entre libros
        });
        return datos;

    }

    public void buscarAutor() {
        System.out.println("Escribe el nombre del autor");
        var nombreAutor = teclado.nextLine();

        String json = null;

        try {
            json = consumoApi.obtenerDatos(URL_BASE + "?search=" + nombreAutor.replace(" ", "+"));

            // Verificar si el JSON es nulo o está vacío
            if (json == null || json.isEmpty()) {
                System.out.println("No se encontraron datos para el autor: " + nombreAutor);
            } else {
                // Convertir el JSON a un objeto Datos
                Datos datos = conversor.obtenerDatos(json, Datos.class);

                // Obtener los resultados usando el getter
                List<Libro> libros = datos.getResultados();
                autorRepository.saveAll(libros.get(0).getAutor());

                // Mostrar los autores de cada libro encontrado
                libros.forEach(libro -> {
                    System.out.println("Título: " + libro.getTitulo());

                    // Mostrar los autores del libro
                    libro.getAutor().forEach(autor -> {
                        System.out.println("Autor: " + autor.getNombre());
                        System.out.println("Año de nacimiento: " + autor.getAnoNacimiento());
                        System.out.println("Año de fallecimiento: " + autor.getAnoFallecimiento());
                    });

                    System.out.println(); // Separador entre libros
                });
            }
        } catch (Exception e) {
            System.out.println("Error al buscar autor: " + e.getMessage());
        } finally {
            muestraElMenu(); // Volver al menú principal independientemente de si hubo un error o no
        }
    }


    public void buscarlibrosweb() {
        try {
            Datos datos = buscarLibros();
            List<Libro> libros = datos.getResultados();

            // Verificar si el libro ya está registrado
            Libro libro = libros.get(0);
            Optional<Libro> libroExistente = librosRepository.findById(libro.getId());

            if (libroExistente.isPresent()) {
                System.out.println("El libro '" + libro.getTitulo() + "' ya está registrado.");
            } else {
                librosRepository.save(libro);
                System.out.println("Libro '" + libro.getTitulo() + "' registrado exitosamente.");
            }
        } catch (Exception e) {
            System.out.println("Error al buscar o registrar el libro: " + e.getMessage());
        } finally {
            muestraElMenu(); // Volver al menú principal independientemente de si hubo un error o no
        }
    }


    private void mostrarLibrosRegistrados() {

       datosLibros.forEach(datos -> {
         List<Libro> libros = datos.resultados();
         libros.forEach(libro -> {
             System.out.println("Título: " + libro.getTitulo());
          System.out.println("Idiomas: " + libro.getIdiomas());

                // Mostrar los autores del libro
            libro.getAutor().forEach(autor -> {
              System.out.println("Autor: " + autor.getNombre());
            System.out.println("Año de nacimiento: " + autor.getAnoNacimiento());
            System.out.println("Año de fallecimiento: " + autor.getAnoFallecimiento());
          });

         System.out.println(); // Separador entre libros
           });
          });

    }
    private void llamarRepository(){
        List<Libro> libros = librosRepository.findAll();

        libros.stream()
                        .sorted(Comparator.comparing(Libro::getTitulo))
                                .forEach(System.out::println);

    }
    private void llamarRepositoryAutor(){
        List<Autor> autors = autorRepository.findAll();

        autors.stream()
                .sorted(Comparator.comparing(Autor::getId))
                .forEach(autor -> {
                    System.out.println(autor);
                    System.out.println("Libros:");
                    List<Libro> libros = Collections.singletonList(autor.getLibro());
                    if (libros != null && !libros.isEmpty()) {
                        libros.forEach(libro -> System.out.println("  - " + libro.getTitulo()));
                    } else {
                        System.out.println("  No disponible");
                    }
                    System.out.println();
                });
    }

    private void buscarLibrosIdioma() {
        System.out.println("""
        
        Lista de libros según el idioma
        
            1. Español (ES)
            2. Inglés (EN)
            3. Francés (FR)
            4. Portugués (PT)
            
            5. Regresar al menú principal
            
        Ingrese el idioma para buscar los libros:
        
    """);

        int opcion;

        if (teclado.hasNextInt()) {
            opcion = teclado.nextInt();
            teclado.nextLine();
            List<Libro> libros = null;
            switch (opcion) {
                case 1:
                    libros = librosRepository.findByIdioma("es");
                    break;

                case 2:
                    libros = librosRepository.findByIdioma("fr");
                    break;
                case 3:
                    libros = librosRepository.findByIdioma("pt");
                    break;
                case 4:
                    System.out.println("Gracias por usar nuestros servicios");
                    muestraElMenu();
                    return;
                default:
                    System.out.println("Opción no válida. Por favor, ingrese un número del 1 al 5");
                    return;
            }

            if (libros != null && !libros.isEmpty()) {
                libros.forEach(System.out::println);
            } else {
                System.out.println("No se encontraron libros para el idioma seleccionado.");
            }
        } else {
            System.out.println("Opción no válida. Por favor, ingrese un número del 1 al 5");
            teclado.nextLine();
        }
    }

    private void listaDeAutoresVivos() {
        System.out.println("Indique a partir de qué año desea buscar el autor, en su año vivo:");
        String fecha = teclado.nextLine();
        try {
            List<Autor> autoresVivos = autorRepository.autorVivoEnDeterminadoAno(Integer.parseInt(fecha));

            autoresVivos.forEach(System.out::println);
        } catch (NumberFormatException e) {
            System.err.println("Error: El año debe ser un número válido.");
        } catch (Exception e) {
            throw new RuntimeException("Error al buscar autores vivos en el año " + fecha, e);
        }
    }
    public void manejarErrorYVolverAlMenu(String mensajeError) {
        System.err.println("Error: " + mensajeError);
        System.out.println("Volviendo al menú principal...");
        muestraElMenu();
    }


}
