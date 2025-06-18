package com.dependancy.inputpreprocessor;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class PreLexer {

    private final List<String> inputLines = new ArrayList<>();

    public void addLine(String line){
        this.inputLines.add(line);
    }

}

