package com.puresoltechnologies.xo.titan.test.bootstrap;

import org.junit.Test;

import com.buschmais.cdo.api.CdoManager;
import com.buschmais.cdo.api.CdoManagerFactory;
import com.buschmais.cdo.api.bootstrap.Cdo;
import com.puresoltechnologies.xo.titan.test.bootstrap.composite.A;

public class TitanStoreBootstrapTest {

	@Test
	public void bootstrap() {
		CdoManagerFactory cdoManagerFactory = Cdo
				.createCdoManagerFactory("Titan");
		CdoManager cdoManager = cdoManagerFactory.createCdoManager();
		cdoManager.currentTransaction().begin();
		A a = cdoManager.create(A.class);
		a.setName("Test");
		cdoManager.currentTransaction().commit();
		cdoManagerFactory.close();
	}

}
