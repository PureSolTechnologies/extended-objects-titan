package com.puresoltechnologies.xo.titan.impl;

import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.buschmais.cdo.spi.datastore.DatastoreMetadataFactory;
import com.buschmais.cdo.spi.metadata.type.TypeMetadata;
import com.buschmais.cdo.spi.reflection.AnnotatedElement;
import com.buschmais.cdo.spi.reflection.AnnotatedMethod;
import com.buschmais.cdo.spi.reflection.AnnotatedType;
import com.buschmais.cdo.spi.reflection.PropertyMethod;
import com.puresoltechnologies.xo.titan.api.annotation.EdgeDefinition;
import com.puresoltechnologies.xo.titan.api.annotation.EdgeDefinition.Incoming;
import com.puresoltechnologies.xo.titan.api.annotation.EdgeDefinition.Outgoing;
import com.puresoltechnologies.xo.titan.api.annotation.VertexDefinition;
import com.puresoltechnologies.xo.titan.impl.metadata.TitanCollectionPropertyMetadata;
import com.puresoltechnologies.xo.titan.impl.metadata.TitanIndexedPropertyMetadata;
import com.puresoltechnologies.xo.titan.impl.metadata.TitanNodeMetadata;
import com.puresoltechnologies.xo.titan.impl.metadata.TitanPropertyMetadata;
import com.puresoltechnologies.xo.titan.impl.metadata.TitanReferencePropertyMetadata;
import com.puresoltechnologies.xo.titan.impl.metadata.TitanRelationMetadata;
import com.tinkerpop.blueprints.Direction;

/**
 * This class implements the XO {@link DatastoreMetadataFactory} for Titan
 * database.
 * 
 * @author Rick-Rainer Ludwig
 */
public class TitanMetadataFactory
		implements
		DatastoreMetadataFactory<TitanNodeMetadata, String, TitanRelationMetadata, String> {

	@Override
	public TitanNodeMetadata createEntityMetadata(AnnotatedType annotatedType,
			Map<Class<?>, TypeMetadata> metadataByType) {
		VertexDefinition annotation = annotatedType
				.getAnnotation(VertexDefinition.class);
		String value = null;
		if (annotation != null) {
			value = annotation.value();
			if ((value == null) || (value.isEmpty())) {
				value = annotatedType.getName();
			}
		}
		return new TitanNodeMetadata(value);
	}

	@Override
	public <ImplementedByMetadata> ImplementedByMetadata createImplementedByMetadata(
			AnnotatedMethod annotatedMethod) {
		return null;
	}

	@Override
	public TitanCollectionPropertyMetadata createCollectionPropertyMetadata(
			PropertyMethod propertyMethod) {
		String name = determineVertexName(propertyMethod);
		Direction direction = determineEdgeDirection(propertyMethod);
		return new TitanCollectionPropertyMetadata(name, direction);
	}

	@Override
	public TitanReferencePropertyMetadata createReferencePropertyMetadata(
			PropertyMethod propertyMethod) {
		String name = determineVertexName(propertyMethod);
		Direction direction = determineEdgeDirection(propertyMethod);
		return new TitanReferencePropertyMetadata(name, direction);
	}

	@Override
	public TitanPropertyMetadata createPropertyMetadata(
			PropertyMethod propertyMethod) {
		String name = determineVertexName(propertyMethod);
		return new TitanPropertyMetadata(name);
	}

	/**
	 * This method is a helper method to extract the name from a
	 * {@link PropertyMethod}.
	 * 
	 * @param propertyMethod
	 *            is the {@link PropertyMethod} object which represents the
	 *            method for which the name is to be checked.
	 * @return A {@link String} object is returned containing the name of the
	 *         edge.
	 */
	private static String determineVertexName(PropertyMethod propertyMethod) {
		VertexDefinition property = propertyMethod
				.getAnnotationOfProperty(VertexDefinition.class);
		return property != null ? property.value() : propertyMethod.getName();
	}

	/**
	 * This method is a helper method to extract the edge direction from a
	 * {@link PropertyMethod}.
	 * 
	 * @param propertyMethod
	 *            is the {@link PropertyMethod} object which represents the
	 *            method for which the edge direction is to be checked.
	 * @return A {@link Direction} object is returned containing the direction
	 *         of the edge.
	 */
	private static Direction determineEdgeDirection(
			PropertyMethod propertyMethod) {
		Outgoing outgoingAnnotation = propertyMethod
				.getAnnotation(Outgoing.class);
		Incoming incomingAnnotation = propertyMethod
				.getAnnotation(Incoming.class);
		if ((outgoingAnnotation != null) && (incomingAnnotation != null)) {
			return Direction.BOTH;
		} else if (incomingAnnotation != null) {
			return Direction.IN;
		} else {
			return Direction.OUT;
		}
	}

	@Override
	public TitanIndexedPropertyMetadata createIndexedPropertyMetadata(
			PropertyMethod propertyMethod) {
		String name = determineVertexName(propertyMethod);
		return new TitanIndexedPropertyMetadata(name);
	}

	@Override
	public TitanRelationMetadata createRelationMetadata(
			AnnotatedElement<?> annotatedElement,
			Map<Class<?>, TypeMetadata> metadataByType) {
		EdgeDefinition relationAnnotation;
		if (annotatedElement instanceof PropertyMethod) {
			relationAnnotation = ((PropertyMethod) annotatedElement)
					.getAnnotationOfProperty(EdgeDefinition.class);
		} else {
			relationAnnotation = annotatedElement
					.getAnnotation(EdgeDefinition.class);
		}
		String name = null;
		if (relationAnnotation != null) {
			String value = relationAnnotation.value();
			if (!EdgeDefinition.DEFAULT_VALUE.equals(value)) {
				name = value;
			}
		}
		if (name == null) {
			name = StringUtils.uncapitalize(annotatedElement.getName());
		}
		return new TitanRelationMetadata(name);
	}
}
