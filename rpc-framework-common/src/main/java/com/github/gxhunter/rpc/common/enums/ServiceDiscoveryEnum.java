package com.github.gxhunter.rpc.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author hunter
 * @Date 2023年02月24日 15:33
 */
@AllArgsConstructor
@Getter
public enum ServiceDiscoveryEnum {

    ZK("zk");

    private final String name;
}
