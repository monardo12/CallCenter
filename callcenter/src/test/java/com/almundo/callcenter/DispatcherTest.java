package com.almundo.callcenter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.junit.Test;

/**
 * Test cases for {@link Dispatcher}.
 * 
 * @author <a href="carlosfruizn@gmail.com">Carlos Fernando Ruiz Nieto</a>
 * @version 0.0.1
 * @since 0.0.1
 */
public class DispatcherTest {

	/** The number of calls */
	private static final int NUMBER_OF_CALLS = 10;
	
	/** The minimum call duration*/
	private static final int MIN_CALL_DURATION = 5;

	/** The maximum call duration */
    private static final int MAX_CALL_DURATION = 10;

    /**
     * Dispatch calls to null employees test.
     */
    @Test(expected = NullPointerException.class)
    public void dispatchCallsToNullEmployeesTest() {
    	
    	new Dispatcher(null);
    }
    
    /**
     * Dispatch calls to employees test.
     *
     * @throws InterruptedException the interrupted exception
     */
    @Test
    public void dispatchCallsToEmployeesTest() throws InterruptedException {
    	
        List<Employee> employees = buildTesEmployees();
        Dispatcher dispatcher = new Dispatcher(employees);
        dispatcher.start();
        
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(dispatcher);

        buildTestCalls().stream().forEach(call -> {
			dispatcher.dispatchCall(call);
			try {
				TimeUnit.MILLISECONDS.sleep(500);
			} catch (InterruptedException e) {
				Assert.fail();
			}
		});

        dispatcher.stop();
        executorService.awaitTermination(MAX_CALL_DURATION, TimeUnit.SECONDS);
		Assert.assertEquals(NUMBER_OF_CALLS,
				employees.stream().mapToInt(employee -> employee.getAttendedCalls().size()).sum());
    }

    /**
     * Builds the tes employees.
     *
     * @return the list
     */
    private static List<Employee> buildTesEmployees() {
    	
    	List<Employee> employees = new ArrayList<>();
    	employees.add(new Operator());
    	employees.add(new Operator());
    	employees.add(new Operator());
    	employees.add(new Operator());
    	employees.add(new Operator());
    	employees.add(new Operator());
    	employees.add(new Supervisor());
    	employees.add(new Supervisor());
    	employees.add(new Supervisor());
    	employees.add(new Director());
        return employees;
    }

    /**
     * Builds the test calls.
     *
     * @return the list
     */
    private static List<Call> buildTestCalls() {

		List<Call> calls = new ArrayList<>();
		for (int i = 0; i < NUMBER_OF_CALLS; i++) {
			calls.add(buildRandomCall());
		}
		return calls;
    }
    
    /**
     * Builds the random call.
     *
     * @return the call
     */
    private static Call buildRandomCall() {
    	
    	return new Call(ThreadLocalRandom.current().nextInt(MIN_CALL_DURATION, MAX_CALL_DURATION));
    }
	
}
