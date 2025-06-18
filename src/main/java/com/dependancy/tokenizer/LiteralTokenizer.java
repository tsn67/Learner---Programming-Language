package com.dependancy.tokenizer;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LiteralTokenizer implements Tokenizer{

    private final Pattern intLiteralPattern = Pattern.compile("-?(?:1000|[1-9][0-9]{0,2}|0)");
    private final Pattern stringLiteralPattern =  Pattern.compile("\"(?:[^\"\\\\]|\\\\.)*\"");
    private final Pattern booleanLiteralPattern =  Pattern.compile("true|false");

    //token type can be NUMBER STRING or BOOLEAN


    public int matchPrefixSingle(Matcher matcher) {
        if (matcher.lookingAt()){
            return matcher.end();
        }
        return -1;
    }

    @Override
    public Token tokenize(String inputValue) {
        if (intLiteralPattern.matcher(inputValue).matches()){
            return new Token(TokenType.NUMBER, inputValue);
        } else if (stringLiteralPattern.matcher(inputValue).matches()){
            return new Token(TokenType.STRING, inputValue);
        } else if (booleanLiteralPattern.matcher(inputValue).matches()){
            return new Token(TokenType.BOOLEAN, inputValue);
        }
        return null;
    }

    @Override
    public int matchPrefix(String inputValue) {

        int test1 = this.matchPrefixSingle(intLiteralPattern.matcher(inputValue));
        if (test1 != -1){
            return test1;
        }

        int test2 = this.matchPrefixSingle(stringLiteralPattern.matcher(inputValue));
        if (test2 != -1){
            return test2;
        }

        return this.matchPrefixSingle(booleanLiteralPattern.matcher(inputValue));
    }
}
