package com.example.construct;

import org.junit.Test;

import java.util.NoSuchElementException;

import static com.example.constructs.Either.left;
import static com.example.constructs.Either.right;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

public class TestEither {
	@Test
	public void shouldQueryLeftPresence() {
		assertTrue(left("A Value").isLeft());
		assertFalse(right("A Value").isLeft());
	}

	@Test
	public void shouldCreateALeftValueAndMapOverIt() {
		assertThat(left("A Sample String").leftMap(String::length).leftValue(), is(equalTo(15)));
	}

	@Test
	public void shouldCreateRightValueButReturnLeftDefaultValueAsRequired() {
		assertThat(right("A Sample String").leftOrElse("DEFAULT"), is(equalTo("DEFAULT")));
	}

	@Test
	public void shouldCreateRightValueButReturnFromLeftSupplierAsRequired() {
		assertThat(right("A Sample String").leftOrElseGet(() -> "DEFAULT"), is(equalTo("DEFAULT")));
	}

	@Test(expected = NoSuchElementException.class)
	public void shouldThrowExceptionWhenInvokedRightOnLeft() {
		left("A Sample String").rightValue();
	}

	@Test
	public void shouldQueryRightPresence() {
		assertTrue(right("A Value").isRight());
		assertFalse(left("A Value").isRight());
	}

	@Test
	public void shouldCreateARightValueAndMapOverIt() {
		assertThat(right("A Sample String").map(String::length).rightValue(), is(equalTo(15)));
	}

	@Test
	public void shouldCreateARightValueAndFlatMapOverIt() {
		assertThat(right("A Sample String").flatMap(str -> right(str.length())).rightValue(), is(equalTo(15)));
	}

	@Test
	public void shouldCreateLeftValueButReturnRightDefaultValueAsRequired() {
		assertThat(left("A Sample String").orElse("DEFAULT"), is(equalTo("DEFAULT")));
	}

	@Test
	public void shouldCreateLeftValueButReturnFromRightSupplierAsRequired() {
		assertThat(left("A Sample String").orElseGet(() -> "DEFAULT"), is(equalTo("DEFAULT")));
	}

	@Test(expected = NoSuchElementException.class)
	public void shouldThrowExceptionWhenInvokedLeftOnRight() {
		right("A Sample String").leftValue();
	}

}
