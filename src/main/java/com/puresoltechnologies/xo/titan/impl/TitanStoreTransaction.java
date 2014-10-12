package com.puresoltechnologies.xo.titan.impl;

import com.buschmais.xo.api.XOException;
import com.buschmais.xo.spi.datastore.DatastoreTransaction;
import com.thinkaurelius.titan.core.TitanGraph;

/**
 * This class implements an XO {@link DatastoreTransaction} for Titan databases.
 * 
 * @author Rick-Rainer Ludwig
 */
public class TitanStoreTransaction implements DatastoreTransaction {

	/**
	 * This field stores whether the transaction is currently active or not.
	 */
	private boolean active = false;

	/**
	 * This method contains the Titan graph as {@link TitanGraph} to handle on
	 * it its transactions.
	 */
	private final TitanGraph titanGraph;

	/**
	 * This is the initial value constructor.
	 * 
	 * @param titanGraph
	 *            is the Titan graph as {@link TitanGraph} object on which this
	 *            transaction shall work on.
	 */
	public TitanStoreTransaction(TitanGraph titanGraph) {
		if (titanGraph == null) {
			throw new IllegalArgumentException("titanGraph must not be null");
		}
		this.titanGraph = titanGraph;
	}

	@Override
	public void begin() {
		if (active) {
			throw new XOException("There is already an active transaction.");
		}
		active = true;
	}

	@Override
	public void commit() {
		if (!active) {
			throw new XOException("There is no active transaction.");
		}
		active = false;
		titanGraph.commit();
	}

	@Override
	public void rollback() {
		if (!active) {
			throw new XOException("There is no active transaction.");
		}
		active = false;
		titanGraph.rollback();
	}

	@Override
	public boolean isActive() {
		return active;
	}
}
