package tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import graph.IndexMinPQ;

/**
 * Unit tests the {@code IndexMinPQ} data type.
 *
 * @author Ivan Chang
 */
class TestPQ {

	@Test
	void testPQ() {
		// insert a bunch of strings
		String[] strings = { "it", "was", "the", "best", "of", "times", "it", "was", "the", "worst" };
		int[] alphaNumericPriority = { 3, 0, 6, 4, 8, 2, 5, 7, 1, 9 }; // "best", "it", "it", "of", ...

		IndexMinPQ<String> pq = new IndexMinPQ<String>(strings.length);
		for (int i = 0; i < strings.length; i++) {
			pq.insert(i, strings[i]);
		}

		// test priority
		int it = 0;
		while (!pq.isEmpty()) {
			int i = pq.delMin();
			assertEquals(alphaNumericPriority[it++], i);
		}

		assertTrue(pq.isEmpty());

		// reinsert the same strings
		for (int i = 0; i < strings.length; i++) {
			pq.insert(i, strings[i]);
		}

		assertTrue(!pq.isEmpty());

		// test priority
		it = 0;
		while (!pq.isEmpty()) {
			int i = pq.delMin();
			assertEquals(alphaNumericPriority[it++], i);
		}

		while (!pq.isEmpty()) {
			pq.delMin();
		}

		assertTrue(pq.isEmpty());
	}

}
