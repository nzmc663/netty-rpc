package com.gupaoedu.vip.rpc.protocol;

import lombok.Data;

import java.io.Serializable;

/**
 * 远程调用传输协议
 * @author tzf
 */
@Data
public class InvokerProtocol implements Protocol,Serializable {

    private static final long serialVersionUID = 2924677033974034846L;
    /**
     * 类全限定名
     */
    private String className;
    /**
     * invoke方法名称
     */
    private String methodName;
    /**
     * 参数类型
     */
    private Class<?>[] parameterType;
    /**
     * 参数列表
     */
    private Object[] parameters;

    public InvokerProtocol(String className, String methodName, Class<?>[] parameterType, Object[] parameters) {
        this.className = className;
        this.methodName = methodName;
        this.parameterType = parameterType;
        this.parameters = parameters;
    }
}
