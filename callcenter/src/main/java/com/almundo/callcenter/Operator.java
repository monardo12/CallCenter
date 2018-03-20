package com.almundo.callcenter;

import com.almundo.callcenter.constant.EmployeeType;

/**
 * Operator representation.
 * 
 * @author <a href="carlosfruizn@gmail.com">Carlos Fernando Ruiz Nieto</a>
 * @version 0.0.1
 * @since 0.0.1
 */
public class Operator extends Employee {

	/* (non-Javadoc)
	 * @see com.almundo.callcenter.Employee#getType()
	 */
	@Override
	public EmployeeType getType() {
		
		return EmployeeType.OPERATOR;
	}

}
