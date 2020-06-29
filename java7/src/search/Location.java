package search;

import java.util.ArrayList;
import java.util.List;

public class Location {

    private final String name;
    private List<Transport> outBoundTransports = new ArrayList<Transport>();

    public Location(String name) {
        this.name = name;
    }

    public void addTransportTo(Location destination) {
        addTransportTo(destination, 0);
    }

    public void addTransportTo(Location destination, double cost) {
        outBoundTransports.add(new Transport(this, destination, cost));
    }

    public Routes findRoutesTo(Location target) {
        if (isAt(target))
            return new Routes().add(Route.VOID);

        return findRoutesTo(target, new ArrayList<Location>());
    }

    Routes findRoutesTo(Location target, List<Location> visited) {
        final Routes routes = new Routes();
        if (alreadyVisited(visited)) {
            return routes.add(Route.VOID);
        }

        if (isAt(target)) {
            return routes.add(new Route());
        }

        visited.add(this);
        for (Transport transport : outBoundTransports) {
            Routes newRoutes = transport.routesTo(target, visited)
                    .selectHavingDestination(target);
            routes.addAll(newRoutes);
        }
        visited.remove(this);
        return routes;
    }

    private boolean isAt(Location target) {
        return this.equals(target);
    }

    private boolean alreadyVisited(List<Location> visited) {
        return visited.contains(this);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + name.hashCode();
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Location other = (Location) obj;
        return name.equals(other.name);
    }

    @Override
    public String toString() {
        return name;
    }
}
