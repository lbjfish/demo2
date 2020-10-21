package com.lee.feignweb.ribbonconfig.rule;

import org.springframework.cloud.alibaba.nacos.ribbon.NacosServer;
import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.RoundRobinRule;
import com.netflix.loadbalancer.Server;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 根据实例版本号隔离规则
 * 详细用法：https://mp.weixin.qq.com/s/9XQ-SIbYsov3KBx9TGFN0g
 *
 * 实例获取规则顺序如下(不满足则走下一个规则)：
 * 1. 相同版本号的实例
 * 2. 无版本号的实例
 * 3. 所有实例中轮询
 *
 * @author zlt
 * @date 2019/9/3
 * <p>
 * Blog: https://zlt2000.gitee.io
 * Github: https://github.com/zlt2000
 */
public class VersionIsolationRule extends RoundRobinRule {
    private final static String KEY_DEFAULT = "default";
    /**
     * 优先根据版本号取实例
     */
    @Override
    public Server choose(ILoadBalancer lb, Object key) {
        if (lb == null) {
            return null;
        }
        String version;
        if (key != null && !KEY_DEFAULT.equals(key)) {
            version = key.toString();
        } else {
            version = "aaa";
        }

        List<Server> targetList = null;
        List<Server> upList = lb.getReachableServers();
        if (!StringUtils.isEmpty(version)) {
            //取指定版本号的实例
            targetList = upList.stream().filter(
                    server -> version.equals(
                            ((NacosServer) server).getMetadata().get("version")
                    )
            ).collect(Collectors.toList());
        }

        if (CollectionUtils.isEmpty(targetList)) {
            //只取无版本号的实例
            targetList = upList.stream().filter(
                    server -> {
                        String metadataVersion = ((NacosServer) server).getMetadata().get("version");
                        return StringUtils.isEmpty(metadataVersion);
                    }
            ).collect(Collectors.toList());
        }

        if (!CollectionUtils.isEmpty(targetList)) {
            return getServer(targetList);
        }
        return super.choose(lb, key);
    }

    /**
     * 随机取一个实例
     */
    private Server getServer(List<Server> upList) {
//        int nextInt = RandomUtil.randomInt(upList.size());
        return upList.get(0);
    }
}
