package com.lee.feignweb.ribbonconfig.rule;

import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.AbstractLoadBalancerRule;
import com.netflix.loadbalancer.BaseLoadBalancer;
import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.Server;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class GlobalRule extends AbstractLoadBalancerRule {

    private AtomicInteger nextServerCyclicCounter;

    public GlobalRule() {
        this.nextServerCyclicCounter = new AtomicInteger(0);
    }

    @Override
    public void initWithNiwsConfig(IClientConfig iClientConfig) {

    }

    @Override
    public Server choose(Object o) {
        BaseLoadBalancer balancer = (BaseLoadBalancer) getLoadBalancer();
        String b = balancer.getName();
        List<Server> upList = this.getLoadBalancer().getReachableServers();
        List<Server> servers = this.getLoadBalancer().getAllServers();
        System.out.println(upList);
        System.out.println(servers);

        int nextServerIndex = incrementAndGetModulo(servers.size());
        return servers.get(nextServerIndex);
    }

    private int incrementAndGetModulo(int modulo) {
        int current;
        int next;
        do {
            current = this.nextServerCyclicCounter.get();
            next = (current + 1) % modulo;
        } while(!this.nextServerCyclicCounter.compareAndSet(current, next));

        return next;
    }

}
