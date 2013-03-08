package io.ous.concurrentunit;

import io.ous.concurrentunit.execution.ExecutionStrategy;

import java.util.concurrent.ExecutorService;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

/**
 * This rule causes Statements to be wrapped in a ConcurrentStatement instance,
 * executing them according to the execution strategy using the given executor service
 * @author Asafh
 *
 */
public class ConcurrentRule extends ExecutionTargetRunner implements TestRule {

    public ConcurrentRule(ExecutionStrategy strategy) {
		super(strategy);
	}

	public ConcurrentRule(ExecutorService executor, ExecutionStrategy strategy) {
		super(executor, strategy);
	}

	public ConcurrentRule(ExecutorService executor, int executionCount) {
		super(executor, executionCount);
	}

	public ConcurrentRule(int executionCount) {
		super(executionCount);
	}

	public Statement apply(Statement base, Description description) {
        return new ConcurrentStatement(base, this);
    }

}
