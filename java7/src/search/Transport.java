package search;

import java.util.List;

public class Transport {

    private final Location to;
    private final Location from;
    private double cost;

    public Transport(Location from, Location to, double cost) {
        this.from = from;
        this.to = to;
        this.cost = cost;
    }

    public boolean hasDestination(Location target) {
        return to.equals(target);
    }

    @Override
    public String toString() {
        return String.format("%s->%s", from, to);
    }

    public Routes routesTo(Location target, List<Location> visited) {
        Routes routes = to.findRoutesTo(target, visited);
        routes.add(this);
        return routes;
    }

    public boolean contains(Location location) {
        return hasOrigin(location) || hasDestination(location);
    }

    private boolean hasOrigin(Location location) {
        return from.equals(location);
    }

    public double addCostTo(double cost) {
        return this.cost + cost;
    }
}
