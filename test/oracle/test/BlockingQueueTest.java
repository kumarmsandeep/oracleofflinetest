package oracle.test;

/**
 * Provides sufficient test coverage for oracle.test.BlockingQueue class.
 */
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class BlockingQueueTest {

	private static final LinkedList<Integer> QUEUE = new LinkedList<Integer>();
	private static int NUM_ENTRIES = 100000;
	private static int NUM_THREADS = 30;
	private static final AtomicInteger readCount = new AtomicInteger(0);
	private static final BlockingQueue<Integer> blockingQueue = new BlockingQueue<>(QUEUE);

	public static void main(String args[]) throws InterruptedException {
		Thread producer = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					for (int i = 0; i < NUM_ENTRIES; i++) {
						blockingQueue.push(new Integer(i));
						System.out.println("Added : " + i);						
					}
				} catch (InterruptedException e) {
					throw new RuntimeException(e);
				}
			}
		});

		List<Thread> consumers = new ArrayList<Thread>();
		for (int i = 0; i < NUM_THREADS; i++) {
			consumers.add(new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						while (true) {
							Integer i = blockingQueue.pull();							
							if(i != null) {
								TimeUnit.MILLISECONDS.sleep(i % 50);
								readCount.incrementAndGet();
								System.out.println("Removed : " + i +": Count : " + readCount.get());
							} else {
								System.err.println("Null should not be returned");
							}
							if(readCount.get() >= NUM_ENTRIES) {
								break;
							}
						}
					} catch (InterruptedException e) {
						System.err.println("read exception");
					}
				}
			}));
		}

		long begin = System.currentTimeMillis();
		producer.start();
		for (Thread r : consumers) {
			r.start();
		}
		producer.join();		
		for (Thread r : consumers) {
			r.join();
		}
		System.out.println("DONE!");
		System.out.println("Diff : " + (NUM_ENTRIES - readCount.get()));
		System.out.println("Number of entries read successfully : " + readCount.get());
		long end = System.currentTimeMillis();
		System.out.println("elapsed time is " + ((end - begin) / 1000.0) + " seconds");
	}
}
