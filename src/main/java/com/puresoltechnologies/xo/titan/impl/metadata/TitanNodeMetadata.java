package com.puresoltechnologies.xo.titan.impl.metadata;

import com.buschmais.cdo.spi.datastore.DatastoreEntityMetadata;

public class TitanNodeMetadata implements DatastoreEntityMetadata<String> {

	private final String discriminator;

	public TitanNodeMetadata(String discriminator) {
		super();
		this.discriminator = discriminator;
	}

	@Override
	public String getDiscriminator() {
		return discriminator;
	}
}
