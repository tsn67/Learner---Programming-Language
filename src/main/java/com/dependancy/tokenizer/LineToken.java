package com.dependancy.tokenizer;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class LineToken {

    private final List<Token> tokens;

    public LineToken() {
        this.tokens = new ArrayList<>();
    }

    public void addToken(Token token) {
        this.tokens.add(token);
    }
}
