package io.ous.concurrentunit;

import io.ous.concurrentunit.execution.ExecutionStrategy;
import io.ous.concurrentunit.execution.ImmediateExecutionStrategy;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ExecutionTargetRunner {
    private final ExecutorService executor;
	private final ExecutionStrategy strategy;

    public ExecutionTargetRunner(ExecutorService executor, ExecutionStrategy strategy) {
        this.executor = executor;
        this.strategy = strategy;
    }
    public ExecutionTargetRunner(int executionCount) {
		this(new ImmediateExecutionStrategy(executionCount));
	}
	public ExecutionTargetRunner(ExecutionStrategy strategy) {
    	this(Executors.newCachedThreadPool(),strategy);
    }
	public ExecutionTargetRunner(ExecutorService executor, int executionCount) {
		this(executor, new ImmediateExecutionStrategy(executionCount));
	}
    
    private<V> V awaitFuture(Future<V> future) throws ExecutionException {
    	while(true) {
    		try {
				return future.get();
			} catch (InterruptedException e) {
				continue;
			}
    	}
    }

	public<V> void run(ExecutionTarget<V> target) throws ExecutionException {
		try {
			target.before();
		}
		catch(Throwable thr) {
			throw new ExecutionException("Error while running 'before' method",thr);
		}
		boolean succss = false;
		try {
			
			Future<List<Future<V>>> run = strategy.run(executor, target);
			List<Future<V>> list = awaitFuture(run);
			for(Future<V> response : list) {
				awaitFuture(response);
			}
			succss = true;
		}
		finally {
			try {
				target.after();
			}
			catch(Throwable thr) {
				if(succss) { //Otherwise don't replace the current exception.
					throw new ExecutionException("Error while running 'after' method",thr);					
				}
			}
		}
	}
}
