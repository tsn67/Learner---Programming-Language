package com.dependancy.tokenizer;

import com.dependancy.errors.InvalidToken;
import com.dependancy.inputpreprocessor.PreLexer;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


@Setter
public class Lexer {

    private PreLexer preLexer;
    private List<Tokenizer> tokenizers;

    public Lexer(PreLexer preLexer) {
        this.preLexer = preLexer;

        //order adding different type tokenizer is important
        //provide least priority to the identifier tokenizer

        this.tokenizers = new ArrayList<>();
        this.tokenizers.add(new KeywordTokenizer());
        this.tokenizers.add(new OperatorTokenizer());
        this.tokenizers.add(new LiteralTokenizer());
        this.tokenizers.add(new SeparatorTokenizer());
        this.tokenizers.add(new IdentifierTokenizer());
    }


    public List<LineToken> tokenize(){

        var lines = preLexer.getInputLines();
        var outputLineTokens = new ArrayList<LineToken>();

        try {
            lines.forEach(line -> {
                line = line.trim(); //remove leading and trailing whitespaces
                LineToken lineToken = new LineToken();

                String[] possibleTokens = {line};

                for(String token : possibleTokens){
                    String remaining = token;
                    while(!remaining.isEmpty()){
                        remaining = remaining.trim();
                        boolean found = false;
                        for (Tokenizer tokenizer : tokenizers) {
                            int matchPrefix = tokenizer.matchPrefix(remaining);
                            if (matchPrefix != -1) {
                                Token tokenObj = tokenizer.tokenize(remaining.substring(0, matchPrefix));
                                lineToken.addToken(tokenObj);
                                remaining = remaining.substring(matchPrefix);
                                found = true;
                                break;
                            }
                        }

                        if (!found){
                            throw new InvalidToken(lines.indexOf(line) + 1, remaining);
                        }
                    }
                }
                outputLineTokens.add(lineToken);
            });
        } catch (InvalidToken e) {
            System.out.println("Invalid Token at line: " + e.getLineNumber());
            throw e;
        }
        return outputLineTokens;
    }

}
