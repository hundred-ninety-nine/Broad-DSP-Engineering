package app;

import api.mbta.com.Stop;
import api.mbta.com.SubwayGraph;

/**
 * This is the main demo.
 * 
 * @author Ivan Chang
 */
public class demo {

	public static void main(String[] args) {
		SubwayGraph g = new SubwayGraph();

		// Question 1: All type 0, 1 "subway" routes.
		// We use filter[type]=0,1 to limit our search.
		System.out.println("Question 1");
		System.out.println("All subway routes");
		System.out.println("=================");
		g.printRouteLongNames();
		System.out.println();

		// Question 2: Route(s) with fewest, most number of stops. Stops with two or more connecting routes.
		System.out.println("Question 2");
		System.out.println("Route(s) with fewest number of stops (Long name, number of stops)");
		System.out.println("=================================================================");
		g.printRouteWithLeastStops();
		System.out.println();

		System.out.println("Route(s) with the most number of stops (Long name, number of stops)");
		System.out.println("===================================================================");
		g.printRouteWithMostStops();
		System.out.println();

		System.out.println("Stop(s) with two or more connecting routes (Stop name, [list of route names])");
		System.out.println("=============================================================================");
		g.printStopsWithTwoOrMoreConnectedRoutes();
		System.out.println();

		// We have a sparse graph as |E| ~ |V|
		System.out.println("Number of vertices |V| = " + g.getNumVertices());
		System.out.println("Number of edges |E| = " + g.getNumEdges());
		System.out.println("We have a sparse graph as |E| ~ |V|!");
		System.out.println();

		// Question 3: Find a feasible rail path connecting any given two stops.
		// In this demo, we consider two examples: 
		// The first example finds a path connecting Davis and Kendall Sq/MIT
		// The second example finds a path connecting Ashmont and Arlington
		// We present the full path in the form
		//    Beginning Subway stop name [a set of connecting routes]
		// -> Next Subway stop name [a set of connecting routes]
		// -> ......
		// -> Destination Subway stop name [a set of connecting routes]
		// It is clear we could compute the union of all the intersections between neighboring sets of connecting routes
		// to obtain the set of valid connection routes.
		Stop Davis = g.matchStopName("Davis");
		Stop Kendall = g.matchStopName("Kendall");
		Stop Ashmont = g.matchStopName("Ashmont");
		Stop Arlington = g.matchStopName("Arlington");

		// Run Dijkstra algorithm to find path between Davis and Kendall
		Stop davis_mit = g.Dijkstra(Davis, Kendall);
		if (davis_mit != null) {
			g.printPath(davis_mit);
		}

		System.out.println();
		
		// Run Dijkstra algorithm to find path between Ashmont and Arlington
		Stop ash_arl = g.Dijkstra(Ashmont, Arlington);
		if (ash_arl != null) {
			g.printPath(ash_arl);
		}
	}

}
