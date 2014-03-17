package com.puresoltechnologies.xo.titan.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.buschmais.cdo.api.CdoManager;
import com.buschmais.cdo.api.CdoManagerFactory;
import com.buschmais.cdo.api.ResultIterable;
import com.buschmais.cdo.api.ResultIterator;
import com.buschmais.cdo.api.bootstrap.Cdo;
import com.puresoltechnologies.xo.titan.test.bootstrap.composite.A;

public class StoreAndReadVerticesIT {

	@Test
	public void test() {
		CdoManagerFactory cdoManagerFactory = Cdo
				.createCdoManagerFactory("Titan");
		CdoManager cdoManager = cdoManagerFactory.createCdoManager();
		cdoManager.currentTransaction().begin();

		A a = cdoManager.create(A.class);
		a.setName("Test");
		cdoManager.currentTransaction().commit();

		cdoManager.currentTransaction().begin();

		ResultIterable<A> aa = cdoManager.find(A.class, "Test");
		assertNotNull(aa);
		ResultIterator<A> iterator = aa.iterator();
		assertTrue(iterator.hasNext());
		assertNotNull(iterator.next());

		cdoManager.currentTransaction().rollback();

		cdoManagerFactory.close();
	}

}
