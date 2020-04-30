package org.bonitasoft.ca.auth;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;

import org.bonitasoft.console.common.server.auth.AuthenticationFailedException;
import org.bonitasoft.console.common.server.auth.AuthenticationManager;
import org.bonitasoft.console.common.server.auth.AuthenticationManagerProperties;
import org.bonitasoft.console.common.server.auth.impl.standard.StandardAuthenticationManagerImpl;
import org.bonitasoft.console.common.server.login.HttpServletRequestAccessor;
import org.bonitasoft.console.common.server.login.TenantIdAccessorFactory;
import org.bonitasoft.console.common.server.login.credentials.Credentials;

public class CustomAuthenticationManager extends StandardAuthenticationManagerImpl {

	private static final Logger LOGGER = Logger.getLogger(CustomAuthenticationManager.class.getName());


	public static final String BASIC_USERNAME = "authentication.username";

	public static final String BASIC_TENANT_ID = "authentication.tenant.id";

	public static final String PASSPHRASE = "authentication.passphrase";
	
	public static final String PASSPHRASE_PROPERTY = "auth.passphrase";

	public static final String GLOBAL_LOGOUT_PROPERTY = "ca.logout.global";

	@Override
	public String getLoginPageURL(HttpServletRequestAccessor requestAccessor, String redirectURL) throws ServletException {
		LOGGER.fine("CustomAuthenticationManager#getLoginPageURL");
		return super.getLoginPageURL(requestAccessor, redirectURL);
	}

	@Override
	public Map<String, Serializable> authenticate(HttpServletRequestAccessor requestAccessor, Credentials credentials) throws AuthenticationFailedException {
		if (LOGGER.isLoggable(Level.FINE)) {
			LOGGER.log(Level.FINE, "#authenticate (this implementation of " + AuthenticationManager.class.getName()
					+ " retrieves the Principal from the request");
		}

		final Map<String, Serializable> credentialMap = new HashMap<>();
		credentialMap.put(BASIC_USERNAME, "walter.bates");
		credentialMap.put(PASSPHRASE, getAuthenticationProperty(PASSPHRASE_PROPERTY, credentials.getTenantId()));
		credentialMap.put(BASIC_TENANT_ID, credentials.getTenantId());
		return credentialMap;
	}


	protected String getAuthenticationProperty(String propertyName, long tenantId) {
		AuthenticationManagerProperties authenticationManagerProperties = AuthenticationManagerProperties.getProperties(tenantId);
		return authenticationManagerProperties.getProperty(propertyName);
	}

	@Override
	public String getLogoutPageURL(HttpServletRequestAccessor requestAccessor, String redirectURL) throws ServletException {
		LOGGER.fine("SAML2AuthenticationManagerImpl#getLogoutPageURL");
		String tenantIdStr = requestAccessor.getTenantId();
		long tenantId = TenantIdAccessorFactory.getTenantIdAccessor(requestAccessor).ensureTenantId();
		String performGlobalLogout = getAuthenticationProperty(GLOBAL_LOGOUT_PROPERTY, tenantId);
		if (Boolean.parseBoolean(performGlobalLogout)) {
			return requestAccessor.asHttpServletRequest().getContextPath() + (tenantIdStr != null ? "?tenant=" + tenantIdStr : "");
		}
		return null;
	}


}
