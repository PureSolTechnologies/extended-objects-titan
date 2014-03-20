package com.puresoltechnologies.xo.titan.api.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.buschmais.cdo.spi.annotation.QueryDefinition;

/**
 * <p>
 * Marks an interface or method as a Gremlin query.
 * </p>
 * <p>
 * For Gremlin language, have a look to: <a
 * href="http://gremlindocs.com">http://gremlindocs.com</a>
 * </p>
 */
@QueryDefinition
@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Gremlin {

	/**
	 * @return Returns the Gremlin expression as {@link String}.
	 */
	String value();

}
