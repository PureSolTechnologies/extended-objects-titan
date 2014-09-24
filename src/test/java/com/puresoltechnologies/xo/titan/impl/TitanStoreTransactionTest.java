package com.puresoltechnologies.xo.titan.impl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import com.buschmais.xo.api.XOException;
import com.thinkaurelius.titan.core.TitanGraph;
import com.thinkaurelius.titan.core.TitanTransaction;

/**
 * This unit test checks the logic for active state and initialization.
 * 
 * @author Rick-Rainer Ludwig
 */
public class TitanStoreTransactionTest {

	private final static TitanGraph titanGraphMock = mock(TitanGraph.class);

	@Before
	public void initialize() {
		TitanTransaction transactionMock = mock(TitanTransaction.class);
		when(transactionMock.isOpen()).thenReturn(true);
		when(transactionMock.isClosed()).thenReturn(false);
		when(titanGraphMock.newTransaction()).thenReturn(transactionMock);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testNullTitanGraph() {
		new TitanStoreTransaction(null);
	}

	@Test
	public void testBeginCommitActive() {
		TitanStoreTransaction transaction = new TitanStoreTransaction(
				titanGraphMock);
		assertFalse(transaction.isActive());
		transaction.begin();
		assertTrue(transaction.isActive());
		transaction.commit();
		assertFalse(transaction.isActive());
	}

	@Test
	public void testBeginRollbackActive() {
		TitanStoreTransaction transaction = new TitanStoreTransaction(
				titanGraphMock);
		assertFalse(transaction.isActive());
		transaction.begin();
		assertTrue(transaction.isActive());
		transaction.rollback();
		assertFalse(transaction.isActive());
	}

	@Test(expected = XOException.class)
	public void testBeginDoubleCommit() {
		TitanStoreTransaction transaction = new TitanStoreTransaction(
				titanGraphMock);
		assertFalse(transaction.isActive());
		transaction.begin();
		assertTrue(transaction.isActive());
		transaction.commit();
		assertFalse(transaction.isActive());
		transaction.commit();
	}

	@Test(expected = XOException.class)
	public void testBeginDoubleRollback() {
		TitanStoreTransaction transaction = new TitanStoreTransaction(
				titanGraphMock);
		assertFalse(transaction.isActive());
		transaction.begin();
		assertTrue(transaction.isActive());
		transaction.rollback();
		assertFalse(transaction.isActive());
		transaction.rollback();
	}

	@Test(expected = XOException.class)
	public void testDoubleBegin() {
		TitanStoreTransaction transaction = new TitanStoreTransaction(
				titanGraphMock);
		assertFalse(transaction.isActive());
		transaction.begin();
		assertTrue(transaction.isActive());
		transaction.begin();
	}

}
