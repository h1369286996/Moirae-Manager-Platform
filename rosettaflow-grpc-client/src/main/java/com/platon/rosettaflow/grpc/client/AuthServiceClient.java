package com.platon.rosettaflow.grpc.client;

import com.google.protobuf.ByteString;
import com.google.protobuf.Empty;
import com.platon.rosettaflow.common.enums.ErrorMsg;
import com.platon.rosettaflow.common.enums.MetaDataUsageEnum;
import com.platon.rosettaflow.common.enums.RespCodeEnum;
import com.platon.rosettaflow.common.exception.BusinessException;
import com.platon.rosettaflow.grpc.constant.GrpcConstant;
import com.platon.rosettaflow.grpc.identity.dto.NodeIdentityDto;
import com.platon.rosettaflow.grpc.metadata.req.dto.ApplyMetaDataAuthorityRequestDto;
import com.platon.rosettaflow.grpc.metadata.req.dto.MetaDataAuthorityDto;
import com.platon.rosettaflow.grpc.metadata.req.dto.MetaDataUsageDto;
import com.platon.rosettaflow.grpc.metadata.resp.dto.ApplyMetaDataAuthorityResponseDto;
import com.platon.rosettaflow.grpc.metadata.resp.dto.GetMetaDataAuthorityDto;
import com.platon.rosettaflow.grpc.service.*;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author admin
 * @date 2021/9/1
 * @description 身份相关接口
 */
@Slf4j
@Component
public class AuthServiceClient {

    @GrpcClient("carrier-grpc-server")
    private AuthServiceGrpc.AuthServiceBlockingStub authServiceBlockingStub;

    public ApplyMetaDataAuthorityResponseDto applyMetaDataAuthority(ApplyMetaDataAuthorityRequestDto requestDto) {
        ApplyMetaDataAuthorityRequest.Builder applyMetaDataAuthorityRequest = ApplyMetaDataAuthorityRequest.newBuilder();

        //1.设置发起任务的用户的信息
        applyMetaDataAuthorityRequest.setUser(requestDto.getUser());

        //2.设置用户类型
        applyMetaDataAuthorityRequest.setUserTypeValue(requestDto.getUserType());

        //3.元数据使用授权信息
        MetaDataAuthority.Builder metaDataAuthorityBuilder = MetaDataAuthority.newBuilder();

        //3.1元数据所属的组织信息
        Organization owner = Organization.newBuilder()
                .setIdentityId(requestDto.getAuth().getOwner().getIdentityId())
                .setNodeName(requestDto.getAuth().getOwner().getNodeName())
                .setNodeId(requestDto.getAuth().getOwner().getNodeId())
                .build();
        metaDataAuthorityBuilder.setOwner(owner);

        // 3.2元数据Id
        metaDataAuthorityBuilder.setMetaDataId(requestDto.getAuth().getMetaDataId());

        //3.3元数据怎么使用
        MetaDataUsage.Builder metaDataUsageBuilder = MetaDataUsage.newBuilder();
        metaDataUsageBuilder.setUsageTypeValue(requestDto.getAuth().getMetaDataUsageDto().getUseType());
        if (requestDto.getAuth().getMetaDataUsageDto().getUseType() == MetaDataUsageEnum.PERIOD.getValue()) {
            if (requestDto.getAuth().getMetaDataUsageDto().getStartAt() == null || requestDto.getAuth().getMetaDataUsageDto().getEndAt() == null) {
                throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.APPLY_METADATA_USAGE_TYPE_ERROR.getMsg());
            }
        } else if (requestDto.getAuth().getMetaDataUsageDto().getStartAt() == MetaDataUsageEnum.TIMES.getValue()) {
            if (requestDto.getAuth().getMetaDataUsageDto().getTimes() == null) {
                throw new BusinessException(RespCodeEnum.BIZ_FAILED, ErrorMsg.APPLY_METADATA_USAGE_TYPE_ERROR.getMsg());
            }
        }
        metaDataUsageBuilder.setStartAt(requestDto.getAuth().getMetaDataUsageDto().getStartAt());
        metaDataUsageBuilder.setEndAt(requestDto.getAuth().getMetaDataUsageDto().getEndAt());
        metaDataUsageBuilder.setTimes(requestDto.getAuth().getMetaDataUsageDto().getTimes().intValue());
        metaDataAuthorityBuilder.setUsage(metaDataUsageBuilder);
        applyMetaDataAuthorityRequest.setAuth(metaDataAuthorityBuilder.build());

        // 4.发起数据授权申请的账户的签名
        applyMetaDataAuthorityRequest.setSign(ByteString.copyFromUtf8(requestDto.getSign()));

        ApplyMetaDataAuthorityResponse applyMetaDataAuthorityResponse = authServiceBlockingStub.applyMetaDataAuthority(applyMetaDataAuthorityRequest.build());

        if (applyMetaDataAuthorityResponse.getStatus() != GrpcConstant.GRPC_SUCCESS_CODE) {
            throw new BusinessException(applyMetaDataAuthorityResponse.getStatus(), applyMetaDataAuthorityResponse.getMsg());
        }

        //TODO 国际化处理
        ApplyMetaDataAuthorityResponseDto applyMetaDataAuthorityResponseDto = new ApplyMetaDataAuthorityResponseDto();
        applyMetaDataAuthorityResponseDto.setStatus(applyMetaDataAuthorityResponse.getStatus());
        applyMetaDataAuthorityResponseDto.setMsg(applyMetaDataAuthorityResponse.getMsg());
        applyMetaDataAuthorityResponseDto.setMetaDataAuthId(applyMetaDataAuthorityResponse.getMetaDataAuthId());
        return applyMetaDataAuthorityResponseDto;
    }

    /**
     * 获取数据授权申请列表
     *
     * @return 获取数据授权申请列表
     */
    public List<GetMetaDataAuthorityDto> getMetaDataAuthorityList() {
        List<GetMetaDataAuthorityDto> getMetaDataAuthorityDtoList = new ArrayList<>();

        Empty empty = Empty.newBuilder().build();
        GetMetaDataAuthorityListResponse getMetaDataAuthorityListResponse = authServiceBlockingStub.getMetaDataAuthorityList(empty);

        if (getMetaDataAuthorityListResponse.getStatus() != GrpcConstant.GRPC_SUCCESS_CODE) {
            throw new BusinessException(getMetaDataAuthorityListResponse.getStatus(), getMetaDataAuthorityListResponse.getMsg());
        }

        GetMetaDataAuthorityDto getMetaDataAuthorityDto;
        MetaDataAuthorityDto metaDataAuthorityDto;
        NodeIdentityDto owner;
        MetaDataUsageDto metaDataUsageDto;
        for (int i = 0; i < getMetaDataAuthorityListResponse.getListCount(); i++) {
            getMetaDataAuthorityDto = new GetMetaDataAuthorityDto();

            owner = new NodeIdentityDto();
            owner.setNodeName(getMetaDataAuthorityListResponse.getList(i).getAuth().getOwner().getNodeName());
            owner.setNodeId(getMetaDataAuthorityListResponse.getList(i).getAuth().getOwner().getNodeId());
            owner.setIdentityId(getMetaDataAuthorityListResponse.getList(i).getAuth().getOwner().getIdentityId());

            metaDataUsageDto = new MetaDataUsageDto();
            metaDataUsageDto.setUseType(getMetaDataAuthorityListResponse.getList(i).getAuth().getUsage().getUsageTypeValue());
            metaDataUsageDto.setStartAt(getMetaDataAuthorityListResponse.getList(i).getAuth().getUsage().getStartAt());
            metaDataUsageDto.setEndAt(getMetaDataAuthorityListResponse.getList(i).getAuth().getUsage().getEndAt());
            metaDataUsageDto.setTimes(getMetaDataAuthorityListResponse.getList(i).getAuth().getUsage().getTimes());

            metaDataAuthorityDto = new MetaDataAuthorityDto();
            metaDataAuthorityDto.setOwner(owner);
            metaDataAuthorityDto.setMetaDataId(getMetaDataAuthorityListResponse.getList(i).getAuth().getMetaDataId());
            metaDataAuthorityDto.setMetaDataUsageDto(metaDataUsageDto);

            //元数据授权申请Id
            getMetaDataAuthorityDto.setMetaDataAuthId(getMetaDataAuthorityListResponse.getList(i).getMetaDataAuthId());
            //发起任务的用户的信息 (task是属于用户的)
            getMetaDataAuthorityDto.setUser(getMetaDataAuthorityListResponse.getList(i).getUser());
            //用户类型 (0: 未定义; 1: 以太坊地址; 2: Alaya地址; 3: PlatON地址)
            getMetaDataAuthorityDto.setUserType((getMetaDataAuthorityListResponse.getList(i).getUserTypeValue()));
            //元数据使用授权信息
            getMetaDataAuthorityDto.setMetaDataAuthorityDto(metaDataAuthorityDto);
            //审核结果:0-等待审核中 1-审核通过 2-审核拒绝
            getMetaDataAuthorityDto.setAuditMetaDataOption(getMetaDataAuthorityListResponse.getList(i).getAuditValue());
            //发起授权申请的时间 (单位: ms)
            getMetaDataAuthorityDto.setApplyAt(getMetaDataAuthorityListResponse.getList(i).getApplyAt());
            //审核授权申请的时间 (单位: ms)
            getMetaDataAuthorityDto.setAuditAt(getMetaDataAuthorityListResponse.getList(i).getAuditAt());

            getMetaDataAuthorityDtoList.add(getMetaDataAuthorityDto);
        }
        return getMetaDataAuthorityDtoList;
    }

    /**
     * 查询自己组织的identity信息
     *
     * @return 返回组织信息
     */
    public NodeIdentityDto getNodeIdentity() {
        Empty empty = Empty.newBuilder().build();
        GetNodeIdentityResponse nodeIdentity = authServiceBlockingStub.getNodeIdentity(empty);

        if (nodeIdentity.getStatus() != GrpcConstant.GRPC_SUCCESS_CODE) {
            throw new BusinessException(nodeIdentity.getStatus(), nodeIdentity.getMsg());
        }

        NodeIdentityDto nodeIdentityDto = new NodeIdentityDto();
        nodeIdentityDto.setNodeId(nodeIdentity.getOwner().getNodeId());
        nodeIdentityDto.setIdentityId(nodeIdentity.getOwner().getIdentityId());

        return nodeIdentityDto;
    }
}
