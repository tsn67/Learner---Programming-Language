package com.dependancy.tokenizer;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IdentifierTokenizer implements Tokenizer{

    private final TokenType type = TokenType.IDENTIFIER;
    private final Pattern pattern = Pattern.compile("[A-Za-z][A-Za-z0-9_]*");

    @Override
    public Token tokenize(String inputValue) {
        return new Token(type, inputValue);

    }

    @Override
    public int matchPrefix(String inputValue) {

        Matcher matcher = pattern.matcher(inputValue);

        if (matcher.lookingAt()) {
            //prefix matches
            return matcher.end();
        }

        //if not match any prefix return -1
        return -1;
    }
}
