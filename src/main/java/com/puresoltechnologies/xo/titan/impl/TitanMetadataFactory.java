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
import com.puresoltechnologies.xo.titan.api.annotation.VertexDefinition;
import com.puresoltechnologies.xo.titan.impl.metadata.TitanCollectionPropertyMetadata;
import com.puresoltechnologies.xo.titan.impl.metadata.TitanIndexedPropertyMetadata;
import com.puresoltechnologies.xo.titan.impl.metadata.TitanNodeMetadata;
import com.puresoltechnologies.xo.titan.impl.metadata.TitanPropertyMetadata;
import com.puresoltechnologies.xo.titan.impl.metadata.TitanReferencePropertyMetadata;
import com.puresoltechnologies.xo.titan.impl.metadata.TitanRelationMetadata;

public class TitanMetadataFactory
		implements
		DatastoreMetadataFactory<TitanNodeMetadata, String, TitanRelationMetadata, String> {

	@Override
	public TitanNodeMetadata createEntityMetadata(AnnotatedType annotatedType,
			Map<Class<?>, TypeMetadata> metadataByType) {
		VertexDefinition annotation = annotatedType
				.getAnnotation(VertexDefinition.class);
		String value = annotation.value();
		if ((value == null) || (value.isEmpty())) {
			value = annotatedType.getName();
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
		return new TitanCollectionPropertyMetadata();
	}

	@Override
	public TitanReferencePropertyMetadata createReferencePropertyMetadata(
			PropertyMethod propertyMethod) {
		VertexDefinition property = propertyMethod
				.getAnnotationOfProperty(VertexDefinition.class);
		String name = property != null ? property.value() : propertyMethod
				.getName();
		return new TitanReferencePropertyMetadata(name);
	}

	@Override
	public TitanPropertyMetadata createPropertyMetadata(
			PropertyMethod propertyMethod) {
		VertexDefinition property = propertyMethod
				.getAnnotationOfProperty(VertexDefinition.class);
		String name = property != null ? property.value() : propertyMethod
				.getName();
		return new TitanPropertyMetadata(name);
	}

	@Override
	public TitanIndexedPropertyMetadata createIndexedPropertyMetadata(
			PropertyMethod propertyMethod) {
		return new TitanIndexedPropertyMetadata();
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
			name = StringUtils.capitalize(annotatedElement.getName());
		}
		return new TitanRelationMetadata(name);
	}
}
