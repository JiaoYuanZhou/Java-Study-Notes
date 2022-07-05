package config;

import R.RPCRequest;
import R.RPCResponse;
import lombok.AllArgsConstructor;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 动态代理类，能根据接口的class类返回相应的代理类
 * 动态代理封装request对象
 */
@AllArgsConstructor
public class ClientProxy implements InvocationHandler {
    //服务端IP
    private String host;
    //服务端端口号
    private int port;


    // jdk 动态代理， 每一次代理对象调用方法，会经过此方法增强（反射获取request对象，socket发送至客户端）
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //request的构建，使用了lombok中的builder，代码简洁
        RPCRequest request = RPCRequest.builder()
                .interfaceName(method.getDeclaringClass().getName())
                .methodName(method.getName())
                .params(args)
                .paramsTypes(method.getParameterTypes()).build();
        RPCResponse response = IOClient.sendRequest(host, port, request);

        return response.getData();

    }

    public <T>T getProxy(Class<T> clazz) {
        Object o = Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, this);
        return (T) o;
    }
}
