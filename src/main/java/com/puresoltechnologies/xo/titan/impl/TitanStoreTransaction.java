package com.puresoltechnologies.xo.titan.impl;

import com.buschmais.cdo.api.CdoException;
import com.buschmais.cdo.spi.datastore.DatastoreTransaction;
import com.thinkaurelius.titan.core.TitanGraph;

public class TitanStoreTransaction implements DatastoreTransaction {

	private boolean active = false;

	private final TitanGraph titanGraph;

	public TitanStoreTransaction(TitanGraph titanGraph) {
		this.titanGraph = titanGraph;
	}

	@Override
	public void begin() {
		if (active) {
			throw new CdoException("There is already an active transaction.");
		}
		active = true;
	}

	@Override
	public void commit() {
		active = false;
		titanGraph.commit();
	}

	@Override
	public void rollback() {
		active = false;
		titanGraph.rollback();
	}

	@Override
	public boolean isActive() {
		return active;
	}
}
