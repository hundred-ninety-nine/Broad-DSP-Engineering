package api.mbta.com;

import java.util.ArrayList;
import java.util.List;

/**
 * A Subway <em>Route</em> representation.
 * 
 * @author Ivan Chang
 */
public class Route {
	private String id;
	private String long_name;
	private List<Stop> stops;
	// private int numStops;
	
	public Route(String id, String long_name) {
		super();
		this.id = id;
		this.long_name = long_name;
		//this.numStops = 0;
		this.stops = new ArrayList<Stop>();
	}
	
	/**
	 * @return the id - this is the unique mbta identifier of a subway route
	 */
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	/**
	 * @return the long name - this is the unique mbta long name of a subway route
	 */
	public String getLong_name() {
		return long_name;
	}

	public void setLong_name(String long_name) {
		this.long_name = long_name;
	}
	
	/**
	 * @return the number of stops this route contains.
	 */
	public int getNumStops() {
		return stops.size();
		// return numStops;
	}

	//private void setNumStops(int numStops) {
	//	this.numStops = numStops;
	//}
	
	/**
	 * @return the list of stops this route contains.
	 */
	public List<Stop> getStops() {
		return stops;
	}

	public void setStops(List<Stop> stops) {
		this.stops = stops;
	}

	/**
	 * Add a stop to which this subway route connects to.
	 * @param <em>stop</em> is a valid subway <em>Stop</em> object.
	 */
	public void addStop(Stop stop) {
		stops.add(stop);
	}
	
	/**
	 * Add a bunch of stops to which this subway route connects to.
	 * @param <em>stops</em> is a list of valid subway <em>Stop</em> objects.
	 */
	public void addStops(List<Stop> stops) {
		this.stops.addAll(stops);
	}
}
