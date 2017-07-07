package oracle.test;

import java.util.Queue;

/**
 * Provides waiting thread-safe access to a java.util.Queue instance.
 *
 * Requirements: - Usage of API from java.util.concurrent packageis prohibited.
 * - Limit the amount of additional consumed memory to O(1). - The
 * implementation is supposed to be acceptable for usage in a highly
 * multi-thread environment.
 *
 * Useful tips a.k.a. common pitfalls: - Please note that you do not need to
 * implement java.util.Queue. - Readiness to accept or provide elements is
 * solely dependent on the underlying queue. Any additional queue capacity
 * limitations break contract defined in the javadoc.
 */
public class BlockingQueue<E> {
	private Queue<E> queue;
	private final int maxCapacity;

	/**
	 * @param queue
	 *            The underlying "wrapped" queue.
	 */
	public BlockingQueue(Queue<E> queue) {
		this(queue, Integer.MAX_VALUE);
	}

	public BlockingQueue(Queue<E> queue, int maxCapacity) {
		this.queue = queue;
		this.maxCapacity = maxCapacity;
	}

	/**
	 * Inserts the specified element into the underlying queue, waiting if
	 * necessary for the underlying queue to be ready to accept new elements.
	 * 
	 * @param e
	 *            the element to insert.
	 * @throws InterruptedException
	 */
	public void push(E e) throws InterruptedException {
		while (queue.size() == maxCapacity) {
			synchronized (queue) {
				queue.wait();
			}
		}
		synchronized (queue) {
			queue.add(e);
			queue.notifyAll();
		}
	}

	/**
	 * Retrieves and removes the head of the underlying queue, waiting if
	 * necessary until it is capable of providing an element.
	 * 
	 * @return the retrieved element
	 * @throws InterruptedException
	 */
	public E pull() throws InterruptedException {
		while (queue.isEmpty()) {
			synchronized (queue) {
				queue.wait();
			}
		}
		synchronized (queue) {
			queue.notifyAll();
			E poll = queue.poll();
			return poll;
		}
	}
}
