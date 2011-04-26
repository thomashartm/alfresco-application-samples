package org.alfresco.extension.authentication.aop;

import java.util.List;

import org.alfresco.extension.authentication.constraint.CredentialConstraint;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class AuthenticationServiceAdvice implements MethodInterceptor {

	private final static Log log = LogFactory.getLog(AuthenticationServiceAdvice.class);
	private List<CredentialConstraint> constraints;
	
	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		if(log.isDebugEnabled()) {
			log.debug("MethodInterceptor invokation on method " + invocation.getMethod().getName());
		}
		
		Object[] args = invocation.getArguments();
		Credentials credentials = new Credentials(args);
		for(CredentialConstraint constraint : constraints){
			constraint.process(credentials);
		}
		
		return invocation.proceed();
	}

	public void setConstraints(List<CredentialConstraint> constraints) {
		this.constraints = constraints;
	}
}
