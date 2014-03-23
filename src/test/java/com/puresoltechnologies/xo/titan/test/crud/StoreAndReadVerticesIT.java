package com.puresoltechnologies.xo.titan.test.crud;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import com.buschmais.cdo.api.CdoManager;
import com.buschmais.cdo.api.ResultIterable;
import com.buschmais.cdo.api.ResultIterator;
import com.buschmais.cdo.api.bootstrap.CdoUnit;
import com.puresoltechnologies.xo.titan.AbstractXOTitanTest;
import com.puresoltechnologies.xo.titan.test.bootstrap.TestEntity;

@RunWith(Parameterized.class)
public class StoreAndReadVerticesIT extends AbstractXOTitanTest {

	public StoreAndReadVerticesIT(CdoUnit cdoUnit) {
		super(cdoUnit);
	}

	@Parameterized.Parameters
	public static Collection<Object[]> getCdoUnits() throws IOException {
		return cdoUnits();
	}

	@Test
	public void test() {
		CdoManager cdoManager = getCdoManager();
		cdoManager.currentTransaction().begin();
		TestEntity createdA = cdoManager.create(TestEntity.class);
		createdA.setName("Test");
		cdoManager.currentTransaction().commit();

		cdoManager.currentTransaction().begin();
		ResultIterable<TestEntity> aa = cdoManager.find(TestEntity.class,
				"Test");
		assertNotNull(aa);
		ResultIterator<TestEntity> iterator = aa.iterator();
		assertTrue(iterator.hasNext());
		TestEntity readA = iterator.next();
		assertNotNull(readA);
		assertEquals("Test", readA.getName());
		cdoManager.currentTransaction().rollback();
	}

}
