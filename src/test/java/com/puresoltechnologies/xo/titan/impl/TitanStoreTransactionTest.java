package com.puresoltechnologies.xo.titan.impl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.Test;

import com.buschmais.cdo.api.CdoException;
import com.thinkaurelius.titan.core.TitanGraph;

/**
 * This unit test checks the logic for active state and initialization.
 * 
 * @author Rick-Rainer Ludwig
 */
public class TitanStoreTransactionTest {

	private final static TitanGraph titanGraph = mock(TitanGraph.class);
	private TitanStoreTransaction transaction;

	@Before
	public void initialize() {
		transaction = new TitanStoreTransaction(titanGraph);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testNullTitanGraph() {
		new TitanStoreTransaction(null);
	}

	@Test
	public void testBeginCommitActive() {
		assertFalse(transaction.isActive());
		transaction.begin();
		assertTrue(transaction.isActive());
		transaction.commit();
		assertFalse(transaction.isActive());
	}

	@Test
	public void testBeginRollbackActive() {
		assertFalse(transaction.isActive());
		transaction.begin();
		assertTrue(transaction.isActive());
		transaction.rollback();
		assertFalse(transaction.isActive());
	}

	@Test
	public void testBeginDoubleCommit() {
		transaction.begin();
		transaction.commit();
		transaction.commit();
	}

	@Test
	public void testBeginDoubleRollback() {
		transaction.begin();
		transaction.rollback();
		transaction.rollback();
	}

	@Test(expected = CdoException.class)
	public void testDoubleBegin() {
		transaction.begin();
		transaction.begin();
	}

}
