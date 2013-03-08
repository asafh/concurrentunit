concurrentunit
==============

concurrentunit was created to help me test concurrent parts in [justconfig](https://github.com/asafh/justconfig).  
Using JUnit there was no easy was to test code under concurrent load.

## Example

```java
@Test
  public void testCachexd() throws SecurityException, Throwable {
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
