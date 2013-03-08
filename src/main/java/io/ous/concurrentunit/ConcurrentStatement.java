package io.ous.concurrentunit;

import io.ous.concurrentunit.util.Throwables;

import org.junit.runners.model.Statement;

/**
 * Wraps an existing Statement executing it using the given ExecutionTargetRunner
 * @author Asafh
 *
 */
public class ConcurrentStatement extends Statement implements ExecutionTarget<Void> {
    private final Statement fOriginalStatement;

	private final ExecutionTargetRunner runner;

    public ConcurrentStatement(Statement originalStatement, ExecutionTargetRunner runner) {
    	this.runner = runner;
        fOriginalStatement = originalStatement;
    }

    @Override
    public void evaluate() throws Throwable {
    	runner.run(this);
    }

	public Void call() throws Exception {
		try {
			fOriginalStatement.evaluate();
			return null;
		}
		catch (Throwable e) {
			return Throwables.asException(e);
			/*
			Callable's call signature throws an Exception
			while Statement's evaluate throws a Throwable.
			We don't want to catch exceptions here, and we almost always we don't want to catch Errors 
			This should is generally everything Throwable.
			*/
		}
	}

	public void before() {
		
	}

	public void after() {
		
	}

}