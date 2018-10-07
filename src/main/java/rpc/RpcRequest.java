package rpc;

import java.io.Serializable;
import java.util.Arrays;

/**
 * @author nnzhang
 */
public class RpcRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    private String fullClassName;
    private String methodName;
    private Object[] args;
    private Class<?>[] params;
    private Class<?> returnType;
    private String requestId;

    public RpcRequest () {}

    public String getFullClassName() {
        return fullClassName;
    }

    public void setFullClassName(String fullClassName) {
        this.fullClassName = fullClassName;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }

    public Class<?>[] getParams() {
        return params;
    }

    public void setParams(Class<?>[] params) {
        this.params = params;
    }

    public Class<?> getReturnType() {
        return returnType;
    }

    public void setReturnType(Class<?> returnType) {
        this.returnType = returnType;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    @Override
    public String toString() {
        return "RpcRequest{" +
                "fullClassName='" + fullClassName + '\'' +
                ", methodName='" + methodName + '\'' +
                ", args=" + Arrays.toString(args) +
                ", params=" + Arrays.toString(params) +
                ", returnType=" + returnType +
                ", requestId='" + requestId + '\'' +
                '}';
    }
}
