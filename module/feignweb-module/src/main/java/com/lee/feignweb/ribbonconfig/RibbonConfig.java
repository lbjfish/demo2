package com.lee.feignweb.ribbonconfig;

import com.lee.feignweb.ribbonconfig.rule.GlobalRule;
import com.lee.feignweb.ribbonconfig.rule.NacosWeightedRule;
import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.RandomRule;
import com.netflix.loadbalancer.RoundRobinRule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RibbonConfig {
    @Bean
    public IRule ribbonRule(){
        return new NacosWeightedRule();
    }
}
