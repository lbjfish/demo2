package com.lee.feignweb.ribbonconfig.rule;

import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.AbstractLoadBalancerRule;
import com.netflix.loadbalancer.Server;

import java.util.List;

public class GlobalRule extends AbstractLoadBalancerRule {


    @Override
    public void initWithNiwsConfig(IClientConfig iClientConfig) {

    }

    @Override
    public Server choose(Object o) {
        List<Server> upList = this.getLoadBalancer().getReachableServers();
        List<Server> servers = this.getLoadBalancer().getAllServers();
        System.out.println(upList);
        System.out.println(servers);
        return servers.get(0);
    }
}
