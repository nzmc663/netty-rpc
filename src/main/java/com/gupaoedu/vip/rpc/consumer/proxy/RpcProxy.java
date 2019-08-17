package com.gupaoedu.vip.rpc.consumer.proxy;

import com.gupaoedu.vip.rpc.protocol.InvokerProtocol;
import com.gupaoedu.vip.rpc.protocol.Protocol;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 代理生成类
 *
 * @author tzf
 */
public class RpcProxy {
    /**
     * 生成代理对象
     */
    public static <T> T getProxy(Class<?> clazz){
        MethodInvocation invocation = new MethodInvocation(clazz);
        Class<?>[] interfaces = clazz.isInterface() ? new Class[]{clazz} : clazz.getInterfaces();
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(),interfaces,invocation);
    }
    /**
     * jdk动态代理类
     */
    private static class MethodInvocation implements InvocationHandler{

        private Class<?> target;

        public MethodInvocation(Class<?> target) {
            this.target = target;
        }

        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (Object.class.equals(method.getDeclaringClass())){
                return method.invoke(this,args);
            }
            String className = this.target.getName();
            String methodName = method.getName();
            //可通过策略模式进行设计改造
            return call(new InvokerProtocol(className,methodName,method.getParameterTypes(),args));
        }

        /**
         * 远程调用
         */
        public Object call(Protocol protocol) {
            final ProxyHandler handler = new ProxyHandler();
            final EventLoopGroup group = new NioEventLoopGroup();
            final Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast("frameDecoder", new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4))
                                    .addLast("frameEncoder", new LengthFieldPrepender(4))
                                    .addLast("encoder", new ObjectEncoder())
                                    .addLast("decoder", new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.cacheDisabled(null)))
                                    .addLast("handler", handler);
                        }
                    });
            try {
                ChannelFuture future = bootstrap.connect("localhost", 8999).sync();
                future.channel().writeAndFlush(protocol).sync();
                future.channel().closeFuture().sync();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }finally {
                group.shutdownGracefully();
            }
            return handler.getResponse();
        }
    }

}
