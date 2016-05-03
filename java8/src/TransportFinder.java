import search.Location;
import search.Routes;

public class TransportFinder {
    public static void main(String[] args) {
        Location bombay = new Location("Bombay");
        Location pune = new Location("Pune");
        Location coimbatore = new Location("Coimbatore");
        Location chennai = new Location("Chennai");
        Location delhi = new Location("Delhi");

        bombay.addTransportTo(coimbatore, 2);
        coimbatore.addTransportTo(bombay, 2);
        coimbatore.addTransportTo(delhi, 6);
        coimbatore.addTransportTo(pune, 2);
        bombay.addTransportTo(delhi, 2);
        delhi.addTransportTo(bombay, 2);
        bombay.addTransportTo(chennai, 2);
        delhi.addTransportTo(pune, 4);
        delhi.addTransportTo(chennai, 6);
        chennai.addTransportTo(coimbatore, 1);
        chennai.addTransportTo(pune, 2);
        pune.addTransportTo(bombay, 1);

        final Routes routes = bombay.findRoutesTo(delhi)
                .via(coimbatore)
                .shortest()
                .cheapest();
        System.out.println("Bombay to Delhi Via Coimbatore = " + routes.show());
        System.out.println(routes.count());
        System.out.println(routes.first().show());
    }
}
