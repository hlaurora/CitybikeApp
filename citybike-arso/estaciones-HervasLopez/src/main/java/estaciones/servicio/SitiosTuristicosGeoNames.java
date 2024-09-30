package estaciones.servicio;

import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import javax.json.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import estaciones.modelo.SitioTuristico;
import repositorio.FactoriaRepositorios;
import repositorio.RepositorioException;
import repositorio.RepositorioJSON;
import servicio.ServicioException;

public class SitiosTuristicosGeoNames implements ISitiosTuristicos {

	RepositorioJSON<SitioTuristico> repositorioSitios = FactoriaRepositorios.getRepositorio(SitioTuristico.class);

	// Retorna una lista con un resumen de sitios alrededor de las coordenadas (lat,
	// lng)
	public List<SitioTuristicoResumen> getSitioInteres(double lat, double lng) throws ServicioException {

		// Operación findNearbyWikipedia para obtener los sitios turísticos
		String urlString = "http://api.geonames.org/findNearbyWikipedia?lat=" + lat + "&lng=" + lng + "&username="
				+ "aadd" + "&lang=es";

		// Procesa el resultado en formato XML utilizando el API DOM

		// Obtener una factoría
		DocumentBuilderFactory factoria = DocumentBuilderFactory.newInstance();

		// Pedir a la factoría la construcción del analizador
		DocumentBuilder analizador;
		Document documento;
		try {
			analizador = factoria.newDocumentBuilder();
			documento = analizador.parse(urlString);
		} catch (Exception e) {
			throw new ServicioException(e.getMessage(), e);
		}

		NodeList elementos = documento.getElementsByTagName("entry");

		String nombre, resumen, url;
		double distancia;
		List<SitioTuristicoResumen> resumenes = new ArrayList<>();

		// Obtenemos del documento los atributos de los sitios
		for (int i = 0; i < elementos.getLength(); i++) {
			Element elemento = (Element) elementos.item(i);
			nombre = elemento.getElementsByTagName("title").item(0).getTextContent();
			resumen = elemento.getElementsByTagName("summary").item(0).getTextContent();
			url = elemento.getElementsByTagName("wikipediaUrl").item(0).getTextContent();
			String urlDecoded;
			try {
				urlDecoded = URLDecoder.decode(url, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				throw new ServicioException(e.getMessage(), e);
			}
			distancia = Double.parseDouble(elemento.getElementsByTagName("distance").item(0).getTextContent());
			// Construye un SitioTuristicoResumen y lo añade a la colección
			SitioTuristicoResumen sitioResumen = new SitioTuristicoResumen(nombre, resumen, urlDecoded, distancia);
			resumenes.add(sitioResumen);
		}
		return resumenes;
	}

	// Retorna la información completa de un sitio de interés a partir de su id
	public SitioTuristico getInfoSitioInteres(String idSitio) throws ServicioException {
		SitioTuristico sitioNuevo;
		try {
			// Primero consulta en el repositorio
			sitioNuevo = repositorioSitios.getById(idSitio);
		} catch (Exception excepcion) {
			// Procesa la información del sitio contenida en dbpedia en formato JSON
			URL url;
			InputStreamReader fuente;
			try {
				url = new URL("https://es.dbpedia.org/data/" + idSitio + ".json");
				fuente = new InputStreamReader(url.openStream());
			} catch (Exception e) {
				throw new ServicioException(e.getMessage(), e);
			}
			JsonReader jsonReader = Json.createReader(fuente);
			JsonObject jsonObject = jsonReader.readObject();

			JsonObject sitio = jsonObject.getJsonObject("http://es.dbpedia.org/resource/" + idSitio);

			// Obtiene todas las propiedades del sitio

			// Nombre del sitio
			String nombre = sitio.getJsonArray("http://www.w3.org/2000/01/rdf-schema#label").getJsonObject(0)
					.getString("value");

			// Resumen del artículo de la Wikipedia
			JsonArray resumenJson = sitio.getJsonArray("http://dbpedia.org/ontology/abstract");
			String resumen = "";
			if (resumenJson != null)
				resumen = resumenJson.getJsonObject(0).getString("value");

			// Categorías
			JsonArray categoriasJson = sitio.getJsonArray("http://www.w3.org/1999/02/22-rdf-syntax-ns#type");
			List<String> categorias = new ArrayList<>();
			if (categoriasJson != null) {
				for (JsonObject c : categoriasJson.getValuesAs(JsonObject.class)) {
					String categoria = c.getString("value");
					categorias.add(categoria);
				}
			}

			// Enlaces con información complementaria
			JsonArray enlacesJson = sitio.getJsonArray("http://dbpedia.org/ontology/wikiPageExternalLink");
			List<String> enlacesExternos = new ArrayList<>();
			if (enlacesJson != null)
				for (JsonObject e : enlacesJson.getValuesAs(JsonObject.class)) {
					String enlace = e.getString("value");
					enlacesExternos.add(enlace);
				}

			// Imagen en Wikimedia
			JsonArray imagenJson = sitio.getJsonArray("http://es.dbpedia.org/property/imagen");
			String imagen = "";
			if (imagenJson != null)
				imagen = imagenJson.getJsonObject(0).get("value").toString();

			// Crea un nuevo SitioTuristico
			sitioNuevo = new SitioTuristico(idSitio, nombre, resumen, categorias, enlacesExternos, imagen,
					"http://es.wikipedia.org/wiki/" + idSitio);

			try {
				// Añadir al repositorio
				repositorioSitios.add(sitioNuevo);
			} catch (RepositorioException e) {
				e.printStackTrace();
			}
		}
		return sitioNuevo;

	}
}
