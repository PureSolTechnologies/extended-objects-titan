package com.puresoltechnologies.xo.titan.impl.metadata;

import com.buschmais.xo.spi.datastore.DatastoreEntityMetadata;
import com.buschmais.xo.spi.metadata.method.IndexedPropertyMethodMetadata;

public class TitanVertexMetadata implements DatastoreEntityMetadata<String> {

	private final String discriminator;
	private final IndexedPropertyMethodMetadata<?> indexedProperty;

	public TitanVertexMetadata(String discriminator,
			IndexedPropertyMethodMetadata<?> indexedProperty) {
		super();
		this.discriminator = discriminator;
		this.indexedProperty = indexedProperty;
	}

	@Override
	public String getDiscriminator() {
		return discriminator;
	}

	public IndexedPropertyMethodMetadata<?> getIndexedProperty() {
		return indexedProperty;
	}

}
