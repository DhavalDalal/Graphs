package search;
import static org.junit.Assert.*;

import org.junit.Test;

import search.Location;
import search.Routes;

public class LocationSpecs {
	private Location bombay = new Location("Bombay");
	private Location pune = new Location("Pune");
	private Location coimbatore = new Location("Coimbatore");
	private Location chennai = new Location("Chennai");
	
	@Test
	public void noRouteToSelf() throws Exception {
		//When
		String route = bombay.findRoutesTo(bombay).first().show();
		//Then
		assertEquals("VOID", route);
	}
	
	@Test
	public void directRoute() throws Exception {
		//Given
		bombay.addTransportTo(pune);
		
		//When
		String route = bombay.findRoutesTo(pune).first().show();
		//Then
		assertEquals("[Bombay->Pune]", route);
	}
	
	@Test
	public void noDirectRoute() throws Exception {
		//Given
		bombay.addTransportTo(coimbatore);

		//When
		String route = bombay.findRoutesTo(pune).first().show();
		//Then
		assertEquals("", route);
	}
	
	@Test
	public void oneDirectRouteWithManyNeighbours() throws Exception {
		//Given
		bombay.addTransportTo(coimbatore);
		bombay.addTransportTo(pune);
		bombay.addTransportTo(chennai);
		//When
		String route = bombay.findRoutesTo(pune).first().show();
		//Then
		assertEquals("[Bombay->Pune]", route);
	}
	
	@Test
	public void hoppingRoute() throws Exception {
		//Given
		bombay.addTransportTo(coimbatore);
		coimbatore.addTransportTo(pune);
		pune.addTransportTo(chennai);
		//When
		//Then
		assertEquals("[Bombay->Coimbatore, Coimbatore->Pune]", bombay.findRoutesTo(pune).first().show());
		assertEquals("[Bombay->Coimbatore, Coimbatore->Pune, Pune->Chennai]", bombay.findRoutesTo(chennai).first().show());
	}

	@Test
	public void detectingLoops() throws Exception {
		//Given
		bombay.addTransportTo(coimbatore);
		coimbatore.addTransportTo(bombay);
		coimbatore.addTransportTo(pune);
		//When
		//Then
		assertEquals("[Bombay->Coimbatore, Coimbatore->Pune]", bombay.findRoutesTo(pune).first().show());
		
		pune.addTransportTo(bombay);
		pune.addTransportTo(chennai);
		assertEquals("[Bombay->Coimbatore, Coimbatore->Pune, Pune->Chennai]", bombay.findRoutesTo(chennai).first().show());
	}
	
	@Test
	public void twoRoutes() throws Exception {
		//Given
		bombay.addTransportTo(pune);
		bombay.addTransportTo(coimbatore);
		coimbatore.addTransportTo(pune);
		//When
		final Routes routes = bombay.findRoutesTo(pune);
		//Then
		assertEquals(2, routes.count());
		assertEquals("[Bombay->Pune]", routes.getRouteAt(0).show());
		assertEquals("[Bombay->Coimbatore, Coimbatore->Pune]", routes.getRouteAt(1).show());
	}
	
	@Test
	public void multipleRoutesViaSameLocation() throws Exception {
		//Given
		bombay.addTransportTo(pune);
		bombay.addTransportTo(chennai);
		bombay.addTransportTo(coimbatore);
		coimbatore.addTransportTo(pune);
		coimbatore.addTransportTo(chennai);
		chennai.addTransportTo(pune);
		//When
		final Routes routes = bombay.findRoutesTo(pune);
		//Then
		assertEquals(4, routes.count());
		assertEquals("[Bombay->Pune]", routes.getRouteAt(0).show());
		assertEquals("[Bombay->Chennai, Chennai->Pune]", routes.getRouteAt(1).show());	
		assertEquals("[Bombay->Coimbatore, Coimbatore->Pune]", routes.getRouteAt(2).show());
		assertEquals("[Bombay->Coimbatore, Coimbatore->Chennai, Chennai->Pune]", routes.getRouteAt(3).show());
	}	
}
