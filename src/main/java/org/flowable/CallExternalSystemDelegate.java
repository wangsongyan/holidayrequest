package org.flowable;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;

public class CallExternalSystemDelegate implements JavaDelegate{

	@Override
	public void execute(DelegateExecution execution) {
		System.out.println("Calling the external system from employee " 
				+ execution.getVariable("employee"));
	}

}
