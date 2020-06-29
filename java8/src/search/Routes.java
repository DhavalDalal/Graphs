package search;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Routes {
    private static final Comparator<Route> byHopsAscending = (r1, r2) -> r1.hops() - r2.hops();
    private static final Comparator<Route> byCostAscending = (r1, r2) -> Double.compare(r1.cost(), r2.cost());
    private final List<Route> routes;

    Routes() {
        this(new ArrayList<>());
    }

    private Routes(List<Route> routes) {
        this.routes = routes;
    }

    public String show() {
        return routes.stream()
                .map(route -> route.show())
                .collect(Collectors.joining(""));
    }

    Routes add(Route route) {
        routes.add(route);
        return this;
    }

    void addAll(Routes newRoutes) {
        routes.addAll(newRoutes.routes);
    }

    void add(Transport transport) {
        for (Route route : routes) {
            route.add(transport);
        }
    }

    public Routes selectHavingDestination(Location target) {
        return new Routes(routes.stream()
                .filter(route -> route.hasDestination(target))
                .collect(Collectors.toList()));
    }

    public Route getRouteAt(int number) {
        if (noRoutes() || number > count() - 1) {
            return new Route();
        }
        return routes.get(number);
    }

    private boolean noRoutes() {
        return routes.isEmpty();
    }

    public Route first() {
        return getRouteAt(0);
    }

    public int count() {
        return routes.size();
    }

    @Override
    public String toString() {
        return show();
    }

    public Routes direct() {
        return hopsLessThanOrEqualTo(0);
    }

    public Routes hopsLessThanOrEqualTo(int howMany) {
        return new Routes(routes.stream()
                .filter(route -> route.hops() <= howMany)
                .collect(Collectors.toList()));
    }

    public boolean contains(String expectedRoute) {
        return routes.stream()
                .map(route -> route.show())
                .anyMatch(route -> route.contains(expectedRoute));
    }

    public Routes via(Location location) {
        return new Routes(routes.stream()
                .filter(route -> route.contains(location))
                .collect(Collectors.toList()));
    }

    private Routes sort(Comparator<Route> comparator) {
        List<Route> sorted = new ArrayList<>(routes);
        sorted.sort(comparator);
        return new Routes(sorted);
    }

    public Routes shortest() {
        Routes sorted = sort(byHopsAscending);
        final int minHops = sorted.getRouteAt(0).hops();
        return hopsLessThanOrEqualTo(minHops);
    }

    public Routes cheapest() {
        Routes sorted = sort(byCostAscending);
        final double minCost = sorted.getRouteAt(0).cost();
        return new Routes(sorted.routes.stream()
                .filter(route -> route.cost() == minCost)
                .collect(Collectors.toList()));
    }
}
