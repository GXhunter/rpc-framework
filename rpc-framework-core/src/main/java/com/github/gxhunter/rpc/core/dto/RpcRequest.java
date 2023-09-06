package com.github.gxhunter.rpc.core.dto;

import lombok.*;

/**
 * @author hunter
 * 
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@ToString
public class RpcRequest  {
    private String requestId;
    private String interfaceName;
    private String methodName;
    private Object[] parameters;
    private Class<?>[] paramTypes;

    public String getRpcServiceName() {
        return this.getInterfaceName();
    }
}
