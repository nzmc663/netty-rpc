package com.gupaoedu.vip.rpc.provider.handler;

import com.gupaoedu.vip.rpc.protocol.InvokerProtocol;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 服务端rpc主要实现核心类
 *
 * @author tzf
 */
public class ServiceHandler extends ChannelInboundHandlerAdapter {

    /**
     * 保存所有可用的服务
     */
    public static ConcurrentHashMap<String, Object> registryMap = new ConcurrentHashMap<>();

    /**
     * 所有相关的服务类
     */
    private List<String> classNames = new ArrayList<>();

    public ServiceHandler() {
        scan("com.gupaoedu.vip.rpc.provider");
        regist();
    }

    private void scan(String packageName) {
        URL url = this.getClass().getClassLoader().getResource(packageName.replaceAll("\\.","/").trim());
        File path = new File(url.getFile());
        for (File file : path.listFiles()) {
            if (file.isDirectory()) {
                scan(packageName + "." + file.getName());
            } else if (file.getName().endsWith(".class")){
                classNames.add(packageName + "." + file.getName().replace(".class","").trim());
            }
        }
    }

    private void regist() {
        for (String className : classNames){
            try {
                Class<?> clazz = Class.forName(className);
                for (Class<?> i : clazz.getInterfaces()){
                    registryMap.put(i.getName(),clazz.newInstance());
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Object result = new Object();
        if (msg instanceof InvokerProtocol){
            InvokerProtocol request = (InvokerProtocol)msg;
            //当客户端建立连接时，需要从自定义协议中获取信息，拿到具体的服务和实参
            //使用反射调用
            if(registryMap.containsKey(request.getClassName())){
                Object clazz = registryMap.get(request.getClassName());
                Method method = clazz.getClass().getMethod(request.getMethodName(), request.getParameterType());
                result = method.invoke(clazz, request.getParameters());
                System.out.println("----->执行方法："+method.getDeclaringClass()+"."+method.getName());
            }
            ctx.write(result);
            ctx.flush();
            ctx.close();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }



}
