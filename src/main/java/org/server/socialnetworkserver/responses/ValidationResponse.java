package org.server.socialnetworkserver.responses;

public class ValidationResponse extends BasicResponse {
    private int errorCode;

    public ValidationResponse(boolean success, String error, int errorCode) {
        super(success,error);
        this.errorCode = errorCode;
    }

    public ValidationResponse(){

    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    @Override
    public String toString() {
        return super.toString() + "{" +
                "errorCode=" + errorCode +
                '}';
    }
}
