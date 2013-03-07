package io.ous.concurrentunit.util;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.junit.Assert;
import org.junit.Test;

public class ImmediateFutureUnitTest {
	@Test
	public void test() throws InterruptedException, ExecutionException, TimeoutException {
		Object unique = new Object();
		ImmediateFuture<Object> future = new ImmediateFuture<Object>(unique);
		Assert.assertTrue(future.isDone());
		
		Assert.assertFalse(future.isCancelled());
		Assert.assertFalse(future.cancel(false));
		Assert.assertFalse(future.cancel(true));
		Assert.assertFalse(future.isCancelled());
		
		Assert.assertEquals(unique,future.get());
		Assert.assertEquals(unique,future.get());
		Assert.assertEquals(unique,future.get(5, TimeUnit.SECONDS));
		Assert.assertEquals(unique,future.get());
	}
}
