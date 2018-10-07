package rpc;

public class RpcResponse {
    private Object responseData;

    public Object getResponseData() {
        return responseData;
    }

    public void setResponseData(Object responseData) {
        this.responseData = responseData;
    }

    @Override
    public String toString() {
        return "RpcResponse{" +
                "responseData=" + responseData +
                '}';
    }
}
