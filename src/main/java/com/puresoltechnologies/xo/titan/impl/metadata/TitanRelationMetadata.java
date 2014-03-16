package com.puresoltechnologies.xo.titan.impl.metadata;

import com.buschmais.cdo.spi.datastore.DatastoreRelationMetadata;

public class TitanRelationMetadata implements DatastoreRelationMetadata<String> {

	private final String discriminator;

	public TitanRelationMetadata(String discriminator) {
		super();
		this.discriminator = discriminator;
	}

	@Override
	public String getDiscriminator() {
		return discriminator;
	}

}
