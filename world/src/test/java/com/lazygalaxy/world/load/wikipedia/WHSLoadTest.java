package com.lazygalaxy.world.load.wikipedia;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.lazygalaxy.world.domain.Image;
import com.lazygalaxy.world.domain.WikipediaPage;
import com.lazygalaxy.world.load.wikipedia.WHSByCountryProcessor;
import com.lazygalaxy.world.load.wikipedia.WHSParentProcessor;
import com.lazygalaxy.world.load.wikipedia.WikipediaPageJSoupLoad;
import com.mongodb.client.model.geojson.Point;
import com.mongodb.client.model.geojson.Position;

import junit.framework.TestCase;

public class WHSLoadTest extends TestCase {
	private static final Logger LOGGER = LogManager.getLogger(WHSLoadTest.class);

	public void testParent() throws Exception {

		WikipediaPageJSoupLoad loader = new WikipediaPageJSoupLoad(new WHSParentProcessor());
		List<WikipediaPage> pages = loader
				.getMongoDocuments(loader.getHTMLDocument("wikipedia/worldheritagesitesbycountry.html"));

		assertEquals(167, pages.size());

		LOGGER.info(pages.get(0));
		assertEquals("worldheritagesitesbycountry", pages.get(0).id);
		assertEquals("com.lazygalaxy.load.wikipedia.WHSParentProcessor", pages.get(0).processor);
		assertEquals(
				"As of July 2018, there are a total of 1,092 World Heritage sites located in 167 States Parties (countries that have adhered to the World Heritage Convention), of which 845 are cultural, 209 are natural and 38 are mixed properties.[1] The countries have been divided by the World Heritage Committee into five geographic zones: Africa, Arab States, Asia and the Pacific, Europe and North America, and Latin America and the Caribbean. The country with the largest number of sites (including sites shared with other countries) is Italy, with 54 entries.[2] The country with the largest number of sites by itself alone (excluding sites shared with other countries) is China, with 53 entries.[3]",
				pages.get(0).summary);

		LOGGER.info(pages.get(1));
		assertEquals("listofworldheritagesitesinafghanistan", pages.get(1).id);
		assertEquals("com.lazygalaxy.load.wikipedia.WHSByCountryProcessor", pages.get(1).processor);

		LOGGER.info(pages.get(pages.size() - 1));
		assertEquals("listofworldheritagesitesinzimbabwe", pages.get(pages.size() - 1).id);
		assertEquals("com.lazygalaxy.load.wikipedia.WHSByCountryProcessor", pages.get(pages.size() - 1).processor);
	}

	public void testByCountryItaly() throws Exception {

		WikipediaPageJSoupLoad loader = new WikipediaPageJSoupLoad(new WHSByCountryProcessor());
		List<WikipediaPage> pages = loader
				.getMongoDocuments(loader.getHTMLDocument("wikipedia/listofworldheritagesitesinitaly.html"));

		assertEquals(137, pages.size());

		LOGGER.info(pages.get(0));
		WikipediaPage expectedPage = new WikipediaPage("List of World Heritage Sites in Italy", new String[] {});
		expectedPage.image = new Image("italyunescowhcregionssvg", "",
				"http://upload.wikimedia.org/wikipedia/commons/9/9b/Italy_UNESCO_WHC_Regions.svg");
		expectedPage.summary = "The United Nations Educational, Scientific and Cultural Organization (UNESCO) World Heritage Sites are places of importance to cultural or natural heritage as described in the UNESCO World Heritage Convention, established in 1972.[1] Italy ratified the convention on June 23, 1978, making its historical sites eligible for inclusion on the list.[2] As of 2018, Italy has a total of 54 inscribed properties, making it the state party with the most World Heritage Sites.[3] Sites in Italy were first inscribed on the list at the 3rd Session of the World Heritage Committee, held in Cairo and Luxor, Egypt in 1979. At that session, one site was added: the \"Rock Drawings in Valcamonica\".[4] A total of 25 (approximately half), of all Italian sites were added during the 1990s (where it was a member of the World Heritage Committee for the entire decade) with 10 sites added at the 21st session held in Naples, Italy in 1997. Italy has served as a member of the World Heritage Committee four times, specifically, 1978-1985 (8 years), 1987-1993 (7 years), 1993-1999 (7 years), and 1999-2001 (3 years).[5] Out of Italy's 54 heritage sites, five are shared with other countries: \"Monte San Giorgio\" and \"Rhaetian Railway in the Albula / Bernina Landscapes\" with Switzerland; \"Historic Centre of Rome\" with the Vatican; \"Prehistoric pile dwellings around the Alps\" with Austria, France, Germany, Slovenia and Switzerland; and \"Venetian Works of Defence between 15th and 17th centuries: Stato da Terra – western Stato da Mar with Croatia and Montenegro. Five World Heritage Sites in Italy are of the natural type, all others are cultural sites (49). Therefore, Italy has the largest number of world \"cultural\" heritage sites followed by Spain with 41 cultural sites.[2]";
		expectedPage.processor = "com.lazygalaxy.load.wikipedia.WHSByCountryProcessor";
		assertPage(expectedPage, pages.get(0));

		LOGGER.info(pages.get(1));
		expectedPage = new WikipediaPage("Palace at Caserta", new String[] {});
		expectedPage.image = new Image("caserta2cfuentededianayactec3b3n30jpg",
				"A row of water basins leading to a large palace building.",
				"http://upload.wikimedia.org/wikipedia/commons/5/58/Caserta%2C_fuente_de_Diana_y_Acte%C3%B3n._30.JPG");
		expectedPage.summary = "18th-Century Royal Palace at Caserta with the Park, the Aqueduct of Vanvitelli, and the San Leucio Complex. Large scale palace and park created by the Bourbon King of Naples Charles III in the mid 18th century. It is notable for blending into the environment. The site also includes an ambitious new town and industrial complex.[7]. ";
		expectedPage.coords = new Point(new Position(41.07333333333334, 14.32638888888889));
		assertPage(expectedPage, pages.get(1));

		LOGGER.info(pages.get(pages.size() - 1));
		assertEquals("scrovegnichapel", pages.get(pages.size() - 1).id);
		assertEquals("http://upload.wikimedia.org/wikipedia/commons/4/44/La_Cappella_degli_Scrovegni.JPG",
				pages.get(pages.size() - 1).image.link);
		assertNull(pages.get(pages.size() - 1).processor);
	}

	public void testByCountryBelrus() throws Exception {

		WikipediaPageJSoupLoad loader = new WikipediaPageJSoupLoad(new WHSByCountryProcessor());
		List<WikipediaPage> pages = loader
				.getMongoDocuments(loader.getHTMLDocument("wikipedia/listofworldheritagesitesinbelarus.html"));

		assertEquals(21, pages.size());

		LOGGER.info(pages.get(0));
		WikipediaPage expectedPage = new WikipediaPage("List of World Heritage sites in Belarus", new String[] {});
		expectedPage.image = new Image("belarusadmlocationmapsvg",
				"List of World Heritage sites in Belarus is located in Belarus",
				"http://upload.wikimedia.org/wikipedia/commons/1/12/Belarus_adm_location_map.svg");
		expectedPage.summary = "This is a list of World Heritage sites in Belarus with properties of cultural and natural heritage in Belarus. Currently, four properties in Belarus are inscribed on the World Heritage List.[1] Three properties are cultural and one is natural.[1] The tentative list of Belarus contains eleven properties.[1]";
		expectedPage.processor = "com.lazygalaxy.load.wikipedia.WHSByCountryProcessor";
		assertPage(expectedPage, pages.get(0));
	}

	public void testHistoricCentreOfFlorence() throws Exception {

		WikipediaPageJSoupLoad loader = new WikipediaPageJSoupLoad();
		List<WikipediaPage> pages = loader
				.getMongoDocuments(loader.getHTMLDocument("wikipedia/historiccentreofflorence.html"));

		assertEquals(1, pages.size());

		LOGGER.info(pages.get(0));
		WikipediaPage expectedPage = new WikipediaPage("Historic Centre of Florence", new String[] {});
		expectedPage.image = new Image("piazzasignoriafirenzejpg", "Piazza Signoria - Firenze.jpg",
				"http://upload.wikimedia.org/wikipedia/commons/e/eb/Piazza_Signoria_-_Firenze.jpg");
		expectedPage.summary = "The historic centre of Florence is part of quartiere 1 of the Italian city of Florence. This quarter was named a World Heritage Site by UNESCO in 1982. Built on the site of an Etruscan settlement, Florence, the symbol of the Renaissance, rose to economic and cultural pre-eminence under the Medici in the 15th and 16th centuries. Its 600 years of extraordinary artistic activity can be seen above all in the 13th-century cathedral (Santa Maria del Fiore), the Church of Santa Croce, the Uffizi and the Pitti Palace, the work of great masters such as Giotto, Brunelleschi, Botticelli and Michelangelo.[1]";
		expectedPage.coords = new Point(new Position(43.77305555555555, 11.25611111111111));

		assertPage(expectedPage, pages.get(0));
	}

	private void assertPage(WikipediaPage expectedPage, WikipediaPage actualPage) {
		assertEquals(expectedPage.summary, actualPage.summary);
		assertEquals(expectedPage.coords, actualPage.coords);
		assertEquals(expectedPage.labels, actualPage.labels);
		assertEquals(expectedPage.image, actualPage.image);
		assertEquals(expectedPage.processor, actualPage.processor);
		assertEquals(expectedPage.name, actualPage.name);
		assertEquals(expectedPage, actualPage);
	}
}
