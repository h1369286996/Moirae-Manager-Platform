package com.platon.rosettaflow.service.Impl;

import com.platon.rosettaflow.grpc.client.AuthServiceClient;
import com.platon.rosettaflow.grpc.client.YarnServiceClient;
import com.platon.rosettaflow.grpc.identity.dto.NodeIdentityDto;
import com.platon.rosettaflow.service.IAuthServiceRpc;
import com.platon.rosettaflow.service.IYarnServiceRpc;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author hudenian
 * @date 2021/7/15
 */
@Slf4j
@Service
public class AuthServiceRpcImpl implements IAuthServiceRpc {

    @Resource
    private AuthServiceClient authServiceClient;

    @Override
    public NodeIdentityDto getOwner() {
        return authServiceClient.getNodeIdentity();
    }
}
