package com.dependancy.tokenizer;

public enum TokenType {
    KEYWORD,
    IDENTIFIER,
    NUMBER,
    STRING,
    BOOLEAN,
    OPERATOR,
    SEPARATOR, //no symbol, comments are removed before the tokenization
}
