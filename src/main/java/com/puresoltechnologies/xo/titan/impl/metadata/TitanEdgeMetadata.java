package com.puresoltechnologies.xo.titan.impl.metadata;

import com.buschmais.xo.spi.datastore.DatastoreRelationMetadata;

public class TitanEdgeMetadata implements DatastoreRelationMetadata<String> {

	private final String label;

	public TitanEdgeMetadata(String label) {
		this.label = label;
	}

	@Override
	public String getDiscriminator() {
		return label;
	}
}
