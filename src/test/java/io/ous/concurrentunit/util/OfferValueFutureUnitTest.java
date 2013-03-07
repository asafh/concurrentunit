package io.ous.concurrentunit.util;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.junit.Assert;
import org.junit.Test;

public class OfferValueFutureUnitTest {
	
	@Test
	public void test() throws InterruptedException, ExecutionException, TimeoutException {
		final Object unique = new Object();
		final OfferValueFuture<Object> future = new OfferValueFuture<Object>();
		Assert.assertFalse(future.isDone());
		
		Assert.assertFalse(future.isCancelled());
		Assert.assertFalse(future.cancel(false));
		Assert.assertFalse(future.cancel(true));
		Assert.assertFalse(future.isCancelled());
		
		Assert.assertFalse(future.isDone());
		
		try {
			future.get(2, TimeUnit.SECONDS);
			throw new IllegalStateException("Get shold have timed out");
		}
		catch(TimeoutException te) {
			
		}
		final Thread annoyMe = Thread.currentThread();
		new Thread(new Runnable() {
			public void run() {
				try {
					Thread.sleep(1000);
					annoyMe.interrupt();
					Thread.sleep(1000);
					future.offer(unique);
					Assert.assertEquals(unique,future.get());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
		try {
			future.get(10,TimeUnit.SECONDS);
			throw new IllegalStateException("Was supposed to be interrupted");
		}
		catch(InterruptedException ie) {
			;
		}
		Assert.assertEquals(unique, future.get(10,TimeUnit.SECONDS));
		
		
		Assert.assertEquals(unique,future.get());
		Assert.assertEquals(unique,future.get());
		Assert.assertEquals(unique,future.get());
	}
}
