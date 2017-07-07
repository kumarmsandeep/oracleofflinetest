package oracle.test;

import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

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
public class BlockingQueue3<E> {
	private Queue<E> queue;
	private final Object mPushLock = new Object();
	private final Object mPullLock = new Object();
	private final AtomicInteger counter = new AtomicInteger(0);

	/**
	 * @param queue
	 *            The underlying "wrapped" queue.
	 */
	public BlockingQueue3(Queue<E> queue) {
		this.queue = queue;
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
		synchronized (mPushLock) {
			boolean wasEmpty = isEmpty();
			queue.add(e);
			incrementCount();
			synchronized (mPullLock) {
				if (wasEmpty) {
					mPullLock.notify();
				}
			}
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
		E item = null;
		while (isEmpty()) {
			synchronized (mPullLock) {
				try {
					mPullLock.wait();
				} catch (InterruptedException e) {
					
				}
			}
		}
		if (!isEmpty()) {
			item = queue.poll();
			System.out.println(item);
			if(item != null) {
				decrementCount();
			}
		}
		return item;
	}

	private boolean isEmpty() {
		return getCount() == 0;
	}

	private int getCount() {
		return counter.get();
	}

	private void incrementCount() {
		counter.incrementAndGet();
	}

	private void decrementCount() {
		counter.decrementAndGet();
	}

	public static void main(String[] args) throws InterruptedException {
		ConcurrentLinkedQueue<Integer> queue = new ConcurrentLinkedQueue<>();
		final BlockingQueue3<Integer> b = new BlockingQueue3<>(queue);
		// System.out.println("put(11)");
		// b.push(11);
		// System.out.println("put(12)");
		// b.push(12);
		Thread t = new Thread() {
			public void run() {
				try {
					System.out.println("take() > " + b.pull());
					System.out.println("take() > " + b.pull());
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			};
		};
		t.start();
		TimeUnit.SECONDS.sleep(5);
		b.push(10);
		TimeUnit.SECONDS.sleep(5);
		b.push(20);
	}
}
