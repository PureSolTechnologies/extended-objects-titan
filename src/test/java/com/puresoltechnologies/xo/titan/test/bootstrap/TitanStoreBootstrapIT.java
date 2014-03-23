package com.puresoltechnologies.xo.titan.test.bootstrap;

import java.io.IOException;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import com.buschmais.cdo.api.CdoManager;
import com.buschmais.cdo.api.bootstrap.CdoUnit;
import com.puresoltechnologies.xo.titan.test.AbstractXOTitanTest;

@RunWith(Parameterized.class)
public class TitanStoreBootstrapIT extends AbstractXOTitanTest {

	public TitanStoreBootstrapIT(CdoUnit cdoUnit) {
		super(cdoUnit);
	}

	@Parameterized.Parameters
	public static Collection<Object[]> getCdoUnits() throws IOException {
		return cdoUnits();
	}

	@Test
	public void bootstrap() {
		CdoManager cdoManager = getCdoManager();
		cdoManager.currentTransaction().begin();
		TestEntity a = cdoManager.create(TestEntity.class);
		a.setName("Test");
		cdoManager.currentTransaction().commit();
	}

}
