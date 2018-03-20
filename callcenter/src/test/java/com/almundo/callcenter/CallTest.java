package com.almundo.callcenter;

import org.junit.Test;

/**
 * Test cases for {@link Call}.
 * 
 * @author <a href="carlosfruizn@gmail.com">Carlos Fernando Ruiz Nieto</a>
 * @version 0.0.1
 * @since 0.0.1
 */
public class CallTest {

	/**
	 * Creates the call with null duration test.
	 */
	@Test(expected = NullPointerException.class)
	public void createCallWithNullDurationTest() {
		
		new Call(null);
	}
	
	/**
	 * Creates the call with invalid duration test.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void createCallWithInvalidDurationTest() {
		
		new Call(-1);
	}
	
}
