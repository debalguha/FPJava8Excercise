package com.example.constructs;

import java.util.NoSuchElementException;
import java.util.function.Function;
import java.util.function.Supplier;

public interface Either<L, R> {
	boolean isLeft();
	boolean isRight();
	L leftValue();
	R rightValue();

	default R orElse(R otherValue) {
		return isRight() ? rightValue() : otherValue;
	}

	default R orElseGet(Supplier<R> supplier) {
		return isRight() ? rightValue() : supplier.get();
	}

	default <R1> Either<L, R1> map(Function<R, R1> mapFunc) {
		return isRight() ? right(mapFunc.apply(rightValue())): left(leftValue());
	}

	default <R1> Either<L, R1> flatMap(Function<R, Either<L, R1>> bindFunc) {
		return isRight() ? bindFunc.apply(rightValue()) : left(leftValue());
	}

	default L leftOrElse(L otherValue) {
		return isLeft() ? leftValue() : otherValue;
	}
	default L leftOrElseGet(Supplier<L> leftValueSupplier) {
		return isLeft() ? leftValue() : leftValueSupplier.get();
	}


	default <L1> Either<L1, R> leftMap(Function<L, L1> mapFunc) {
		return isLeft() ? left(mapFunc.apply(leftValue())): right(rightValue());
	}

	static <L1, R1> Either<L1, R1> left(L1 value){
		return new LeftProjection<>(value);
	}

	static <L1, R1> Either<L1, R1> right(R1 value){
		return new RightProjection<>(value);
	}
}

class RightProjection<L, R> implements Either<L, R> {

	private final R value;
	RightProjection(R value){
		this.value = value;
	}

	@Override
	public boolean isLeft() {
		return false;
	}

	@Override
	public boolean isRight() {
		return true;
	}

	@Override
	public L leftValue() {
		throw new NoSuchElementException("Either.leftValue on Right");
	}

	@Override
	public R rightValue() {
		return value;
	}
}

class LeftProjection<L, R> implements Either<L, R> {

	private final L value;
	LeftProjection(L value){
		this.value = value;
	}

	@Override
	public boolean isLeft() {
		return true;
	}

	@Override
	public boolean isRight() {
		return false;
	}

	@Override
	public L leftValue() {
		return value;
	}

	@Override
	public R rightValue() {
		throw new NoSuchElementException("Either.rightValue on Left");
	}
}