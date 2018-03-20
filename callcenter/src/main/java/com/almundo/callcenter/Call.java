package com.almundo.callcenter;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Call representation.
 * 
 * @author <a href="carlosfruizn@gmail.com">Carlos Fernando Ruiz Nieto</a>
 * @version 0.0.1
 * @since 0.0.1
 */
public class Call {

	/** The Constant logger. */
	private static final Logger logger = LoggerFactory.getLogger(Dispatcher.class);
	
	/** The id. */
	private String id;
	
	/** The duration of a call in seconds. */
	private Integer duration;

	/**
	 * Instantiates a new call.
	 *
	 * @param duration the duration
	 */
	public Call(final Integer duration) {
		
		super();
		Validate.notNull(duration);
		Validate.isTrue(duration >= 0);
		this.id = UUID.randomUUID().toString();
		this.duration = duration;
	}

	/**
	 * Gets the call id.
	 *
	 * @return the id
	 */
	public String getId() {
		
		return id;
	}

	/**
	 * Gets the call duration.
	 *
	 * @return the duration
	 */
	public Integer getDuration() {
		
		return this.duration;
	}

	/**
	 * Starts the call.
	 *
	 * @throws InterruptedException the interrupted exception
	 */
	public void start() throws InterruptedException {

		logger.info(this + " started");
		TimeUnit.SECONDS.sleep(this.duration);
		logger.info(this + " finished");
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		
		return "Call [id=" + id + ", duration=" + duration + "]";
	}

}
