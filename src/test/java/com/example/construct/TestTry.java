package com.example.construct;

import org.junit.Test;

import java.util.Arrays;

import static com.example.constructs.Try.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

public class TestTry {
	@Test
	public void shouldCheckIfSuccess() {
		assertTrue(success("SUCCESS").isSuccess());
		assertFalse(failure(new RuntimeException("Blah Blah")).isSuccess());
	}

	@Test
	public void shouldMapSuccessValue() {
		assertThat(success("SUCCESS").map(String::length).rightValue(), is(equalTo(7)));
	}

	@Test
	public void shouldCheckIfFailure() {
		assertTrue(failure(new RuntimeException("Blah Blah")).isFailure());
		assertFalse(success("SUCCESS").isFailure());
	}

	@Test
	public void shouldMapAnExceptionToAnother() {
		assertThat(failure(new RuntimeException("Blah Blah")).mapException(excep -> new IllegalStateException(excep)).exception(), isA(IllegalStateException.class));
	}

	@Test
	public void shouldTryAnOperationAndMapException() {
		assertThat(doTry(() -> String.valueOf(null).trim(), NullPointerException.class).exception(), isA(NullPointerException.class));
	}

	@Test
	public void shouldTryAnOperationAndYieldAValue() {
		assertThat(doTry(() -> String.join(",", Arrays.asList("a", "b", "c"))).value(), is(equalTo("a,b,c")));
	}

	@Test
	public void shouldTryAFailingOperationAndYieldADefaultValue() {
		assertThat(doTry(() -> String.valueOf(null).trim()).valueOrElse("DEFAULT"), is(equalTo("DEFAULT")));
	}

	@Test
	public void shouldTryAFailingOperationAndYieldADefaultValueFromSupplier() {
		assertThat(doTry(() -> String.valueOf(null).trim()).valueOrElseGet(() -> "DEFAULT"), is(equalTo("DEFAULT")));
	}

	@Test
	public void shouldTryAnOperationOrLogErrorInConsumer() {
		StringBuilder loggingMessageBuilder = new StringBuilder();
		doTry(() -> String.valueOf(null).trim()).logOnFailure(e -> loggingMessageBuilder.append(e.getMessage()));
		assertThat(loggingMessageBuilder.toString().length(), is(greaterThan(0)));
	}

	@Test(expected = NullPointerException.class)
	public void shouldThrowAnExceptionWhenComputationFailed() {
		doTry(() -> String.valueOf(null).trim()).orElseThrow();
	}

	@Test(expected = NullPointerException.class)
	public void shouldReturnAValueOrThrowTheUnderlyingExceptionIfAny() {
		assertThat(doTry(() -> "   SOME   ".trim()).orElseThrow(), is(equalTo("SOME")));
		doTry(() -> String.valueOf(null).trim()).orElseThrow();
	}

}
