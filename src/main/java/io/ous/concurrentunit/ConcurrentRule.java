package io.ous.concurrentunit;

import io.ous.concurrentunit.execution.ExecutionStrategy;
import io.ous.concurrentunit.execution.ImmediateExecutionStrategy;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

/**
 * A concurrent rule executes tests concurrently given an executor service and execution strategy
 * @author Asafh
 *
 */
public class ConcurrentRule implements TestRule {
	private final ExecutorService executor;
	private final ExecutionStrategy strategy;

	public ConcurrentRule(int executionCount) {
		this(Executors.newCachedThreadPool(), new ImmediateExecutionStrategy(executionCount));
	}
	public ConcurrentRule(ExecutorService executor, int executionCount) {
		this(executor, new ImmediateExecutionStrategy(executionCount));
	}
    public ConcurrentRule(ExecutorService executor, ExecutionStrategy strategy) {
        this.executor = executor;
        this.strategy = strategy;
    }

    public Statement apply(Statement base, Description description) {
        return new ConcurrentStatement(base, executor, strategy);
    }

}
