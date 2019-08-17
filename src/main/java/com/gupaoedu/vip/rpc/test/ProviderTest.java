package com.gupaoedu.vip.rpc.test;

import com.gupaoedu.vip.rpc.provider.ServiceLoader;

/**
 * 服务提供方测试类
 *
 * @author tzf
 */
public class ProviderTest {

    public static void main(String[] args) {
        ServiceLoader serviceLoader = new ServiceLoader(8999);
        serviceLoader.startup();
    }
}
