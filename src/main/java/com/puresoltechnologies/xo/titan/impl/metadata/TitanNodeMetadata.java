package com.puresoltechnologies.xo.titan.impl.metadata;

import com.buschmais.xo.spi.datastore.DatastoreEntityMetadata;

public class TitanNodeMetadata implements DatastoreEntityMetadata<String> {

	private final String discriminator;
	private final Class<?> useIndexOfType;

	public TitanNodeMetadata(String discriminator, Class<?> useIndexOfType) {
		super();
		this.discriminator = discriminator;
		this.useIndexOfType = useIndexOfType;
	}

	@Override
	public String getDiscriminator() {
		return discriminator;
	}

	public Class<?> getUseIndexOfType() {
		return useIndexOfType;
	}

}
