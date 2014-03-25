package com.puresoltechnologies.xo.titan.impl;

import java.lang.reflect.Method;

import com.buschmais.xo.api.XOException;
import com.buschmais.xo.spi.datastore.DatastoreSession;
import com.buschmais.xo.spi.reflection.AnnotatedElement;
import com.puresoltechnologies.xo.titan.api.annotation.Gremlin;

/**
 * This class manages the Gremlin expressions for the
 * {@link TitanCassandraStore}.
 * 
 * @author Rick-Rainer Ludwig
 */
public class GremlinManager {

	/**
	 * This is a helper method to extract the Gremlin expression.
	 * 
	 * @param expression
	 *            is the object which comes in from
	 *            {@link DatastoreSession#executeQuery(Object, java.util.Map)}.
	 * @return A {@link String} containing a Gremlin expression is returned.
	 */
	public static <QL> String getGremlinExpression(QL expression) {
		if (expression instanceof String) {
			return (String) expression;
		} else if (AnnotatedElement.class.isAssignableFrom(expression
				.getClass())) {
			AnnotatedElement<?> typeExpression = (AnnotatedElement<?>) expression;
			Gremlin gremlin = typeExpression.getAnnotation(Gremlin.class);
			if (gremlin == null) {
				throw new XOException(typeExpression
						+ " must be annotated with " + Gremlin.class.getName());
			}
			return gremlin.value();
		} else if (Class.class.isAssignableFrom(expression.getClass())) {
			Class<?> clazz = (Class<?>) expression;
			Gremlin gremlin = clazz.getAnnotation(Gremlin.class);
			if (gremlin == null) {
				throw new XOException(expression + " must be annotated with "
						+ Gremlin.class.getName());
			}
			return gremlin.value();
		} else if (Method.class.isAssignableFrom(expression.getClass())) {
			Method method = (Method) expression;
			Gremlin gremlin = method.getAnnotation(Gremlin.class);
			if (gremlin == null) {
				throw new XOException(expression + " must be annotated with "
						+ Gremlin.class.getName());
			}
			return gremlin.value();
		}
		throw new XOException("Unsupported query expression " + expression);
	}

}
