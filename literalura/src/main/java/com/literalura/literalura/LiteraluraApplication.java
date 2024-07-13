package com.literalura.literalura;

import com.literalura.literalura.Principal.Principal;
import com.literalura.literalura.model.DatosLibros;

import com.literalura.literalura.repository.AutorRepository;
import com.literalura.literalura.repository.LibrosRepository;
import com.literalura.literalura.service.Consumo.ConsumoAPI;
import com.literalura.literalura.service.Consumo.ConvierteDatos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LiteraluraApplication implements CommandLineRunner {


	private static final String URL_BASE = "https://gutendex.com/books/";

	@Autowired
	private LibrosRepository librosRepository;
	@Autowired
	private AutorRepository autorRepository;
	public static void main(String[] args) {
		SpringApplication.run(LiteraluraApplication.class, args);
	}


	@Override
	public void run(String... args) throws Exception {
		var consumoApi = new ConsumoAPI();
		var json = consumoApi.obtenerDatos(URL_BASE);
		//System.out.println(json);


		ConvierteDatos conversor = new ConvierteDatos();
		var datos = conversor.obtenerDatos(json, DatosLibros.class);
		//System.out.println(datos);




		Principal principal = new Principal(librosRepository, autorRepository);
		principal.muestraElMenu();
		//principal.buscarLibros();
		//11principal.buscarAutor();
		//System.out.println(datos);

	}
}
