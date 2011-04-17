package org.alfresco.extension.authentication.approver;

import java.util.ArrayList;
import java.util.Arrays;
import junit.framework.Assert;
import net.sf.acegisecurity.providers.encoding.PasswordEncoder;

import org.alfresco.extension.authentication.aop.Credentials;
import org.alfresco.extension.authentication.aop.ExtendedAuthorizationModel;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.NodeService;
import org.alfresco.service.cmr.security.PersonService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

public class PasswordHistoryApproverTest {

	private PasswordHistoryApprover approver;
	private String user;
	@Mock private PersonService mockedPersonService;
	@Mock private NodeService mockedNodeService;
	
	@Before
	public void setUp() throws Exception {
		user = "testUser";
		approver = new PasswordHistoryApprover();
		mockedPersonService = mock(PersonService.class);
		mockedNodeService = mock(NodeService.class);
		
		approver.setPersonService(mockedPersonService);
		approver.setNodeService(mockedNodeService);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void processSuccessfullyWithLimitedHistory() {
		approver.setMaxHistoryEntries(3);
		Object[] args = new Object[] { user, "oldpass".toCharArray(), "newpass".toCharArray() };	
		ArrayList<String> passwordHistory = createPasswordHistory("pass1","pass2","pass3");
		
		NodeRef dummyRef = new NodeRef("workspace://testStore/xxx");
		
		when(mockedPersonService.getPerson(user)).thenReturn(dummyRef);
		when(mockedNodeService.hasAspect(dummyRef, ExtendedAuthorizationModel.ASPECT_PASSWORD_EXTENSION)).thenReturn(true);
		when(mockedNodeService.getProperty(dummyRef,
				ExtendedAuthorizationModel.PROPERTY_PASSWORD_HISTORY)).thenReturn(passwordHistory);

		try {
			PasswordEncoder encoder = mock(PasswordEncoder.class);
			when(encoder.encodePassword("newpass", null)).thenReturn("newpass");
			when(encoder.encodePassword("oldpass", null)).thenReturn("oldpass");
			
			Credentials credentials = new Credentials(encoder, args);
			approver.process(credentials);
			
			Assert.assertFalse(passwordHistory.contains("pass1"));
			Assert.assertTrue(passwordHistory.contains("newpass"));
			Assert.assertEquals(3, passwordHistory.size());
		} catch (Exception e) {
			fail("No exception expected" + e);			
		}
	}

	private ArrayList<String> createPasswordHistory(String...strings){
		ArrayList<String> passwordHistory = new ArrayList<String>();
		passwordHistory.addAll(Arrays.asList(strings));
		return passwordHistory;
	}
}
