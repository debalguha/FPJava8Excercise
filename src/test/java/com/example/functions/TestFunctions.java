package com.example.functions;


import org.junit.Test;

import java.util.Optional;

import static com.example.functions.Functions.*;
import static java.util.Optional.of;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TestFunctions {
    @Test
    public void shouldTestForNullOrEmptyLine() {
        assertTrue(of("blah blah").filter(notNullOrEmptyLinesPredicate).isPresent());
        assertFalse(Optional.<String>ofNullable(null).filter(notNullOrEmptyLinesPredicate).isPresent());
        assertFalse(of("").filter(notNullOrEmptyLinesPredicate).isPresent());
    }
    @Test
    public void shouldTestForCommentedLines() {
        assertTrue(of("bluh bluh").filter(unCommentedLinesPredicate).isPresent());
        assertFalse(Optional.<String>ofNullable(null).filter(unCommentedLinesPredicate).isPresent());
        assertTrue(of("").filter(unCommentedLinesPredicate).isPresent());
    }
    @Test
    public void shouldCheckForMandatoryColumns() {
        String input = "a,b,c,d,e,f";
        String input1 = "a,b,c,,e,f";
        assertTrue(mandatoryColumnsPredicateFunc.apply(new int[]{1,3,5}).test(input));
        assertFalse(mandatoryColumnsPredicateFunc.apply(new int[]{1,3,5}).test(input1));
        assertFalse(mandatoryColumnsPredicateFunc.apply(new int[]{1,3,5}).test(""));
        assertFalse(mandatoryColumnsPredicateFunc.apply(new int[]{1,3,5}).test(null));
    }
}
