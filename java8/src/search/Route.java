package search;

import java.util.ArrayList;
import java.util.List;

public class Route {

    public static final Route VOID = new Route() {
        public void add(Transport transport) {
        }

        public boolean hasDestination(Location target) {
            return false;
        }

        public String show() {
            return "VOID";
        }
    };
    private List<Transport> transports = new ArrayList<>();

    public boolean hasDestination(Location target) {
        if (noTransport())
            return false;

        return transports.get(0).hasDestination(target);
    }

    void add(Transport transport) {
        transports.add(transport);
    }

    @Override
    public String toString() {
        return transports.toString();
    }

    public String show() {
        if (noTransport())
            return "";

        return reverse().toString();
    }

    private Route reverse() {
        final Route reversed = new Route();
        for (int i = transports.size() - 1; i >= 0; i--) {
            reversed.add(transports.get(i));
        }
        return reversed;
    }

    public int hops() {
        if (noTransport())
            return 0;

        return transports.size() - 1;
    }

    private boolean noTransport() {
        return transports.isEmpty();
    }

    public boolean contains(Location location) {
        return transports.stream()
                .anyMatch(transport -> transport.contains(location));
    }

    public double cost() {
        return transports.stream()
                .reduce(0.0, (totalCost, transport) -> transport.addCostTo(totalCost), Double::sum);
    }
}
