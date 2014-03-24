package com.puresoltechnologies.xo.titan.test.mapping;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import java.net.URISyntaxException;
import java.util.Collection;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import com.buschmais.cdo.api.CdoManager;
import com.buschmais.cdo.api.bootstrap.CdoUnit;
import com.puresoltechnologies.xo.titan.AbstractXOTitanTest;

@Ignore("Not fully implemented, yet.")
@RunWith(Parameterized.class)
public class IndexMappingIT extends AbstractXOTitanTest {

	public IndexMappingIT(CdoUnit cdoUnit) {
		super(cdoUnit);
	}

	@Parameterized.Parameters
	public static Collection<Object[]> getCdoUnits() throws URISyntaxException {
		return cdoUnits(A.class, D.class);
	}

	@Test
	public void indexedProperty() {
		CdoManager cdoManager = getCdoManager();
		cdoManager.currentTransaction().begin();
		A a1 = cdoManager.create(A.class);
		a1.setIndex("1");
		A a2 = cdoManager.create(A.class);
		a2.setIndex("2");
		cdoManager.currentTransaction().commit();
		cdoManager.currentTransaction().begin();
		assertThat(cdoManager.find(A.class, "1").iterator().next(), equalTo(a1));
		assertThat(cdoManager.find(A.class, "2").iterator().next(), equalTo(a2));
		assertThat(cdoManager.find(A.class, "3").iterator().hasNext(),
				equalTo(false));
		cdoManager.currentTransaction().commit();
	}

	@Test
	public void useIndexOf() {
		CdoManager cdoManager = getCdoManager();
		cdoManager.currentTransaction().begin();
		A a1 = cdoManager.create(D.class);
		a1.setIndex("1");
		cdoManager.currentTransaction().commit();
		cdoManager.currentTransaction().begin();
		assertThat(cdoManager.find(D.class, "1").iterator().next(), equalTo(a1));
		cdoManager.currentTransaction().commit();
	}
}
