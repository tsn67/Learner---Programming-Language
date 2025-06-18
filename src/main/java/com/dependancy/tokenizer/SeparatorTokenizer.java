package com.dependancy.tokenizer;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SeparatorTokenizer implements Tokenizer{

    private final TokenType type = TokenType.SEPARATOR;
    private final Pattern pattern  = Pattern.compile("[(){},]");

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
