package com.puresoltechnologies.xo.titan.impl;

import com.buschmais.xo.api.XOException;
import com.buschmais.xo.spi.datastore.DatastoreTransaction;
import com.thinkaurelius.titan.core.TitanGraph;
import com.thinkaurelius.titan.core.TitanTransaction;

/**
 * This class implements an XO {@link DatastoreTransaction} for Titan databases.
 * 
 * @author Rick-Rainer Ludwig
 */
public class TitanStoreTransaction implements DatastoreTransaction {

	/**
	 * This method contains the Titan graph as {@link TitanGraph} to handle on
	 * it its transactions.
	 */
	private final TitanGraph titanGraph;

	/**
	 * This field contains a reference to a lock.
	 */
	private TitanTransaction transaction = null;

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
	public synchronized void begin() {
		if (transaction != null) {
			throw new XOException("There is already an active transaction.");
		}
		transaction = titanGraph.newTransaction();
	}

	@Override
	public synchronized void commit() {
		if (transaction == null) {
			throw new XOException("There is no active transaction.");
		}
		transaction.commit();
		transaction = null;
	}

	@Override
	public synchronized void rollback() {
		if (transaction == null) {
			throw new XOException("There is no active transaction.");
		}
		transaction.rollback();
		transaction = null;
	}

	@Override
	public synchronized boolean isActive() {
		return transaction != null && transaction.isOpen();
	}
}
