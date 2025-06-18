package com.dependancy.errors;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CompileTimeError extends Error{

    private Integer lineNumber;
    private String message;

    public CompileTimeError(){
        super("Compile Time Error!");
    }
}
