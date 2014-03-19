package com.puresoltechnologies.xo.titan.api.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.buschmais.cdo.spi.annotation.IndexDefinition;

/**
 * Marks a property as indexed.
 * <p/>
 * <p>
 * An indexed property is used to find instances using
 * {@link com.buschmais.cdo.api.CdoManager#find(Class, Object)}.
 * </p>
 */
@IndexDefinition
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Indexed {

}
