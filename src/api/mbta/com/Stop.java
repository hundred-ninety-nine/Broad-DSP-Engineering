package api.mbta.com;

import java.util.ArrayList;
import java.util.List;

/**
 * A Subway <em>Stop</em> representation.
 * 
 * @author Ivan Chang
 */
public class Stop implements Comparable<Stop> {
	
	private String id;
	private String name;
	private List<Route> connectsTo;
	private int distance;
	private Stop prev;

	public Stop() {
	}

	public Stop(String id, String name) {
		super();
		this.id = id;
		this.name = name;
		this.connectsTo = new ArrayList<Route>();
	}

	public Stop(Stop stop) {
		super();
		this.id = stop.id;
		this.name = stop.name;
		this.connectsTo = new ArrayList<Route>(stop.connectsTo);
		this.distance = stop.distance;
		this.prev = stop.prev;
	}

	/**
	 * @return the id - this is the unique mbta identifier of a subway stop
	 */
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the name - this is the unique mbta name of a subway stop
	 */
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setConnectsTo(List<Route> connectsTo) {
		this.connectsTo = connectsTo;
	}

	/**
	 * @return the connectsTo - this is a list of subway routes connected through this subway stop
	 */
	public List<Route> getConnectsTo() {
		return connectsTo;
	}

	/**
	 * @return the distance - keep track of the distance between this subway stop and any other subway stop (e.g. a starting subway stop)
	 */
	public int getDistance() {
		return distance;
	}

	public void setDistance(int distance) {
		this.distance = distance;
	}

	/**
	 * @return the prev - keep track of the previous subway stop along the same trip
	 */
	public Stop getPrev() {
		return prev;
	}

	public void setPrev(Stop prev) {
		this.prev = prev;
	}

	/**
	 * Add a route to which this subway stop connects to.
	 * @param <em>route</em> is a valid subway <em>Route</em> object.
	 */
	public void addConnectsTo(Route route) {
		connectsTo.add(route);
	}

	/**
	 * Returns number of subway routes this subway stop connects to.
	 *
	 * @return a list of subway routes names.
	 */
	public int getNumOfConnections() {
		return connectsTo.size();
	}

	/**
	 * Returns distance between current node and any other stop s.
	 *
	 * @return a distance measure (integer only).
	 */
	@Override
	public int compareTo(Stop s) {
		return distance - s.distance;
	}
}
