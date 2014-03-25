package com.puresoltechnologies.xo.titan.impl;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;

import com.buschmais.xo.api.XOException;
import com.buschmais.xo.spi.reflection.AnnotatedType;
import com.puresoltechnologies.xo.titan.api.annotation.Gremlin;

public class GremlinManagerTest {

	@Test
	public void testStringExpression() {
		String expression = GremlinManager
				.getGremlinExpression("This is a string expression.");
		assertThat(expression, is("This is a string expression."));
	}

	@Test
	public void testAnnotatedElementExpression() {
		Gremlin gremlin = mock(Gremlin.class);
		when(gremlin.value()).thenReturn("This is a Gremlin expression.");
		AnnotatedType annotatedElement = mock(AnnotatedType.class);
		when(annotatedElement.getAnnotation(Gremlin.class)).thenReturn(gremlin);
		String expression = GremlinManager
				.getGremlinExpression(annotatedElement);
		assertThat(expression, is("This is a Gremlin expression."));
	}

	@Test(expected = XOException.class)
	public void testIllegalQuery() {
		GremlinManager.getGremlinExpression(new Object());
	}
}
