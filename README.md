concurrentunit
==============

concurrentunit was created to help me test concurrent parts in [justconfig](https://github.com/asafh/justconfig).  
While writing JUnit tests I couldn't find an easy was to test code under concurrent load. To solve
this issue I created concurrentunit, which is basically a helper to submit concurrent executions of code with a
given execution strategy (included are ScheduledExecutionStrategy and ImmediateExecutionStrategy)

## Example
In this example. the before method is invoked, then after 100ms, the call method is invoked 50 times at a rate of 1/50ms. When all call invocations are done after is invoked.
```java
@Test
  public void testCached() throws SecurityException, Throwable {
		ExecutionTargetRunner runner = new ExecutionTargetRunner(new ScheduledExecutionStrategy(50, 100, 50));
		runner.run(new ExecutionTarget<Void>() {
		protected CachedInvocationHandler cachedInvocationHandler;
		protected InvocationHandler wrapped;
		protected Object unique;
			@Override
	  	public void before() throws Throwable {
				wrapped = mock(InvocationHandler.class);
				cachedInvocationHandler = new CachedInvocationHandler(wrapped);
				unique = new Object();
				when(wrapped.invoke(PROXY, x, NO_ARGS)).thenReturn(unique);
			}
	    
	    @Override
			public Void call() throws Exception {
				try {
					assertEquals(unique, cachedInvocationHandler.invoke(PROXY, x, NO_ARGS));
					assertEquals(unique,cachedInvocationHandler.invoke(PROXY, x, NO_ARGS));
					assertEquals(unique,cachedInvocationHandler.invoke(PROXY, x, NO_ARGS));
					assertEquals(unique,cachedInvocationHandler.invoke(PROXY, x, NO_ARGS));
					
					return null;
				}
				catch (Throwable e) {
					return Throwables.asException(e);
				}
			}
			@Override
			public void after() throws Throwable {
				verify(wrapped, times(1)).invoke(PROXY, x, NO_ARGS);
				verifyNoMoreInteractions(wrapped);
			}
		});
	}
```
