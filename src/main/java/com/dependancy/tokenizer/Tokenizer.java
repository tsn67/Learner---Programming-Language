package com.dependancy.tokenizer;

public interface Tokenizer {
    Token tokenize(String inputValue);
    int matchPrefix(String inputValue);
}
