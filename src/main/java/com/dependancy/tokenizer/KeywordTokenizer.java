package com.dependancy.tokenizer;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class KeywordTokenizer implements Tokenizer{

    private final TokenType type = TokenType.KEYWORD;
    private final Pattern pattern = Pattern.compile("\\b(int|string|void|return|if|else|while|main|bool)\\b");

    public int matchPrefix(String inputValue) {
        Matcher matcher = pattern.matcher(inputValue);

        if (matcher.lookingAt()) {
            return matcher.end();
        }

        return -1;
    }

    public Token tokenize(String inputValue) {
        return  new Token(type, inputValue);
    }
}
