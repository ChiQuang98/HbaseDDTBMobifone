package com.mobifone.bigdata.ldap;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.GeneralSecurityException;
import java.security.cert.X509Certificate;
import java.util.Hashtable;

import javax.naming.AuthenticationException;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.ldap.InitialLdapContext;
import javax.net.SocketFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.log4j.Logger;


/**
 * BlindSSLSocketFactoryTest Simple test to show an Active Directory (LDAP) and
 * HTTPS connection without verifying the server's certificate.
 * 
 * From: http://blog.platinumsolutions.com/node/79
 * http://blog.platinumsolutions.com/files/BlindSSLSocketFactoryTest.java.txt
 * 
 * @author Mike McKinney, Platinum Solutions, Inc.
 */
public class BlindSslSocketFactory extends SocketFactory {
	
	private static Logger logger = Logger.getLogger(BlindSslSocketFactory.class);
	
	private static SocketFactory blindFactory = null;
	public static String LDAP_HOST = "ldaps://10.3.12.17:636";
	public static String LDAP_DOMAIN = "mobifone.vn";

	/**
	 * Builds an all trusting "blind" ssl socket factory.
	 */
	static {
		// create a trust manager that will purposefully fall down on the job
		TrustManager[] blindTrustMan = new TrustManager[] { new X509TrustManager() {
			public X509Certificate[] getAcceptedIssuers() {
				return null;
			}

			public void checkClientTrusted(X509Certificate[] c, String a) {
			}

			public void checkServerTrusted(X509Certificate[] c, String a) {
			}
		} };

		// create our "blind" ssl socket factory with our lazy trust manager
		try {
			SSLContext sc = SSLContext.getInstance("SSL");
			sc.init(null, blindTrustMan, new java.security.SecureRandom());
			blindFactory = sc.getSocketFactory();
		} catch (GeneralSecurityException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @see javax.net.SocketFactory#getDefault()
	 */
	public static SocketFactory getDefault() {
		return new BlindSslSocketFactory();
	}

	/**
	 * @see javax.net.SocketFactory#createSocket(java.lang.String, int)
	 */
	public Socket createSocket(String arg0, int arg1) throws IOException, UnknownHostException {
		return blindFactory.createSocket(arg0, arg1);
	}

	/**
	 * @see javax.net.SocketFactory#createSocket(java.net.InetAddress, int)
	 */
	public Socket createSocket(InetAddress arg0, int arg1) throws IOException {
		return blindFactory.createSocket(arg0, arg1);
	}

	/**
	 * @see javax.net.SocketFactory#createSocket(java.lang.String, int,
	 *      java.net.InetAddress, int)
	 */
	public Socket createSocket(String arg0, int arg1, InetAddress arg2, int arg3)
			throws IOException, UnknownHostException {
		return blindFactory.createSocket(arg0, arg1, arg2, arg3);
	}

	/**
	 * @see javax.net.SocketFactory#createSocket(java.net.InetAddress, int,
	 *      java.net.InetAddress, int)
	 */
	public Socket createSocket(InetAddress arg0, int arg1, InetAddress arg2, int arg3) throws IOException {
		return blindFactory.createSocket(arg0, arg1, arg2, arg3);
	}
	
	
	
	public static boolean authentication(String strUser, String strPass) {
		
		boolean validateCert = false;

		// ****************************************************>
		// LDAPS CONNECTION
		// ****************************************************>
		Hashtable<String, String> env = new Hashtable<String, String>();
		
		env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
		env.put(Context.PROVIDER_URL, LDAP_HOST);
		env.put(Context.SECURITY_AUTHENTICATION, "simple");
		env.put(Context.SECURITY_PRINCIPAL, strUser + "@" + LDAP_DOMAIN);
		env.put(Context.SECURITY_CREDENTIALS, strPass);
		
		System.setProperty("com.sun.jndi.ldap.object.disableEndpointIdentification", "true");
		
		if (LDAP_HOST.startsWith("ldaps") && !validateCert) {
			env.put("java.naming.ldap.factory.socket", BlindSslSocketFactory.class.getName());
		}
		
		try {
			new InitialLdapContext(env, null);
			return true;
		} catch (AuthenticationException e) {
			logger.error("error", e);
		} catch (NamingException e) {
			logger.error("error", e);
		}
		
		return false;
	}
	
}
