/*
 * Copyright 2004/2005 Anite - Enforcement & Security
 *    http://www.anite.com/publicsector
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.anite.zebra.core.exceptions;

import org.apache.commons.lang.exception.NestableException;

/**
 * @author Matthew.Norris
 */
public class BaseZebraException extends NestableException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * @param message
	 */
	public BaseZebraException(String message) {
		super(message);
	}

	/**
	 * @param message
	 * @param nestedException
	 */
	public BaseZebraException(String message, Throwable nestedException) {
		super(message, nestedException);
	}

	/**
	 * @param nestedException
	 */
	public BaseZebraException(Throwable nestedException) {
		super(nestedException);
	}

}