package com.dependancy.errors;

public class InvalidSyntax extends CompileTimeError {

  private final String additionalInfo;
  private final String causeToken;

  public InvalidSyntax(Integer lineNumber, String causeToken, String additionalInfo){
    super.setLineNumber(lineNumber);
    super.setMessage("Invalid Syntax error:(parser)");
    this.causeToken = causeToken;
    this.additionalInfo = additionalInfo;
  }

  public String getDetailedInfo() {
    return super.getMessage() + "\nline:" + super.getLineNumber() + "\nToken cause: " + this.causeToken + "\nAdditional Info: " + this.additionalInfo;
  }
}
