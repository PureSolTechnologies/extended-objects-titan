package com.puresoltechnologies.xo.titan.api;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.net.URI;
import java.net.URISyntaxException;

import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

import com.buschmais.cdo.api.CdoException;
import com.buschmais.cdo.api.bootstrap.CdoUnit;

public class TitanXOProviderTest {

	private static TitanXOProvider titanXOProvider = null;

	@BeforeClass
	public static void initialize() {
		titanXOProvider = new TitanXOProvider();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testNullXOUnit() {
		titanXOProvider.createDatastore(null);
	}

	@Test(expected = CdoException.class)
	public void testNullURI() {
		CdoUnit cdoUnit = Mockito.mock(CdoUnit.class);
		when(cdoUnit.getUri()).thenReturn(null);
		titanXOProvider.createDatastore(cdoUnit);
	}

	@Test(expected = CdoException.class)
	public void testIllegalProtocol() throws URISyntaxException {
		CdoUnit cdoUnit = mock(CdoUnit.class);
		URI uri = new URI("illegal-titan-cassandra://titan");
		when(cdoUnit.getUri()).thenReturn(uri);
		titanXOProvider.createDatastore(cdoUnit);
	}
}
