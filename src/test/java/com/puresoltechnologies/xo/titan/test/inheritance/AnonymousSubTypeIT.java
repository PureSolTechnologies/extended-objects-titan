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
public class AnonymousSubTypeIT extends AbstractXOTitanTest {

	public AnonymousSubTypeIT(CdoUnit cdoUnit) {
		super(cdoUnit);
	}

	@Parameterized.Parameters
	public static Collection<Object[]> getCdoUnits() throws URISyntaxException {
		return cdoUnits(D.class);
	}

	@Test
	public void anonymousSubType() {
		CdoManager cdoManager = getCdoManager();
		cdoManager.currentTransaction().begin();
		D b = cdoManager.create(D.class);
		b.setIndex("1");
		cdoManager.currentTransaction().commit();

		cdoManager.currentTransaction().begin();
		A a = cdoManager.find(A.class, "1").iterator().next();
		assertThat(a.getIndex(), equalTo("1"));
		cdoManager.currentTransaction().commit();
	}

}
