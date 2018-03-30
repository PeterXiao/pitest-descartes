package eu.stamp_project.mutationtest.descartes.operators;


import eu.stamp_project.mutationtest.test.Calculator;
import eu.stamp_project.mutationtest.test.TestUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;
import org.pitest.reloc.asm.commons.Method;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class MutationOperatorTest {

    Collection<Method> targets;

    @Before
    public void initialize() {
        targets = TestUtils.getMethods(Calculator.class);
    }

    @Parameter
    public String operatorID;

    @Parameter(1)
    public String expectedMethod;

    @Parameters(name="{index}: Searching methods with operator: {0}")
    public static Collection<Object[]> parameters() {
        return Arrays.asList( new Object[][]{
                {"void"       , "clear"},
                {"1"          , "getCeiling"},
                {"(byte)2"    , "getByte"},
                {"(short)3"   , "getShort"},
                {"23456L"     , "getSquare"},
                {"'c'"        , "getRandomOperatorSymbol"},
                {"3.14"       , "add"},
                {"1.2f"       , "getSomething"},
                {"true"       , "isOdd"},
                {"\"string\"" , "getScreen"},
        });
    }

    @Test
    public void shouldFilterMethods() {
        MutationOperator operator = MutationOperator.fromID(operatorID);
        for (Method method: targets) {
            if(operator.canMutate(method))
                assertEquals("Wrong method accepted", expectedMethod, method.getName());
        }
    }



}
