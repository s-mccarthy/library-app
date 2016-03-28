package com.library.app.exceptions;

import javax.ejb.ApplicationException;

@ApplicationException
public class CategoryAlreadyExistsException extends RuntimeException {
	private static final long serialVersionUID = -3997070012901203885L;

}