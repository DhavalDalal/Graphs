package search;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import search.Location;
import search.Routes;

public class RoutesSpecs {
	private Location bombay = new Location("Bombay");
	private Location pune = new Location("Pune");
	private Location coimbatore = new Location("Coimbatore");
	private Location chennai = new Location("Chennai");
	private Location delhi = new Location("Delhi");

	@Before
	public void setupMap() {
		bombay.addTransportTo(coimbatore, 2);
		coimbatore.addTransportTo(bombay, 2);
		bombay.addTransportTo(delhi, 2);
		delhi.addTransportTo(bombay, 2);
		bombay.addTransportTo(chennai, 2);
		coimbatore.addTransportTo(delhi, 6);
		coimbatore.addTransportTo(pune, 2);
		chennai.addTransportTo(pune, 2);
		chennai.addTransportTo(coimbatore, 1);
		pune.addTransportTo(bombay, 1);
		delhi.addTransportTo(pune, 4);
		delhi.addTransportTo(chennai, 6);
	}
	
	@Test
	public void all() throws Exception {
		// When
		final Routes routes = bombay.findRoutesTo(pune);
		// Then
		assertEquals(9, routes.count());
		List<String> expectedRoutes = Arrays.asList(
			"[Bombay->Coimbatore, Coimbatore->Pune]", 
			"[Bombay->Coimbatore, Coimbatore->Delhi, Delhi->Chennai, Chennai->Pune]",
			"[Bombay->Coimbatore, Coimbatore->Delhi, Delhi->Pune]", 
			"[Bombay->Delhi, Delhi->Chennai, Chennai->Coimbatore, Coimbatore->Pune]",
			"[Bombay->Delhi, Delhi->Chennai, Chennai->Pune]", 
			"[Bombay->Delhi, Delhi->Pune]", 
			"[Bombay->Chennai, Chennai->Coimbatore, Coimbatore->Pune]",
			"[Bombay->Chennai, Chennai->Coimbatore, Coimbatore->Delhi, Delhi->Pune]",
			"[Bombay->Chennai, Chennai->Pune]"
		);
		assertRoutes(expectedRoutes, routes);
	}

	@Test
	public void direct() throws Exception {
		assertEquals(0, bombay.findRoutesTo(pune).direct().count());
		assertEquals(1, bombay.findRoutesTo(chennai).direct().count());
		assertEquals(1, bombay.findRoutesTo(chennai).direct().count());
	}
	
	@Test
	public void lessThanOrEqualToSpecifiedHops() throws Exception {
		//When
		final Routes routes = bombay.findRoutesTo(pune).hopsLessThanOrEqualTo(1);
		//Then
		assertEquals(3, routes.count());
		List<String> expectedRoutes = Arrays.asList(
			"[Bombay->Delhi, Delhi->Pune]",
			"[Bombay->Coimbatore, Coimbatore->Pune]",
			"[Bombay->Chennai, Chennai->Pune]"
		);
		assertRoutes(expectedRoutes, routes);
	}

	@Test
	public void viaLocation() throws Exception {
		//When
		final Routes routes = bombay.findRoutesTo(pune).via(delhi);
		//Then
		List<String> expectedRoutes = Arrays.asList(
			"[Bombay->Delhi, Delhi->Pune]",
			"[Bombay->Delhi, Delhi->Chennai, Chennai->Pune]",
			"[Bombay->Delhi, Delhi->Chennai, Chennai->Coimbatore, Coimbatore->Pune]",
			"[Bombay->Coimbatore, Coimbatore->Delhi, Delhi->Pune]",
			"[Bombay->Coimbatore, Coimbatore->Delhi, Delhi->Chennai, Chennai->Pune]",
			"[Bombay->Chennai, Chennai->Coimbatore, Coimbatore->Delhi, Delhi->Pune]"
		);
		assertRoutes(expectedRoutes, routes);
		assertEquals(0, pune.findRoutesTo(bombay).via(chennai).count());
		assertEquals(1, bombay.findRoutesTo(chennai).via(coimbatore).count());
	}
	
	@Test
	public void shortest() throws Exception {
		//When
		final Routes routes = chennai.findRoutesTo(bombay).shortest();
		//Then
		assertEquals(2, routes.count());
		List<String> expectedRoutes = Arrays.asList(
			"[Chennai->Pune, Pune->Bombay]",
			"[Chennai->Coimbatore, Coimbatore->Bombay]"
		);
		assertRoutes(expectedRoutes, routes);
	}
	
	@Test
	public void cheapest() throws Exception {
		//When
		final Routes routes = chennai.findRoutesTo(delhi).cheapest();
		//Then
		assertEquals(2, routes.count());
		List<String> expectedRoutes = Arrays.asList(
			"[Chennai->Pune, Pune->Bombay, Bombay->Delhi]",
			"[Chennai->Coimbatore, Coimbatore->Bombay, Bombay->Delhi]"
		);
		assertRoutes(expectedRoutes, routes);
	}

	private void assertRoutes(List<String> expectedRoutes, final Routes routes) {
		final int actual = routes.count();
		final int expected = expectedRoutes.size();
		assertEquals(String.format("Routes count does not match", expected, actual), expected, actual);
		for (String expectedRoute : expectedRoutes) {
			assertTrue(String.format("expected route %s not present in %s", expectedRoute, routes.show()), routes.contains(expectedRoute));
		}
	}

}
