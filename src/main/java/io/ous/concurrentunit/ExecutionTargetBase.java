package io.ous.concurrentunit;

public abstract class ExecutionTargetBase<V> implements
		ExecutionTarget<V> {

	public void before() throws Throwable {}
	public void after() throws Throwable {}
}
