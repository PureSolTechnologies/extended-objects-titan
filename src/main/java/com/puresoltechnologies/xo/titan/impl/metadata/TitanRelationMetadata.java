package com.puresoltechnologies.xo.titan.impl.metadata;

import com.buschmais.xo.spi.datastore.DatastoreRelationMetadata;

public class TitanRelationMetadata implements DatastoreRelationMetadata<String> {

	private final String label;

	public TitanRelationMetadata(String label) {
		this.label = label;
	}

	@Override
	public String getDiscriminator() {
		return label;
	}
}
