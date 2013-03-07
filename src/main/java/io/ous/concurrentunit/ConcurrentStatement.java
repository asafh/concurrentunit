package io.ous.concurrentunit;

import io.ous.concurrentunit.execution.ExecutionStrategy;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import org.junit.runners.model.Statement;

class ConcurrentStatement extends Statement {
    private final Statement fOriginalStatement;

    private final ExecutorService executor;
	private final ExecutionStrategy strategy;

    public ConcurrentStatement(Statement originalStatement,ExecutorService executor, ExecutionStrategy strategy) {
        fOriginalStatement = originalStatement;
        this.executor = executor;
        this.strategy = strategy;
    }

    @Override
    public void evaluate() throws Throwable {
    	Future<List<Future<Boolean>>> run = strategy.run(executor, new Callable<Boolean>() {
			public Boolean call() throws Exception {
				try {
					fOriginalStatement.evaluate();
					return true;
				}
				catch(Exception e) {
					throw e;
				}
				catch(Error e) {
					throw e;
				}
				catch (Throwable e) {
					throw new IllegalStateException(e); //should never occur since normally everything is either an Exception or an Error
				}
			}
		});
    	
    	List<Future<Boolean>> list = run.get();
    	for(Future<Boolean> response : list) {
    		response.get(); //Throws ExecutionException if occured that will buble up
    	}
    }

}