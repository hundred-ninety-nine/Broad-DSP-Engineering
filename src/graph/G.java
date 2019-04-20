package graph;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * A generic Graph that represents a sparse graph using adjacency list.
 * It supports the <em>add Vertex</em> and <em>add Edge/em>
 * operations.
 * 
 *  @author Ivan Chang
 */
public class G<T> implements Iterable<T> {

	public interface EdgeLengthFunc<T> {
		int edgeLength(T v, T w);
	}

	private Map<T, Set<T>> verticesMap;
	private int edgesCount;
	private EdgeLengthFunc<T> edgeLength;

	public G() {
		verticesMap = new HashMap<>();
	}

	public int getNumVertices() {
		return verticesMap.size();
	}

	public int getNumEdges() {
		return edgesCount;
	}
	
	public Set<T> getNeighbors(T v) {
		validateVertex(v);
		return verticesMap.get(v);
	}

	private void validateVertex(T v) {
		if (!hasVertex(v))
			throw new IllegalArgumentException(v.toString() + " is not a vertex");
	}

	public int degree(T v) {
		validateVertex(v);
		return verticesMap.get(v).size();
	}

	public void addEdge(T v, T w) {
		if (!hasVertex(v))
			addVertex(v);
		if (!hasVertex(w))
			addVertex(w);
		if (!hasEdge(v, w))
			edgesCount++;
		verticesMap.get(v).add(w);
		verticesMap.get(w).add(v);
	}

	public void addVertex(T v) {
		if (!hasVertex(v))
			verticesMap.put(v, new HashSet<T>());
	}

	public boolean hasEdge(T v, T w) {
		validateVertex(v);
		validateVertex(w);
		return verticesMap.get(v).contains(w);
	}

	public boolean hasVertex(T v) {
		return verticesMap.containsKey(v);
	}

	public EdgeLengthFunc<T> getEdgeLength() {
		return edgeLength;
	}

	public void setEdgeLength(EdgeLengthFunc<T> edgeLength) {
		this.edgeLength = edgeLength;
	}

	@Override
	public Iterator<T> iterator() {
		return verticesMap.keySet().iterator();
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();

		for (T v : verticesMap.keySet()) {
			builder.append(v.toString() + ": ");
			for (T w : verticesMap.get(v)) {
				builder.append(w.toString() + " ");
			}
			builder.append("\n");
		}

		return builder.toString();
	}
}
