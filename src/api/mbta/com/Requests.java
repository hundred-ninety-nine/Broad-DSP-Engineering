package api.mbta.com;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.json.JSONArray;
import org.json.JSONObject;

import client.HttpClient;

/**
 * A <em>Requests</em> class that inherits the GET protocol from Base HttpClient.
 * 
 *  @author Ivan Chang
 */
public class Requests extends HttpClient {

	// Keep track of subway stops discovered, avoid duplicates.
	// Safe for concurrent use
	static Map<String, Stop> visited = new ConcurrentHashMap<String, Stop>();

	public Requests(String url) {
		super(url);
	}

	/**
	 * Returns a list of MBTA Subway <em>Route</em> objects
	 * 
	 */
	public List<Route> subwayRoutes() {
		List<Route> routes = new ArrayList<Route>();
		try {
			//HttpClient httpClient = new HttpClient(apiUrl);
			String filter = "filter[type]";
			String subway = "0,1";
			Map<String, String> qp = new HashMap<String, String>();
			qp.put(filter, subway);

			String response = GET("/routes", qp);

			JSONObject json = new JSONObject(response.toString());
			JSONArray routesData = json.getJSONArray("data");

			routes = StreamSupport.stream(routesData.spliterator(), false)
					.map(route -> new Route(((JSONObject) route).getString("id"),
							((JSONObject) route).getJSONObject("attributes").getString("long_name")))
					.collect(Collectors.toList());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return routes;
	}

	/**
	 * Returns a MBTA Subway <em>Route</em> object including stops
	 * 
	 * @param route is a valid <em>Route</em> object
	 */
	public Route mbtaStops(Route route) {
		List<Stop> stops = new ArrayList<Stop>();
		try {
			//HttpClient httpClient = new HttpClient(apiUrl);
			String filter = "filter[route]";
			String value = route.getId();
			Map<String, String> qp = new HashMap<String, String>();
			qp.put(filter, value);

			String response = GET("/stops", qp);

			JSONObject json = new JSONObject(response.toString());
			JSONArray stopsData = json.getJSONArray("data");

			stops = StreamSupport.stream(stopsData.spliterator(), true).map(stop -> {
				String id = ((JSONObject) stop).getString("id");
				if (!visited.containsKey(id)) {
					String name = ((JSONObject) stop).getJSONObject("attributes").isNull("name") ? ""
							: ((JSONObject) stop).getJSONObject("attributes").getString("name");
					visited.put(id, new Stop(id, name));
				}
				visited.get(id).addConnectsTo(route);
				return visited.get(id);
			}).collect(Collectors.toList());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		route.setStops(stops);
		return route;
	}

	/**
	 * Returns multiple MBTA Subway <em>Route</em> objects including stops
	 * 
	 * @param routes is a valid list <em>Route</em> objects
	 */
	public List<Route> mbtaStops(List<Route> routes) {
		List<Route> includeStops = StreamSupport.stream(routes.spliterator(), true).map(route -> mbtaStops(route))
				.collect(Collectors.toList());
		return includeStops;
	}
}
