package search;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Routes {
    private List<Route> routes = new ArrayList<>();

    private static interface Criterion {
        public boolean isSatisfiedBy(Route route);
    }

    ;
    private static final Comparator<Route> byHopsAscending = new Comparator<Route>() {
        @Override
        public int compare(Route r1, Route r2) {
            return r1.hops() - r2.hops();
        }
    };

    private static final Comparator<Route> byCostAscending = new Comparator<Route>() {
        @Override
        public int compare(Route r1, Route r2) {
            return Double.compare(r1.cost(), r2.cost());
        }
    };

    public String show() {
        StringBuilder allRoutes = new StringBuilder();
        for (Route route : routes) {
            allRoutes.append(route.show());
        }
        return allRoutes.toString();
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
        return select(new Criterion() {
            @Override
            public boolean isSatisfiedBy(Route route) {
                return route.hasDestination(target);
            }
        });
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
        return select(new Criterion() {
            @Override
            public boolean isSatisfiedBy(Route route) {
                return route.hops() <= howMany;
            }
        });
    }

    private Routes select(Criterion criterion) {
        final Routes selected = new Routes();
        for (Route route : routes) {
            if (criterion.isSatisfiedBy(route))
                selected.add(route);
        }
        return selected;
    }

    public boolean contains(String expectedRoute) {
        List<String> allRoutes = new ArrayList<>();
        for (Route route : routes) {
            allRoutes.add(route.show());
        }
        return allRoutes.contains(expectedRoute);
    }

    public Routes via(Location location) {
        return select(new Criterion() {
            @Override
            public boolean isSatisfiedBy(Route route) {
                return route.contains(location);
            }
        });
    }

    private Routes sort(Comparator<Route> comparator) {
        List<Route> sorted = new ArrayList<>(routes);
        Collections.sort(sorted, comparator);
        Routes routes = new Routes();
        routes.routes = sorted;
        return routes;
    }

    public Routes shortest() {
        Routes sorted = sort(byHopsAscending);
        final int minHops = sorted.getRouteAt(0).hops();
        return hopsLessThanOrEqualTo(minHops);
    }

    public Routes cheapest() {
        Routes sorted = sort(byCostAscending);
        final double minCost = sorted.getRouteAt(0).cost();

        return select(new Criterion() {
            @Override
            public boolean isSatisfiedBy(Route route) {
                return route.cost() == minCost;
            }
        });
    }
}
