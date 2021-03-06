package com.puresoltechnologies.xo.titan.test.relation.qualified;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;

import com.puresoltechnologies.xo.titan.api.annotation.EdgeDefinition;

@EdgeDefinition("OneToOne")
@Retention(RUNTIME)
public @interface QualifiedOneToOne {
}
