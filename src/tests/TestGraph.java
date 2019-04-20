package tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import graph.G;

/**
 * Unit tests the {@code G} data type.
 *
 * @author Ivan Chang
 */
class TestGraph {

	@Test
	void testG() {
		G<String> g = new G<>();

		g.addEdge("A", "B");
		g.addEdge("A", "C");
		g.addEdge("C", "D");
		g.addEdge("D", "E");
		g.addEdge("D", "G");
		g.addEdge("E", "G");
		g.addVertex("H");

		assertTrue(g.hasVertex("A"));
		assertTrue(g.hasVertex("B"));
		assertTrue(g.hasVertex("C"));
		assertTrue(g.hasVertex("D"));
		assertTrue(g.hasVertex("D"));
		assertFalse(g.hasVertex("F"));
		assertTrue(g.hasVertex("G"));
		assertTrue(g.hasVertex("H"));
		assertTrue(g.hasEdge("A", "B"));
		assertTrue(g.hasEdge("A", "C"));
		assertTrue(g.hasEdge("B", "A"));
		assertEquals(7, g.getNumVertices());
		assertEquals(6, g.getNumEdges());
	}

}
