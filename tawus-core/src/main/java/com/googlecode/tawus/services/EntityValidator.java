package com.googlecode.tawus.services;

import org.apache.tapestry5.ValidationException;

public interface EntityValidator {

   void validate(Object entity) throws ValidationException;

}
