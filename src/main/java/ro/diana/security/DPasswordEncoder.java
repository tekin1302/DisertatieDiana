/*
 * Created on Sep 23, 2011 by dan.damian
 * Copyright (c) 2011 ARCASSIS SRL.
 * www.arcassis.com
 * All rights reserved.
 * This software is the confidential and proprietary information of Arcassis SRL ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the license agreement you entered into with Arcassis SRL.
 */
package ro.diana.security;

import org.springframework.dao.DataAccessException;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.authentication.encoding.PasswordEncoder;

/**
 * 
 * @author tekin
 * 
 * Encryption contains encryption tools.
 * 
 */
public class DPasswordEncoder implements PasswordEncoder {

	private static DPasswordEncoder theInstance = null;
	
	private DPasswordEncoder(){}
	
	public static DPasswordEncoder getInstance() {
		if (theInstance == null) {
			theInstance = new DPasswordEncoder();
		}
		return theInstance;
	}
	
	
	private Md5PasswordEncoder md5PwdEnc = new Md5PasswordEncoder();
	
	/* (non-Javadoc)
	 * @see org.springframework.security.authentication.encoding.DPasswordEncoder#encodePassword(java.lang.String, java.lang.Object)
	 */
	public String encodePassword(String password, Object salt)
			throws DataAccessException {
		return md5PwdEnc.encodePassword(password, salt);
	}

	/* (non-Javadoc)
	 * @see org.springframework.security.authentication.encoding.DPasswordEncoder#isPasswordValid(java.lang.String, java.lang.String, java.lang.Object)
	 */
	public boolean isPasswordValid(String encPass, String rawPass, Object salt)
			throws DataAccessException {
		return md5PwdEnc.isPasswordValid(encPass, rawPass, null);
	}
	
	public static void main(String[] args) {
		String password = "diana";
		DPasswordEncoder pe = new DPasswordEncoder();
		System.out.println(pe.encodePassword(password, null));
	}
}
