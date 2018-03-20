package com.almundo.callcenter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.almundo.callcenter.constant.EmployeeState;
import com.almundo.callcenter.constant.EmployeeType;

/**
 * Employee abstract representation.
 * 
 * @author <a href="carlosfruizn@gmail.com">Carlos Fernando Ruiz Nieto</a>
 * @version 0.0.1
 * @since 0.0.1
 */
public abstract class Employee implements Runnable {

	/** The Constant logger. */
	private static final Logger logger = LoggerFactory.getLogger(Employee.class);
	
	/** The id. */
	private String id;
	
	/** The employee state. */
	private EmployeeState state;
	
	/** The incoming calls. */
	private BlockingQueue<Call> incomingCalls;

    /** The attended calls. */
    private BlockingQueue<Call> attendedCalls;
	
    /**
     * Instantiates a new employee.
     */
    public Employee() {
    	
    	this.id = UUID.randomUUID().toString();
        this.state = EmployeeState.AVAILABLE;
        this.incomingCalls = new LinkedBlockingQueue<>();
        this.attendedCalls = new LinkedBlockingQueue<>();
    }
    
    /**
     * Gets the employee id.
     *
     * @return the id
     */
    public String getId() {
    	
		return id;
	}

	/**
     * Gets the employee type.
     *
     * @return the type
     */
    public abstract EmployeeType getType();
    
    /**
     * Gets the employee state.
     *
     * @return the state
     */
    public synchronized EmployeeState getState() {
    	
        return this.state;
    }

    /**
     * Gets the employee's attended calls.
     *
     * @return the attended calls
     */
    public synchronized List<Call> getAttendedCalls() {
    	
        return new ArrayList<>(attendedCalls);
    }
    
    /**
     * Sets the employee state.
     *
     * @param employeeState the new state
     */
    private synchronized void setState(final EmployeeState employeeState) {
    	
        logger.info(this + " is now " + employeeState);
        this.state = employeeState;
    }
    
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		
		return "Employee [id=" + id + ", type=" + getType() + ", state=" + state + "]";
	}

	/**
     * Attend call.
     *
     * @param call the call
     */
    public synchronized void attend(Call call) {
    	
        logger.info(this + " receives " + call);
        this.incomingCalls.add(call);
    }
    
	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		
		logger.info(this + " is active");
		while(true) {
			if (!this.incomingCalls.isEmpty()) {
				Call call = this.incomingCalls.poll();
				setState(EmployeeState.BUSY);
				logger.info(this + " answers " + call);
	            try {
	                call.start();
	            } catch (InterruptedException e) {
	                logger.error(this + " was interrupted and could not finish " + call);
	            } finally {
	                setState(EmployeeState.AVAILABLE);
	            }
	            this.attendedCalls.add(call);
	            logger.info(this + " finishes " + call);
			}
		}
	}

}
