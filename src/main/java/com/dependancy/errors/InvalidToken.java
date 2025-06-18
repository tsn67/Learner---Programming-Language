package com.dependancy.errors;

public class InvalidToken extends CompileTimeError{

  public InvalidToken(Integer lineNumber, String input){
    super.setMessage("Invalid Token: " + input);
    super.setLineNumber(lineNumber);
  }
}