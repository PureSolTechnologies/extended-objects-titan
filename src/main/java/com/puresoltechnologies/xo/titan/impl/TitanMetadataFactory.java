package com.puresoltechnologies.xo.titan.impl;

import java.util.Map;

import com.buschmais.cdo.spi.datastore.DatastoreMetadataFactory;
import com.buschmais.cdo.spi.metadata.type.TypeMetadata;
import com.buschmais.cdo.spi.reflection.AnnotatedElement;
import com.buschmais.cdo.spi.reflection.AnnotatedMethod;
import com.buschmais.cdo.spi.reflection.AnnotatedType;
import com.buschmais.cdo.spi.reflection.PropertyMethod;
import com.puresoltechnologies.xo.titan.api.annotation.VertexType;
import com.puresoltechnologies.xo.titan.impl.metadata.TitanCollectionPropertyMetadata;
import com.puresoltechnologies.xo.titan.impl.metadata.TitanIndexedPropertyMetadata;
import com.puresoltechnologies.xo.titan.impl.metadata.TitanNodeMetadata;
import com.puresoltechnologies.xo.titan.impl.metadata.TitanPrimitivePropertyMetadata;
import com.puresoltechnologies.xo.titan.impl.metadata.TitanReferencePropertyMetadata;
import com.puresoltechnologies.xo.titan.impl.metadata.TitanRelationMetadata;

public class TitanMetadataFactory
		implements
		DatastoreMetadataFactory<TitanNodeMetadata, String, TitanRelationMetadata, String> {

	@Override
	public TitanNodeMetadata createEntityMetadata(AnnotatedType annotatedType,
			Map<Class<?>, TypeMetadata> metadataByType) {
		VertexType annotation = annotatedType.getAnnotation(VertexType.class);
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
		return new TitanReferencePropertyMetadata();
	}

	@Override
	public TitanPrimitivePropertyMetadata createPropertyMetadata(
			PropertyMethod propertyMethod) {
		return new TitanPrimitivePropertyMetadata();
	}

	@Override
	public TitanIndexedPropertyMetadata createIndexedPropertyMetadata(
			PropertyMethod propertyMethod) {
		TitanIndexedPropertyMetadata titanIndexedPropertyMetadata = new TitanIndexedPropertyMetadata();
		return titanIndexedPropertyMetadata;
	}

	@Override
	public TitanRelationMetadata createRelationMetadata(
			AnnotatedElement<?> annotatedElement,
			Map<Class<?>, TypeMetadata> metadataByType) {
		return new TitanRelationMetadata();
	}
}
