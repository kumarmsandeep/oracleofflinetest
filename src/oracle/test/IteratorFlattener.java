package oracle.test;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * "Flattens" nested sub-iterators into an iterator in a depth-first manner.
 *
 * Requirements: - Limit the amount of additional consumed memory to O(1).
 *
 * Example: an iterator {{1,2},{3},{4,5}} which has three sub-iterators with 2,
 * 1 and 2 elements correspondingly, presents values in this order: {1, 2, 3, 4,
 * 5}
 */
public class IteratorFlattener<E> implements Iterator<E> {
	private final Iterator<Iterator<E>> rootIterator;
	private Iterator<E> presentIterator;

	public IteratorFlattener(Iterator<Iterator<E>> root) {
		if (root == null) {
			throw new IllegalArgumentException("root iterator should not be null");
		}
		this.rootIterator = root;
	}

	private Iterator<E> getNextAvailableIterator() {
		if (presentIterator != null && presentIterator.hasNext()) {
			return presentIterator;
		}
		while (rootIterator.hasNext()) {
			presentIterator = rootIterator.next();
			if (presentIterator != null && presentIterator.hasNext()) {
				return presentIterator;
			}
		}
		return null;
	}

	/**
	 * Returns {@code true} if the iteration has more elements.
	 *
	 * @return {@code true} if the iteration has more elements
	 */
	public boolean hasNext() {
		return getNextAvailableIterator() != null;
	}

	/**
	 * Returns the next element in the iteration.
	 *
	 * @return the next element in the iteration
	 * @throws NoSuchElementException
	 *             if the iteration has no more elements
	 */
	public E next() {		
		return presentIterator.next();
	}

	/**
	 * Removes from the underlying collection the last element returned by this
	 * iterator (optional operation). This method can be called only once per
	 * call to {@link #next}.
	 *
	 * @throws IllegalStateException
	 *             if the {@code next} method has not yet been called, or the
	 *             {@code remove} method has already been called after the last
	 *             call to the {@code next} method
	 */
	public void remove() {
		if (presentIterator != null) {
			presentIterator.remove();
		}
	}
}
