package com.puresoltechnologies.xo.titan.test.concurrency;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import com.buschmais.cdo.api.CdoManager;
import com.buschmais.cdo.api.ConcurrencyMode;
import com.buschmais.cdo.api.Transaction;
import com.buschmais.cdo.api.ValidationMode;
import com.buschmais.cdo.api.bootstrap.CdoUnit;
import com.puresoltechnologies.xo.titan.AbstractXOTitanTest;

@RunWith(Parameterized.class)
public class MultithreadedIT extends AbstractXOTitanTest {

	public MultithreadedIT(CdoUnit cdoUnit) {
		super(cdoUnit);
	}

	@Parameterized.Parameters
	public static Collection<Object[]> getCdoUnits() {
		return cdoUnits(asList(TestEntity.class), Collections.<Class<?>> emptyList(),
				ValidationMode.AUTO, ConcurrencyMode.MULTITHREADED,
				Transaction.TransactionAttribute.REQUIRES);
	}

	@Test
	public void instance() throws ExecutionException, InterruptedException {
		CdoManager cdoManager = getCdoManager();
		cdoManager.currentTransaction().begin();
		TestEntity a = cdoManager.create(TestEntity.class);
		cdoManager.currentTransaction().commit();
		ExecutorService executorService = Executors.newCachedThreadPool();
		Future<Integer> future1 = executorService.submit(new Worker(a));
		TimeUnit.SECONDS.sleep(1);
		Future<Integer> future2 = executorService.submit(new Worker(a));
		assertThat(future1.get(), equalTo(1));
		assertThat(future2.get(), equalTo(2));
	}

	private static class Worker implements Callable<Integer> {

		private final TestEntity a;

		private Worker(TestEntity a) {
			this.a = a;
		}

		@Override
		public Integer call() throws Exception {
			return a.incrementAndGet();
		}
	}
}
