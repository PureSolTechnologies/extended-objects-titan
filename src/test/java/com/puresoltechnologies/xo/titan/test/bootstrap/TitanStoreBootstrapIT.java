package com.puresoltechnologies.xo.titan.test.bootstrap;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import com.buschmais.cdo.api.CdoManager;
import com.buschmais.cdo.api.Query;
import com.buschmais.cdo.api.bootstrap.CdoUnit;
import com.puresoltechnologies.xo.titan.AbstractXOTitanTest;

@RunWith(Parameterized.class)
public class TitanStoreBootstrapIT extends AbstractXOTitanTest {

	public TitanStoreBootstrapIT(CdoUnit cdoUnit) {
		super(cdoUnit);
	}

	@Parameterized.Parameters
	public static Collection<Object[]> getCdoUnits() throws IOException {
		return configuredCdoUnits();
	}

	@Test
	public void bootstrap() {
		CdoManager cdoManager = getCdoManager();

		cdoManager.currentTransaction().begin();
		TestEntity a = cdoManager.create(TestEntity.class);
		a.setName("Test");
		cdoManager.currentTransaction().commit();

		cdoManager.currentTransaction().begin();
		Query<TestEntity> query = cdoManager.createQuery(
				"_().has('name','Test')", TestEntity.class);
		TestEntity readA = query.execute().getSingleResult();
		assertEquals(a.getName(), readA.getName());
		cdoManager.currentTransaction().commit();
	}

}
