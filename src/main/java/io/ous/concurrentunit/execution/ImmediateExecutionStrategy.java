package io.ous.concurrentunit.execution;

import io.ous.concurrentunit.util.ImmediateFuture;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class ImmediateExecutionStrategy implements ExecutionStrategy {

	private final int executionCount;

	/**
	 * Constructs a strategy that will cause <code>executionCount</code> invocations to be submitted immediately
	 * @param executionCount
	 */
	public ImmediateExecutionStrategy(int executionCount) {
		this.executionCount = executionCount;
	}
	
	/**
	 * Submits all of the executions at once
	 */
	public<V> Future<List<Future<V>>> run(ExecutorService service, Callable<V> target) {
		List<Future<V>> ret = new ArrayList<Future<V>>(executionCount);
		for(int i = 0; i < executionCount; ++i) {
			Future<V> submit = service.submit(target);
			ret.add(submit);
		}
		return new ImmediateFuture<List<Future<V>>>(ret);
	}
}
