package org.alfresco.extension.authentication.aop;

import java.util.List;

import net.sf.acegisecurity.providers.encoding.PasswordEncoder;

import org.alfresco.extension.authentication.approver.CredentialApprover;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class AuthenticationServiceAdvice implements MethodInterceptor {

	private final static Log log = LogFactory.getLog(AuthenticationServiceAdvice.class);
	private PasswordEncoder passwordEncoder;
	private List<CredentialApprover> approvers;
	
	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		Object[] args = invocation.getArguments();
		log.debug("MethodInterceptor invokation on method " + invocation.getMethod().getName());
		
		Credentials credentials = new Credentials(passwordEncoder, args);
		for(CredentialApprover approver : approvers){
			approver.process(credentials);
		}
		
		return invocation.proceed();

	}

	public void setApprovers(List<CredentialApprover> approvers) {
		this.approvers = approvers;
	}
}
