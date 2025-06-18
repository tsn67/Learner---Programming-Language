package com.dependancy.inputpreprocessor;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class PreLexer {

    private final List<String> inputLines = new ArrayList<>();

    public void addLine(String line){

        //add the line if it is not a comment line
        //comment-line line starting with '//'
        String testLine = line.trim();
        if (testLine.startsWith("//")){
            return;
        }
        this.inputLines.add(line);
    }

}

