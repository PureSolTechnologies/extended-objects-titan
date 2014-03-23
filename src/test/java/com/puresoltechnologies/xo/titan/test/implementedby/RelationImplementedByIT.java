package com.puresoltechnologies.xo.titan.test.implementedby;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import java.net.URISyntaxException;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import com.buschmais.cdo.api.CdoManager;
import com.buschmais.cdo.api.bootstrap.CdoUnit;
import com.puresoltechnologies.xo.titan.AbstractXOTitanTest;

@RunWith(Parameterized.class)
public class RelationImplementedByIT extends AbstractXOTitanTest {

	public RelationImplementedByIT(CdoUnit cdoUnit) {
		super(cdoUnit);
	}

	@Parameterized.Parameters
	public static Collection<Object[]> getCdoUnits() throws URISyntaxException {
		return cdoUnits(A.class, B.class, A2B.class);
	}

	@Test
	public void nonPropertyMethod() {
		CdoManager cdoManager = getCdoManagerFactory().createCdoManager();
		cdoManager.currentTransaction().begin();
		A a = cdoManager.create(A.class);
		B b = cdoManager.create(B.class);
		A2B a2b = cdoManager.create(a, A2B.class, b);
		a2b.setValue(1);
		int i = a2b.incrementValue();
		assertThat(i, equalTo(2));
		cdoManager.currentTransaction().commit();
		cdoManager.close();
	}

	@Test
	public void propertyMethods() {
		CdoManager cdoManager = getCdoManagerFactory().createCdoManager();
		cdoManager.currentTransaction().begin();
		A a = cdoManager.create(A.class);
		B b = cdoManager.create(B.class);
		A2B a2b = cdoManager.create(a, A2B.class, b);
		a2b.setCustomValue("VALUE");
		String value = a2b.getCustomValue();
		assertThat(value, equalTo("set_VALUE_get"));
		cdoManager.currentTransaction().commit();
		cdoManager.close();
	}

	@Test(expected = UnsupportedOperationException.class)
	public void unsupportedOperation() {
		CdoManager cdoManager = getCdoManagerFactory().createCdoManager();
		cdoManager.currentTransaction().begin();
		A a = cdoManager.create(A.class);
		B b = cdoManager.create(B.class);
		A2B a2b = cdoManager.create(a, A2B.class, b);
		cdoManager.currentTransaction().commit();
		cdoManager.currentTransaction().begin();
		try {
			a2b.unsupportedOperation();
		} finally {
			cdoManager.currentTransaction().commit();
		}
	}
}
