package io.ous.concurrentunit.execution;

import io.ous.concurrentunit.execution.ScheduledExecutionStrategy.FixedMode;
import io.ous.concurrentunit.testutils.CartesianParameters;
import io.ous.concurrentunit.testutils.Stopwatch;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.junit.runners.model.RunnerScheduler;
import org.mockito.Mockito;

@RunWith(Parameterized.class)
public class ScheduledExecutionStrategyUnitTest {
	private final int executionCount;
	private long delay;
	private long period;
	private FixedMode mode;
	
	private ScheduledExecutionStrategy strategy;
	
	@Parameters(name="{index}: Count={0}, Delay={1}, Period={2}, Mode={3}")
	public static Collection<Object[]> getParameters() {
		Integer[] sizes = new Integer[] {0,1,100,50,5};
		Long[] delays = new Long[] {0l,50l,303l,500l,20l};
		Long[] periods = new Long[]{0l,30l,120l,400l,22l};
		FixedMode[] modes = FixedMode.values();
		return new CartesianParameters().add(sizes).add(delays).add(periods).add(modes).toParameters();
	}
	
	public ScheduledExecutionStrategyUnitTest(int count, long delay, long period, FixedMode mode) {
		executionCount = count;
		this.delay = delay;
		this.period = period;
		this.mode = mode;
	}
	
	@Before
	public void init() {
		strategy = new ScheduledExecutionStrategy(executionCount, delay, period, mode);
	}
	
	@Test
	public void test() throws Exception {
		System.out.println(strategy);
		ExecutorService executor = Executors.newCachedThreadPool();// Mockito.mock(ExecutorService.class);
//		Callable<?> call = Mockito.mock(Callable.class);
		final AtomicInteger counter = new AtomicInteger(-1);
		final Stopwatch watch = new Stopwatch();
		Future<List<Future<Integer>>> run = strategy.run(executor, new Callable<Integer>() {
			public Integer call() throws Exception {
				watch.mark();
				return counter.incrementAndGet();
			}
		});
		List<Future<Integer>> list = run.get();
		watch.end();
		
		System.out.println(list);
		System.out.println(watch.getElapsed());
		System.out.println(watch.getLaps());
		/*ImmediateExecutionStrategy strategy = new ImmediateExecutionStrategy(executionCount);
		ExecutorService executor = Mockito.mock(ExecutorService.class);
		Callable<?> call = Mockito.mock(Callable.class);
		strategy.run(executor, call);
		Mockito.verify(executor,Mockito.times(executionCount)).submit(call);
//		Mockito.verify(call,Mockito.times(executionCount)).call();
		Mockito.verifyNoMoreInteractions(executor,call);*/
	}
}
