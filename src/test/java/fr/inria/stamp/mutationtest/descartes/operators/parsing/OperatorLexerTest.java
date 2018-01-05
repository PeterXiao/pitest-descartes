package fr.inria.stamp.mutationtest.descartes.operators.parsing;

import java.io.IOException;
import java.io.StringReader;

import static org.junit.Assert.*;
import org.junit.Test;

public class OperatorLexerTest {

    @Test
    public void shouldMatchConstantTokens() {
        TokenType[] types = {
                                TokenType.VOID_KWD,
                                TokenType.NULL_KWD,
                                TokenType.TRUE_KWD,
                                TokenType.FALSE_KWD,
                                TokenType.EMPTY_KWD,
                                TokenType.LPAR,
                                TokenType.BYTE_KWD,
                                TokenType.RPAR,
                                TokenType.SHORT_KWD,
                                TokenType.EOF
                            };
        String input = "void null true false empty ( byte ) short";
        OperatorLexer lexer = new OperatorLexer(new StringReader(input));

        try {
            for (TokenType token :
                    types) {
                assertEquals("Failed to match: " + token.name(), token, lexer.nextToken().getType());
            }
        }catch(IOException exc){
            fail("Unexpected exception: " + exc.getMessage());
        }
    }

    @Test
    public void shouldMatchIntegerLiterals() {
        String input = "1 0b1101 0b1_0_0 0B1 0XFF 0xA_a 0111 07_1_1";
        int[] values = {1, 13, 4, 1, 255, 170, 73, 457};
        OperatorLexer lexer = new OperatorLexer(new StringReader(input));
        try{
            for(int i=0; i < values.length; i++) {
                Token token = lexer.nextToken();
                assertEquals(TokenType.INT_LITERAL, token.getType());
                assertEquals(values[i], token.getData());
            }
        }catch(IOException exc) {
            fail("Unexpected exception: " + exc.getMessage());
        }
    }

    @Test
    public void shouldMatchLongLiterals() {
        String input = "12_345_678_910L  0133767016076l 0x2DFDC1C3El";
        long value = 12345678910L;
        OperatorLexer lexer = new OperatorLexer(new StringReader(input));
        try {
            Token lookahead = lexer.nextToken();
            while (lookahead.getType() != TokenType.EOF) {
                assertEquals(TokenType.LONG_LITERAL, lookahead.getType());
                assertEquals(value, lookahead.getData());
                lookahead = lexer.nextToken();
            }
        }catch(IOException exc) {
            fail("Unexpected exception: " + exc.getMessage());
        }
    }

    @Test
    public void shouldMatchStringLiterals() {
        String[] inputs = {
                "\"This is a string\"",
                "\"\\123\"",
                "\"\\uFFAA\""
        };
        String[] values = {
                "This is a string",
                "\123",
                "\uFFAA"
        };
        for(int i=0; i<inputs.length; i++) {

            try {
                OperatorLexer lexer = new OperatorLexer(new StringReader(inputs[i]));
                Token token = lexer.nextToken();
                assertEquals(TokenType.STRING_LITERAL, token.getType());
                assertEquals(values[i], token.getData());
            } catch (IOException exc) {
                fail("Unexpected exception: " + exc.getMessage());
            }
        }

    }

    @Test
    public void shouldMatchCharLiterals() {
        String input    = "'a'  '\\r' '\\n' '\\t' '\\b' '\\f' '\\'' '\\\\' '\\7' '\\67' '\\354' '\\u1234'";
        char[] expected = {'a', '\r', '\n', '\t', '\b', '\f', '\'', '\\',  '\7', '\67', '\354' ,'\u1234'};


        try {
            OperatorLexer lexer = new OperatorLexer(new StringReader(input));
            for (int i = 0; i < expected.length; i++) {
                Token token = lexer.nextToken();
                assertEquals(TokenType.CHAR_LITERAL, token.getType());
                assertEquals("Failed matching literal in " + i, expected[i], token.getData());
            }
            assertEquals(TokenType.EOF, lexer.nextToken().getType());
        }catch(IOException exc) {
            fail("Unexpected exception: " + exc.getMessage());
        }

    }

    @Test
    public void shouldMatchQualifiedNames() {
        String input = "object java.lang.Object path.to.a.Class__";
        int count = input.split("\\s+").length;
        int matched = 0;
        OperatorLexer lexer = new OperatorLexer(new StringReader(input));
        try {
            Token lookahead = lexer.nextToken();
            while(lookahead.getType() != TokenType.EOF) {
                assertEquals("Value: " + lookahead.getData().toString() + " was not recognized as a qualified name",
                        TokenType.QUALIFIED_NAME, lookahead.getType());
                lookahead = lexer.nextToken();
                matched++;
            }
        }
        catch (IOException exc) {
            fail("Unexpected exception: " + exc.getMessage());
        }
        assertEquals("Expected " + count + "names but got " + matched,
                matched, count);
    }

}