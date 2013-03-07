package io.ous.concurrentunit.execution;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.mockito.Mockito;

@RunWith(Parameterized.class)
public class ImmediateExecutionStrategyUnitTest {
	private final int executionCount;
	@Parameters(name="{index}: Count={0}")
	public static Collection<Object[]> getParameters() {
		Object[][] ret = new Object[][] {{0},{1},{100},{909},{5}};
		return Arrays.asList(ret);
	}
	
	public ImmediateExecutionStrategyUnitTest(int executionCount) {
		this.executionCount = executionCount;
	}
	
	@Test
	public void test() throws Exception {
		ImmediateExecutionStrategy strategy = new ImmediateExecutionStrategy(executionCount);
		ExecutorService executor = Mockito.mock(ExecutorService.class);
		
		Callable<?> call = Mockito.mock(Callable.class);
		Future<?> run = strategy.run(executor, call);
		Assert.assertTrue(run.isDone());
		Assert.assertFalse(run.isCancelled());
		
		Mockito.verify(executor,Mockito.times(executionCount)).submit(call);
//		Mockito.verify(call,Mockito.times(executionCount)).call();
		Mockito.verifyNoMoreInteractions(executor,call);
	}
}
