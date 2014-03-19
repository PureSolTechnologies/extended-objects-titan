package com.puresoltechnologies.xo.titan.api.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.buschmais.cdo.spi.annotation.EntityDefinition;

@EntityDefinition
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface VertexDefinition {

	String DEFAULT_VALUE = "";

	/**
	 * @return The name of the type.
	 */
	String value() default "";

}
