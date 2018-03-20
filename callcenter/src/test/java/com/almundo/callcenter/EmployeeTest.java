package com.almundo.callcenter;

import static org.junit.Assert.assertEquals;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

import com.almundo.callcenter.constant.EmployeeState;
import com.almundo.callcenter.constant.EmployeeType;

/**
 * Test cases for {@link Employee}.
 * 
 * @author <a href="carlosfruizn@gmail.com">Carlos Fernando Ruiz Nieto</a>
 * @version 0.0.1
 * @since 0.0.1
 */
public class EmployeeTest {

	/**
	 * Operator creation test.
	 */
	@Test
	public void createOperatorTest() {
		
		Employee operator = new Operator();
		assertEquals(EmployeeState.AVAILABLE, operator.getState());
		assertEquals(EmployeeType.OPERATOR, operator.getType());
		
	}
	
	/**
	 * Supervisor creation test.
	 */
	@Test
	public void createSupervisorTest() {
		
		Employee operator = new Supervisor();
		assertEquals(EmployeeState.AVAILABLE, operator.getState());
		assertEquals(EmployeeType.SUPERVISOR, operator.getType());
	}
	
	/**
	 * Director creation test.
	 */
	@Test
	public void createDirectorTest() {
		
		Employee operator = new Director();
		assertEquals(EmployeeState.AVAILABLE, operator.getState());
		assertEquals(EmployeeType.DIRECTOR, operator.getType());
	}
	
	/**
	 * Attend call test.
	 * @throws InterruptedException 
	 */
	@Test
	public void attendCallTest() throws InterruptedException {
		
		 Employee employee =  new Operator();
		 ExecutorService executorService = Executors.newSingleThreadExecutor();
		 executorService.execute(employee);

		 assertEquals(EmployeeState.AVAILABLE, employee.getState());
		 TimeUnit.SECONDS.sleep(1);
		 employee.attend(new Call(1));
		 employee.attend(new Call(2));
		 TimeUnit.SECONDS.sleep(1);
		 assertEquals(EmployeeState.BUSY, employee.getState());
	        
		 executorService.awaitTermination(5, TimeUnit.SECONDS);
		 assertEquals(2, employee.getAttendedCalls().size());
	}
	
}
