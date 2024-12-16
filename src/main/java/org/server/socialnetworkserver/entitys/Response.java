package org.server.socialnetworkserver.entitys;

public class Response {
    private boolean success;
    private String error;
    private int errorCode;

    public Response(boolean success, String error, int errorCode) {
        this.success = success;
        this.error = error;
        this.errorCode = errorCode;
    }

    public Response(){

    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    @Override
    public String toString() {
        return "Response{" +
                "success=" + success +
                ", error='" + error + '\'' +
                ", errorCode=" + errorCode +
                '}';
    }
}
