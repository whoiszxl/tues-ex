package com.whoiszxl.tues.wallet.service.impl;

import com.whoiszxl.tues.common.enums.SwitchStatusEnum;
import com.whoiszxl.tues.common.utils.DateProvider;
import com.whoiszxl.tues.common.utils.IdWorker;
import com.whoiszxl.tues.member.entity.UmsMemberAddress;
import com.whoiszxl.tues.member.entity.dto.UmsMemberAddressDTO;
import com.whoiszxl.tues.member.service.MemberAddressService;
import com.whoiszxl.tues.trade.entity.dto.OmsCoinDTO;
import com.whoiszxl.tues.trade.service.CoinService;
import com.whoiszxl.tues.wallet.common.CoinNodeProviderFactory;
import com.whoiszxl.tues.wallet.ethereum.core.entity.AddressResponse;
import com.whoiszxl.tues.wallet.ethereum.core.service.NodeProvider;
import com.whoiszxl.tues.wallet.service.AddressDispenseService;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * 地址分配服务实现
 *
 * @author whoiszxl
 * @date 2021/3/24
 */
@Service
public class AddressDispenseServiceImpl implements AddressDispenseService {

    @Autowired
    private MemberAddressService memberAddressService;

    @Autowired
    private CoinNodeProviderFactory addressCreatorFactory;

    @Autowired
    private CoinService coinService;

    @Autowired
    private DateProvider dateProvider;

    @Autowired
    private IdWorker idWorker;

    @Override
    public UmsMemberAddressDTO createOrGetAddress(Long memberId, Integer coinId) {
        //判断地址是否已经生成，已经生成直接返回
        UmsMemberAddressDTO memberAddress = memberAddressService.getMemberAddressByCoinId(memberId, coinId);
        if(memberAddress != null) {
            return memberAddress;
        }

        //获取币种信息
        String coinName = null;
        List<OmsCoinDTO> coinDTOList = coinService.findAllByStatusOrderBySortAscIdAsc(SwitchStatusEnum.STATUS_OPEN.getStatusCode());
        Optional<OmsCoinDTO> coinOptional = coinDTOList.stream().filter(item -> item.getId().equals(coinId)).findFirst();
        if(coinOptional.isPresent()) {
            OmsCoinDTO omsCoinDTO = coinOptional.get();
            coinName = omsCoinDTO.getCoinName();
        }
        //未生成调用工厂生成对应地址
        NodeProvider addressCreator = addressCreatorFactory.get(coinName);
        AddressResponse depositAddress = addressCreator.createDepositAddress(memberId);

        //将生成的地址保存到数据库
        if(ObjectUtils.isNotEmpty(depositAddress) && StringUtils.isNotBlank(depositAddress.getAddress())) {
            UmsMemberAddress umsMemberAddress = new UmsMemberAddress();
            umsMemberAddress.setId(idWorker.nextId());
            umsMemberAddress.setMemberId(memberId);
            umsMemberAddress.setCoinId(coinId);
            umsMemberAddress.setDepositAddress(depositAddress.getAddress());
            umsMemberAddress.setKeystore(depositAddress.getKeystoreName());
            umsMemberAddress.setPrivateKey(depositAddress.getMnemonic());
            umsMemberAddress.setStatus(SwitchStatusEnum.STATUS_OPEN.getStatusCode());
            umsMemberAddress.setUpdatedAt(dateProvider.now());
            umsMemberAddress.setCreatedAt(dateProvider.now());

            return memberAddressService.save(umsMemberAddress);
        }
        return null;
    }
}
