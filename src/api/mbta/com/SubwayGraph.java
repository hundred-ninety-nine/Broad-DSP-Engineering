package api.mbta.com;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import graph.G;
import graph.IndexMinPQ;

/**
 * A Graph such that each vertex represents a unique subway stop
 * and each edge represents a unique pair of consecutive subway stops
 * connected by any rail line.
 * 
 * @author Ivan Chang
 */
public class SubwayGraph extends G<Stop> {
	private List<Route> routes;

	/**
	 * Initialize the Subway Graph by loading data from Requests,
	 * which contains HTTP methods to GET data from MBTA api.
	 */
	public SubwayGraph() {
		Requests requests = new Requests("https://api-v3.mbta.com");
		routes = requests.mbtaStops(requests.subwayRoutes());
		// Add vertices to graph
		routes.forEach(route -> {
			route.getStops().forEach(v -> {
				this.addVertex(v);
			});
		});
		// Add edges to graph.
		// Note that the graph is undirected and hence 
		// both (v, w) and (w, v) are valid edges.
		routes.forEach(route -> {
			List<Stop> stops = route.getStops();
			int i = 0;
			while (i < stops.size() - 1) {
				Stop v = stops.get(i);
				Stop w = stops.get(i + 1);
				this.addEdge(v, w);
				this.addEdge(w, v);
				i++;
			}
		});

		// Define edge length function to return 1 for 
		// any edges.
		G.EdgeLengthFunc<Stop> edgeLength = (v, w) -> 1;
		this.setEdgeLength(edgeLength);
	}

	/**
	 * Returns a list of Subway Route objects.
	 *
	 * @return a list of Subway Routes.
	 */
	public List<Route> getRoutes() {
		return routes;
	}

	/**
	 * Returns a list of names representing the long names of Subway routes.
	 *
	 * @return a list of subway routes names.
	 */
	public List<String> getRouteLongNames() {
		return routes.stream().map(route -> route.getLong_name()).collect(Collectors.toList());
	}

	/**
	 * Print a list of long names of Subway routes.
	 *
	 */
	public void printRouteLongNames() {
		List<String> longnames = getRouteLongNames();
		StdOut(",", longnames.toArray());
	}

	public void setRoutes(List<Route> routes) {
		this.routes = routes;
	}


	/**
	 * Print a list of route names with least number of stops.
	 *
	 */
	public void printRouteWithLeastStops() {
		List<Route> routes = sortByNumOfStopsIncreasing();
		if (!routes.isEmpty()) {
			String long_name = routes.get(0).getLong_name();
			int numStops = routes.get(0).getNumStops();
			StdOut(",", long_name, numStops);
			int i = 1;
			while (i < routes.size() - 1) {
				if (numStops == routes.get(i).getNumStops()) {
					StdOut(",", routes.get(i).getLong_name(), numStops);
				} else
					break;
				i++;
			}
		}
	}

	/**
	 * Print a list of route names with the most stops.
	 *
	 */
	public void printRouteWithMostStops() {
		List<Route> routes = sortByNumOfStopsDecreasing();
		if (!routes.isEmpty()) {
			String long_name = routes.get(0).getLong_name();
			int numStops = routes.get(0).getNumStops();
			StdOut(",", long_name, numStops);
			int i = 1;
			while (i < routes.size() - 1) {
				if (numStops == routes.get(i).getNumStops()) {
					StdOut(",", routes.get(i).getLong_name(), numStops);
				} else
					break;
				i++;
			}
		}
	}

	/**
	 * Print a list of stops with two or more connected routes.
	 *
	 */
	public void printStopsWithTwoOrMoreConnectedRoutes() {
		List<Stop> stops = sortByConnectsToDecreasing();
		if (!stops.isEmpty()) {
			int i = 0;
			while (i < stops.size()) {
				Stop stop = stops.get(i);
				List<Route> routes = stop.getConnectsTo();
				if (routes.size() <= 1)
					break;
				List<String> long_names = routes.stream().map(route -> route.getLong_name())
						.collect(Collectors.toList());
				StdOut(",", stop.getName(), long_names);
				i++;
			}
		}
	}


	/**
	 * Returns a copy of <em>Route</em> objects sorted by increasing number of stops.
	 *
	 * @return a list of <em>Route</em> objects sorted by increasing number of stops.
	 */
	private List<Route> sortByNumOfStopsIncreasing() {
		List<Route> copy = new ArrayList<Route>(routes);
		Collections.sort(copy, (r1, r2) -> r1.getNumStops() - r2.getNumStops());
		return copy;
	}

	/**
	 * Returns a copy of <em>Route</em> objects sorted by decreasing number of stops.
	 *
	 * @return a list of <em>Route</em> objects sorted by decreasing number of stops.
	 */
	private List<Route> sortByNumOfStopsDecreasing() {
		List<Route> copy = new ArrayList<Route>(routes);
		Collections.sort(copy, (r1, r2) -> r2.getNumStops() - r1.getNumStops());
		return copy;
	}

	/**
	 * Returns a copy of <em>Stop</em> objects sorted by decreasing number of connecting routes.
	 *
	 * @return a copy of <em>Stop</em> objects sorted by decreasing number of connecting routes.
	 */
	private  List<Stop> sortByConnectsToDecreasing() {
		List<Route> copy = new ArrayList<Route>(routes);
		Set<Stop> setOfStops = new HashSet<Stop>();
		for (Route route : copy) {
			setOfStops.addAll(route.getStops());
		}
		List<Stop> stops = new ArrayList<Stop>(setOfStops);
		Collections.sort(stops, (s1, s2) -> s2.getNumOfConnections() - s1.getNumOfConnections());
		return stops;
	}

	/**
	 * Helper function that matches the name of a subway stop.
	 * @param name is the name of subway stop (partial match OK)
	 * @return a subway <em>Stop</em> object matching the given input name.
	 */
	public Stop matchStopName(String name) {
		name = Objects.toString(name, "");
		if (name.length() == 0)
			return null;
		Iterator<Stop> it = iterator();
		// int i = 0;
		while (it.hasNext()) {
			Stop s = it.next();
			if (s.getName().toLowerCase().contains(name.toLowerCase())) {
				// System.out.println(s.getName() + " Key Index " + i);
				return s;
			}
			// i++;
		}
		return null;
	}

	/**
	 * Dijkstra algorithm that finds the shortest path between two subway stops on a given Subway Graph.
	 * @param start is the beginning subway stop
	 * @param goal is the destination subway stop
	 * @return a Subway <em>Stop</em> object with <em>prev</em> populated for reconstructing the path through recursive backtracking, returns a <em>null</em> if path not exists.
	 */
	public Stop Dijkstra(Stop start, Stop goal) {
		List<Stop> allStops = new ArrayList<Stop>();
		iterator().forEachRemaining(s -> {
			if (s == start) {
				s.setDistance(0);
			} else {
				s.setDistance(Integer.MAX_VALUE / 2);
			}
			s.setPrev(null);
			allStops.add(s);
		});

		IndexMinPQ<Stop> pq = new IndexMinPQ<Stop>(getNumVertices());
		for (int i = 0; i < allStops.size(); i++) {
			pq.insert(i, allStops.get(i));
		}

		while (!pq.isEmpty()) {
			int min = pq.delMin();
			Stop u = allStops.get(min);
			if (u == goal) {
				System.out.println("Found path from " + start.getName() + " -> " + goal.getName());
				return u;
			}
			Set<Stop> neighbors = getNeighbors(u);
			neighbors.forEach(v -> {
				if (v.getDistance() > u.getDistance() + getEdgeLength().edgeLength(u, v)) {
					v.setDistance(u.getDistance() + getEdgeLength().edgeLength(u, v));
					v.setPrev(u);
					pq.decreaseKey(allStops.indexOf(v), v, true);
				} else {

				}
			});
		}

		System.out.println("Failed to find path from " + start.getName() + " -> " + goal.getName());
		return null;
	}

	/**
	 * Helper method for printing path.
	 * @param path is the destination stop with backtracking information.
	 */
	public void printPath(Stop path) {
		printPath_aux(path);
		System.out.println();
	}

	/**
	 * Helper method for printing path.
	 * @param path is the destination stop with backtracking information.
	 */
	private void printPath_aux(Stop path) {
		if (path.getPrev() != null) {
			printPath_aux(path.getPrev());
		}
		System.out.print(" -> " + path.getName() + " "
				+ path.getConnectsTo().stream().map(r -> r.getLong_name()).collect(Collectors.toList()).toString());
	}

	private void StdOut(String sep, Object... x) {
		if (x.length > 0) {
			System.out.print(x[0]);
			int i = x.length - 1;
			while (i > 0) {
				System.out.print(sep + " " + x[x.length - i]);
				i--;
			}

		}
		System.out.println();
	}
}
