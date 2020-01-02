package com.example.constructs;

import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public interface Try<E extends RuntimeException, V> extends Either<E, V> {

	boolean isSuccess();

	boolean isFailure();

	V value();

	E exception();

	@Override
	default boolean isLeft() {
		return isFailure();
	}

	@Override
	default boolean isRight() {
		return isSuccess();
	}

	@Override
	default E leftValue() {
		return exception();
	}

	@Override
	default V rightValue() {
		return value();
	}

	default <E1 extends RuntimeException> Try<E1, V> mapException(Function<E, E1> exceptionMapper) {
		return isFailure() ? failure(exceptionMapper.apply(exception())) : success(value());
	}

	default Try<E, V> logOnFailure(Consumer<E> loggingConsumer) {
		if (isFailure()) {
			loggingConsumer.accept(exception());
		}
		return this;
	}

	default V valueOrElse(V v) {
		return orElse(v);
	}

	default V valueOrElseGet(Supplier<V> valueSupplier) {
		return orElseGet(valueSupplier);
	}

	default V orElseThrow() {
		if (isFailure()) {
			throw exception();
		}
		return value();
	}
	static <E extends RuntimeException, V> Try<E, V> doTry(Supplier<V> operation) {
		try {
			return Try.success(operation.get());
		} catch (RuntimeException t) {
			return Try.failure((E) t);
		}
	}
	static <E extends RuntimeException, V> Try<E, V> doTry(Supplier<V> operation, Class<E> exceptionClass) {
		return doTry(operation).mapException(exceptionClass::cast);
	}

	static <E extends RuntimeException, V> Try<E, V> failure(E t) {
		return new Failure(t);
	}

	static <E extends RuntimeException, V> Try<E, V> success(V v) {
		return new Success(v);
	}
}

final class Success<E extends RuntimeException, V> implements Try<E, V> {

	private final V v;

	Success(V t) {
		this.v = t;
	}

	@Override
	public boolean isSuccess() {
		return true;
	}

	@Override
	public boolean isFailure() {
		return false;
	}

	@Override
	public V value() {
		return v;
	}

	@Override
	public E exception() {
		throw new NoSuchElementException("Try.exception on Success");
	}

}

final class Failure<E extends RuntimeException, V> implements Try<E, V> {

	private final E cause;

	Failure(E e) {
		this.cause = e;
	}

	@Override
	public boolean isSuccess() {
		return false;
	}

	@Override
	public boolean isFailure() {
		return true;
	}

	@Override
	public V value() {
		throw new NoSuchElementException("Try.value on Failure");
	}

	@Override
	public E exception() {
		return cause;
	}

}