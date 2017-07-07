package oracle.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

/**
 * Provides sufficient test coverage for oracle.test.IteratorFlattener class.
 */
public class IteratorFlattenerTest {
	@Test
	public void testStringsFlattener() {
		List<String> words1 = Arrays.asList("val1", "val2", "val3");
		List<String> words2 = Arrays.asList("val4", "val5");
		List<String> words3 = Arrays.asList("val5", "val6", "val7");
		List<String> expectedList = Arrays.asList("val1", "val2", "val3", "val4", "val5", "val5", "val6", "val7");
		List<Iterator<String>> words = Arrays.asList(words1.iterator(), null, words2.iterator(), words3.iterator());
		IteratorFlattener<String> flat = new IteratorFlattener<>(words.iterator());
		List<String> flatList = new ArrayList<String>();
		while (flat.hasNext()) {
			flatList.add(flat.next());
		}
		Assert.assertEquals(expectedList, flatList);
	}

	@Test
	public void testRemoveItemFromFlattener() {
		List<String> words1 = new ArrayList<String>(Arrays.asList("val1", "val2", "val3"));
		List<String> words2 = new ArrayList<String>(Arrays.asList("val4", "val5"));
		List<String> words3 = new ArrayList<String>(Arrays.asList("val5", "val6", "val7"));
		List<Iterator<String>> words = Arrays.asList(words1.iterator(), null, words2.iterator(), words3.iterator());
		IteratorFlattener<String> flat = new IteratorFlattener<>(words.iterator());
		while (flat.hasNext()) {
			flat.next();
			flat.remove();
		}

		Assert.assertEquals(0, words1.size());
		Assert.assertEquals(0, words2.size());
		Assert.assertEquals(0, words3.size());
	}

	@Test
	public void testIntegersFlattener() {
		List<Integer> numList1 = Arrays.asList(1, 2, 3);
		List<Integer> numList2 = Arrays.asList(5, 6, 7, 8);
		List<Integer> numList3 = Arrays.asList();
		List<Integer> numList4 = Arrays.asList(10, 20);
		List<Integer> expectedList = Arrays.asList(1, 2, 3, 5, 6, 7, 8, 10, 20);
		List<Iterator<Integer>> numListItr = Arrays.asList(numList1.iterator(), numList2.iterator(),
				numList3.iterator(), numList4.iterator());
		IteratorFlattener<Integer> flat = new IteratorFlattener<>(numListItr.iterator());
		List<Integer> flatList = new ArrayList<Integer>();
		while (flat.hasNext()) {
			flatList.add(flat.next());
		}
		Assert.assertEquals(expectedList, flatList);
	}
}
