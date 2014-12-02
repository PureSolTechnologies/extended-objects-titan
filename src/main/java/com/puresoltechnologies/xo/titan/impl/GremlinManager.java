package com.puresoltechnologies.xo.titan.impl;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Map.Entry;

import com.buschmais.xo.api.XOException;
import com.buschmais.xo.spi.reflection.AnnotatedElement;
import com.puresoltechnologies.xo.titan.api.annotation.Gremlin;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;

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
     *            DatastoreSession#executeQuery(Object, java.util.Map).
     * @return A {@link String} containing a Gremlin expression is returned.
     */
    public static <QL> GremlinExpression getGremlinExpression(QL expression,
	    Map<String, Object> parameters) {
	GremlinExpression gremlinExpression = null;
	if (expression instanceof String) {
	    gremlinExpression = new GremlinExpression("", (String) expression);
	} else if (expression instanceof Gremlin) {
	    Gremlin gremlin = (Gremlin) expression;
	    gremlinExpression = new GremlinExpression(gremlin.name(),
		    gremlin.value());
	} else if (AnnotatedElement.class.isAssignableFrom(expression
		.getClass())) {
	    AnnotatedElement<?> typeExpression = (AnnotatedElement<?>) expression;
	    gremlinExpression = extractExpression(typeExpression);
	} else if (Class.class.isAssignableFrom(expression.getClass())) {
	    Class<?> clazz = (Class<?>) expression;
	    gremlinExpression = extractExpression(clazz);
	} else if (Method.class.isAssignableFrom(expression.getClass())) {
	    Method method = (Method) expression;
	    gremlinExpression = extractExpression(method);
	} else {
	    throw new XOException("Unsupported query expression "
		    + expression.toString() + "(class=" + expression.getClass()
		    + ")");
	}
	return applyParameters(parameters, gremlinExpression);
    }

    private static GremlinExpression extractExpression(
	    AnnotatedElement<?> typeExpression) {
	Gremlin gremlin = typeExpression.getAnnotation(Gremlin.class);
	if (gremlin == null) {
	    throw new XOException(typeExpression + " must be annotated with "
		    + Gremlin.class.getName());
	}
	return new GremlinExpression(gremlin);
    }

    private static <QL> GremlinExpression extractExpression(Class<?> clazz) {
	Gremlin gremlin = clazz.getAnnotation(Gremlin.class);
	if (gremlin == null) {
	    throw new XOException(clazz.getName() + " must be annotated with "
		    + Gremlin.class.getName());
	}
	return new GremlinExpression(gremlin);
    }

    private static <QL> GremlinExpression extractExpression(Method method) {
	Gremlin gremlin = method.getAnnotation(Gremlin.class);
	if (gremlin == null) {
	    throw new XOException(method.getName() + " must be annotated with "
		    + Gremlin.class.getName());
	}
	return new GremlinExpression(gremlin);
    }

    private static GremlinExpression applyParameters(
	    Map<String, Object> parameters, GremlinExpression gremlinExpression) {
	StringBuffer typeDefinitions = createTypeDefinitions(parameters);
	String expressionString = gremlinExpression.getExpression();
	for (String type : parameters.keySet()) {
	    String placeholder = "\\{" + type + "\\}";
	    if (!"this".equals(type)) {
		expressionString = expressionString.replaceAll(placeholder,
			type);
	    }
	}
	String enhancedExpressionString = typeDefinitions.toString()
		+ expressionString;
	return new GremlinExpression(gremlinExpression.getResultName(),
		enhancedExpressionString);
    }

    private static StringBuffer createTypeDefinitions(
	    Map<String, Object> parameters) {
	StringBuffer typeDefinitions = new StringBuffer();
	for (Entry<String, Object> entry : parameters.entrySet()) {
	    String type = entry.getKey();
	    if (!"this".equals(type)) {
		Object value = entry.getValue();
		if (String.class.equals(value.getClass())) {
		    typeDefinitions.append(type);
		    typeDefinitions.append("=");
		    typeDefinitions.append("'");
		    typeDefinitions.append(value);
		    typeDefinitions.append("'");
		    typeDefinitions.append("\n");
		} else if (Edge.class.isAssignableFrom(value.getClass())) {
		    continue;
		} else if (Vertex.class.isAssignableFrom(value.getClass())) {
		    continue;
		} else {
		    typeDefinitions.append(type);
		    typeDefinitions.append("=");
		    typeDefinitions.append(value);
		    typeDefinitions.append("\n");
		}
	    }
	}
	return typeDefinitions;
    }

}
