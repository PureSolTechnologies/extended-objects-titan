package com.puresoltechnologies.xo.titan.impl;

import java.util.Map;

import com.buschmais.cdo.spi.datastore.DatastoreMetadataFactory;
import com.buschmais.cdo.spi.metadata.type.TypeMetadata;
import com.buschmais.cdo.spi.reflection.AnnotatedElement;
import com.buschmais.cdo.spi.reflection.AnnotatedMethod;
import com.buschmais.cdo.spi.reflection.AnnotatedType;
import com.buschmais.cdo.spi.reflection.PropertyMethod;
import com.puresoltechnologies.xo.titan.impl.metadata.TitanNodeMetadata;
import com.puresoltechnologies.xo.titan.impl.metadata.TitanRelationMetadata;

public class TitanMetadataFactory
		implements
		DatastoreMetadataFactory<TitanNodeMetadata, String, TitanRelationMetadata, String> {

	@Override
	public TitanNodeMetadata createEntityMetadata(AnnotatedType annotatedType,
			Map<Class<?>, TypeMetadata> metadataByType) {
		return new TitanNodeMetadata(annotatedType.getAnnotatedElement()
				.getName());
	}

	@Override
	public <ImplementedByMetadata> ImplementedByMetadata createImplementedByMetadata(
			AnnotatedMethod annotatedMethod) {
		return null;
	}

	@Override
	public <CollectionPropertyMetadata> CollectionPropertyMetadata createCollectionPropertyMetadata(
			PropertyMethod propertyMethod) {
		return null;
	}

	@Override
	public <ReferencePropertyMetadata> ReferencePropertyMetadata createReferencePropertyMetadata(
			PropertyMethod propertyMethod) {
		return null;
	}

	@Override
	public <PrimitivePropertyMetadata> PrimitivePropertyMetadata createPropertyMetadata(
			PropertyMethod propertyMethod) {
		return null;
	}

	@Override
	public <IndexedPropertyMetadata> IndexedPropertyMetadata createIndexedPropertyMetadata(
			PropertyMethod propertyMethod) {
		return null;
	}

	@Override
	public TitanRelationMetadata createRelationMetadata(
			AnnotatedElement<?> annotatedElement,
			Map<Class<?>, TypeMetadata> metadataByType) {
		return null;
	}
}
