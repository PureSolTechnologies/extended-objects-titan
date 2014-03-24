package com.puresoltechnologies.xo.titan.test.mapping;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.net.URISyntaxException;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import com.buschmais.cdo.api.CdoManager;
import com.buschmais.cdo.api.Query;
import com.buschmais.cdo.api.Query.Result.CompositeRowObject;
import com.buschmais.cdo.api.bootstrap.CdoUnit;
import com.puresoltechnologies.xo.titan.AbstractXOTitanTest;

@RunWith(Parameterized.class)
public class EnumPropertyMappingT extends AbstractXOTitanTest {

	public EnumPropertyMappingT(CdoUnit cdoUnit) {
		super(cdoUnit);
	}

	@Parameterized.Parameters
	public static Collection<Object[]> getCdoUnits() throws URISyntaxException {
		return cdoUnits(A.class);
	}

	@Test
	public void enumerationLabel() {
		CdoManager cdoManager = getCdoManager();

		cdoManager.currentTransaction().begin();
		A a = cdoManager.create(A.class);
		a.setEnumeration(Enumeration.FIRST);
		cdoManager.currentTransaction().commit();

		cdoManager.currentTransaction().begin();
		assertThat(a.getEnumeration(), equalTo(Enumeration.FIRST));
		Query<CompositeRowObject> query = cdoManager
				.createQuery("_().has('_xo_discriminator_A').has('enumeration','FIRST').map");
		CompositeRowObject result = query.execute().getSingleResult();
		// assertThat(
		// executeQuery("MATCH (a:A) WHERE a.enumeration='FIRST' RETURN a")
		// .getColumn("a"), hasItem(a));
		assertThat(result.get("_xo_discriminator_A", String.class), is("A"));
		a.setEnumeration(Enumeration.SECOND);
		cdoManager.currentTransaction().commit();

		cdoManager.currentTransaction().begin();
		assertThat(a.getEnumeration(), equalTo(Enumeration.SECOND));
		query = cdoManager
				.createQuery("_().has('_xo_discriminator_A').has('enumeration','SECOND').map");
		result = query.execute().getSingleResult();
		// assertThat(
		// executeQuery(
		// "MATCH (a:A) WHERE a.enumeration='SECOND' RETURN a")
		// .getColumn("a"), hasItem(a));
		assertThat(result.get("_xo_discriminator_A", String.class), is("A"));
		a.setEnumeration(null);
		cdoManager.currentTransaction().commit();

		cdoManager.currentTransaction().begin();
		assertThat(a.getEnumeration(), equalTo(null));
		cdoManager.currentTransaction().commit();
	}

	@Test
	public void enumerationProperty() {
		CdoManager cdoManager = getCdoManager();

		cdoManager.currentTransaction().begin();
		A a = cdoManager.create(A.class);
		a.setMappedEnumeration(Enumeration.FIRST);
		cdoManager.currentTransaction().commit();

		cdoManager.currentTransaction().begin();
		assertThat(a.getMappedEnumeration(), equalTo(Enumeration.FIRST));
		Query<CompositeRowObject> query = cdoManager
				.createQuery("_().has('_xo_discriminator_A').has('MAPPED_ENUMERATION','FIRST').map");
		CompositeRowObject result = query.execute().getSingleResult();
		// assertThat(
		// executeQuery(
		// "MATCH (a:A) WHERE a.MAPPED_ENUMERATION='FIRST' RETURN a")
		// .getColumn("a"), hasItem(a));
		assertThat(result.get("_xo_discriminator_A", String.class), is("A"));
		a.setMappedEnumeration(Enumeration.SECOND);
		cdoManager.currentTransaction().commit();

		cdoManager.currentTransaction().begin();
		assertThat(a.getMappedEnumeration(), equalTo(Enumeration.SECOND));
		query = cdoManager
				.createQuery("_().has('_xo_discriminator_A').has('MAPPED_ENUMERATION','SECOND').map");
		result = query.execute().getSingleResult();
		// assertThat(
		// executeQuery(
		// "MATCH (a:A) WHERE a.MAPPED_ENUMERATION='SECOND' RETURN a")
		// .getColumn("a"), hasItem(a));
		assertThat(result.get("_xo_discriminator_A", String.class), is("A"));
		a.setMappedEnumeration(null);
		cdoManager.currentTransaction().commit();

		cdoManager.currentTransaction().begin();
		assertThat(a.getMappedEnumeration(), equalTo(null));
		cdoManager.currentTransaction().commit();
	}
}
