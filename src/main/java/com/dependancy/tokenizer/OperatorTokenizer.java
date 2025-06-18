package com.dependancy.tokenizer;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OperatorTokenizer implements Tokenizer{

    private final TokenType type = TokenType.OPERATOR;
    Pattern pattern = Pattern.compile("==|>=|<=|!=|=|>|<|\\+|-|/|\\*|%|and|not|or|xor");

    @Override
    public Token tokenize(String inputValue) {
        return new Token(type, inputValue);
    }

    @Override
    public int matchPrefix(String inputValue) {
        Matcher matcher = pattern.matcher(inputValue);

        if(matcher.lookingAt()){
            return matcher.end();
        }

        return -1;

    }
}
