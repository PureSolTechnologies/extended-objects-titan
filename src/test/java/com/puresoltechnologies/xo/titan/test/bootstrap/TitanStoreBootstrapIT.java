package com.puresoltechnologies.xo.titan.test.bootstrap;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.buschmais.cdo.api.CdoManager;
import com.buschmais.cdo.api.CdoManagerFactory;
import com.buschmais.cdo.api.bootstrap.Cdo;
import com.puresoltechnologies.xo.titan.test.AbstractXOTitanTest;

public class TitanStoreBootstrapIT extends AbstractXOTitanTest {

	private static CdoManagerFactory cdoManagerFactory;
	private CdoManager cdoManager;

	@BeforeClass
	public static void initialize() {
		cdoManagerFactory = Cdo.createCdoManagerFactory("Titan");
	}

	@AfterClass
	public static void teardown() {
		if (cdoManagerFactory != null) {
			cdoManagerFactory.close();
		}
	}

	@Before
	public void setup() {
		cdoManager = cdoManagerFactory.createCdoManager();
	}

	@After
	public void destroy() {
		cdoManager.close();
	}

	@Test
	public void bootstrap() {
		cdoManager.currentTransaction().begin();
		TestEntity a = cdoManager.create(TestEntity.class);
		a.setName("Test");
		cdoManager.currentTransaction().commit();
	}

}
