package org.server.socialnetworkserver.responses;

public class BasicResponse {
    private boolean success;
    private String error;


    public BasicResponse(boolean success, String error) {
        this.success = success;
        this.error = error;
    }

    public BasicResponse(){

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

    @Override
    public String toString() {
        return "BaseResponse{" +
                "success=" + success +
                ", error='" + error + '\'' +
                '}';
    }
}
