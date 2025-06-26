package com.dependancy.errors;

public class SemanticError extends CompileTimeError {

    public String additionalInfo;

    public SemanticError(Integer lineNumber, String message, String additionalInfo){
        super.setLineNumber(lineNumber);
        super.setMessage(message);
        this.additionalInfo = additionalInfo;
    }

    public String getDetailedInfo() {
        return super.getMessage() + "\nline:" + super.getLineNumber() + "\nAdditional Info: " + this.additionalInfo;
    }
}
