package com.puresoltechnologies.xo.titan.test.inheritance;

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
public class AnonymousSuperTypeIT extends AbstractXOTitanTest {

	public AnonymousSuperTypeIT(CdoUnit cdoUnit) {
		super(cdoUnit);
	}

	@Parameterized.Parameters
	public static Collection<Object[]> getCdoUnits() throws URISyntaxException {
		return cdoUnits(A.class, B.class);
	}

	@Test
	public void anonymousSuperType() {
		CdoManager cdoManager = getCdoManager();
		cdoManager.currentTransaction().begin();
		B b = cdoManager.create(B.class);
		b.setIndex("1");
		b.setVersion(1);
		cdoManager.currentTransaction().commit();
		cdoManager.currentTransaction().begin();
		A a = cdoManager.find(A.class, "1").iterator().next();
		assertThat(b, equalTo(a));
		assertThat(a.getVersion().longValue(), equalTo(1L));
		cdoManager.currentTransaction().commit();
	}
}
