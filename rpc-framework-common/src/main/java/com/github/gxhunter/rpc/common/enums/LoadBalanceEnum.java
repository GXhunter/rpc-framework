package com.github.gxhunter.rpc.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author hunter
 * @Date 2023年02月24日 15:31
 */
@AllArgsConstructor
@Getter
public enum LoadBalanceEnum {

    LOADBALANCE("loadBalance");

    private final String name;
}
