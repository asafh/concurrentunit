package io.ous.concurrentunit;

import java.util.concurrent.Callable;

/**
 * A task to execute multiple times (concurrently), with setup and teardown code run before and after all executions are invoked.
 * @author Asafh
 *
 */
public interface ExecutionTarget<V> extends Callable<V> {
	public void before() throws Throwable;
	public void after() throws Throwable;
}
