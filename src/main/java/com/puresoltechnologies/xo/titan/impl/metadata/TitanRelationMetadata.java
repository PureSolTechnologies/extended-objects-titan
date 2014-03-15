package com.puresoltechnologies.xo.titan.impl.metadata;

import com.buschmais.cdo.spi.datastore.DatastoreRelationMetadata;

public class TitanRelationMetadata implements DatastoreRelationMetadata<String> {

	@Override
	public String getDiscriminator() {
		return null;
	}

}
