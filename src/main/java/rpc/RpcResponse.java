package rpc;

public class RpcResponse {
    private Object responseData;
    private String requestId;

    public Object getResponseData() {
        return responseData;
    }

    public void setResponseData(Object responseData) {
        this.responseData = responseData;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    @Override
    public String toString() {
        return "RpcResponse{" +
                "responseData=" + responseData +
                ", requestId='" + requestId + '\'' +
                '}';
    }
}
