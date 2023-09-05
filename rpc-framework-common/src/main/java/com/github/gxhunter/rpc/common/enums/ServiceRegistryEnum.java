package com.github.gxhunter.rpc.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author hunter
 * @Date 2023年02月24日 15:30
 */
@AllArgsConstructor
@Getter
public enum ServiceRegistryEnum {

    ZK("zk");

    private final String name;
}
