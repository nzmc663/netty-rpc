package com.gupaoedu.vip.rpc.provider;

import com.gupaoedu.vip.rpc.provider.handler.ServiceHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

/**
 * 服务启动类
 *
 * @author tzf
 */
public class ServiceLoader {

    private int port;

    public ServiceLoader(int port) {
        this.port = port;
    }

    public void startup(){
        //boss线程组
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        //worker线程组
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        //服务端bootstrap
        ServerBootstrap bootstrap = new ServerBootstrap();

        bootstrap.group(bossGroup,workerGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG,128)
                .childOption(ChannelOption.SO_KEEPALIVE,true)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel channel) throws Exception {
                        ChannelPipeline pipeline = channel.pipeline();
                        /**
                         * 自定义协议解码器
                         *
                         * 入参有5个，分别解释如下
                         * maxFrameLength：框架的最大长度。如果帧的长度大于此值，则将抛出TooLongFrameException。
                         * lengthFieldOffset：长度字段的偏移量：即对应的长度字段在整个消息数据中得位置
                         * lengthFieldLength：长度字段的长度：如：长度字段是int型表示，那么这个值就是4（long型就是8）
                         * lengthAdjustment：要添加到长度字段值的补偿值
                         * initialBytesToStrip：从解码帧中去除的第一个字节数
                         */
                        pipeline.addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4))
                                //自定义协议编码器
                                .addLast(new LengthFieldPrepender(4))
                                //对象参数类型编码器
                                .addLast("encoder",new ObjectEncoder())
                                //对象参数类型解码器
                                .addLast("decoder",new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.cacheDisabled(null)))
                                //自定义逻辑处理器
                                .addLast(new ServiceHandler());
                    }
                });
        try {
            ChannelFuture future = bootstrap.bind(port).sync();//绑定端口
            System.out.println("----->启动成功,监听端口："+port);
            future.channel().closeFuture().sync();//关闭管道
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
