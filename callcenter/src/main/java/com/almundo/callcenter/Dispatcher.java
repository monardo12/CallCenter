package com.almundo.callcenter;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;

import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.almundo.callcenter.constant.EmployeeState;
import com.almundo.callcenter.constant.EmployeeType;

/**
 * Class in charge of dispatching calls.
 * 
 * @author <a href="carlosfruizn@gmail.com">Carlos Fernando Ruiz Nieto</a>
 * @version 0.0.1
 * @since 0.0.1
 */
public class Dispatcher implements Runnable {

	/** The Constant logger. */
	private static final Logger logger = LoggerFactory.getLogger(Dispatcher.class);
	
	/** The max concurrent calls. */
	public static final Integer MAX_CONCURRENT_CALLS = 10;
	
	/** If the dispatcher is active. */
	private Boolean active;
	
	/** The executor service. */
	private ExecutorService executorService;
	
	/** The employees. */
	private BlockingQueue<Employee> employees;

    /** The incoming calls. */
    private BlockingQueue<Call> incomingCalls;
    
    /**
     * Instantiates a new dispatcher.
     *
     * @param employees the employees
     */
    public Dispatcher(final List<Employee> employees) {
    	
    	Validate.notNull(employees);
    	this.employees = new LinkedBlockingQueue<>(employees);
    	this.incomingCalls = new LinkedBlockingQueue<>();
		this.executorService = Executors.newFixedThreadPool(MAX_CONCURRENT_CALLS);
    }
    
    /**
     * Checks if the dispatcher is active.
     *
     * @return the boolean
     */
    public synchronized Boolean isActive() {
    	
        return this.active;
    }
    
    /**
     * Starts the employee threads.
     */
    public synchronized void start() {
    	
    	this.active = true;
        this.employees.forEach(e -> this.executorService.execute(e));
    }
    
    /**
     * Stops the employee threads.
     */
    public synchronized void stop() {
    	
        this.active = false;
        this.executorService.shutdown();
    }
    
    /**
     * Dispatch call.
     *
     * @param call the call
     */
    public synchronized void dispatchCall(Call call) {
    	
        logger.info("Dispatching " + call);
        this.incomingCalls.add(call);
    }
    
    /**
     * Gets the available employee.
     * If an operator is not available looks for an available supervisor.
     * If a supervisor is not available looks for an available director.
     * If a director is not available. No employee is assigned.
     *
     * @return the available employee
     */
    private Employee getAvailableEmployee() {
    	
		List<Employee> availableEmployees = this.employees.stream()
				.filter(e -> EmployeeState.AVAILABLE.equals(e.getState()))
				.collect(Collectors.toList());
		
		logger.info("Available employees: " + availableEmployees.size());
        Optional<Employee> employee = availableEmployees.stream()
        		.filter(e -> EmployeeType.OPERATOR.equals(e.getType()))
        		.findAny();
        if (!employee.isPresent()) {
            employee = availableEmployees.stream()
            		.filter(e -> EmployeeType.SUPERVISOR.equals(e.getType()))
            		.findAny();
            if (!employee.isPresent()) {
                employee = availableEmployees.stream()
                		.filter(e -> EmployeeType.DIRECTOR.equals(e.getType()))
                		.findAny();
                if (!employee.isPresent()) {
                    return null;
                }
            }
        }
        
        logger.info(employee.get() + " is available");
        return employee.get();
    }
    
	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		
		while(isActive()) {
			if(!this.incomingCalls.isEmpty()) {
				Employee employee = getAvailableEmployee();
				if(employee != null) {
					Call call = this.incomingCalls.poll();
					try {
						employee.attend(call);
					} catch (Exception e) {
						logger.error(e.getMessage());
	                    this.incomingCalls.offer(call);
					}
				}
			}
		}
	}
	
}
