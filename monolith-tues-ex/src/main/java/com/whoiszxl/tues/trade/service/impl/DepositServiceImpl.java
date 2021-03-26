package com.whoiszxl.tues.trade.service.impl;

import com.whoiszxl.tues.common.bean.Result;
import com.whoiszxl.tues.common.enums.UpchainStatusEnum;
import com.whoiszxl.tues.common.exception.ExceptionCatcher;
import com.whoiszxl.tues.common.utils.BeanCopierUtils;
import com.whoiszxl.tues.common.utils.DateProvider;
import com.whoiszxl.tues.trade.dao.DepositDao;
import com.whoiszxl.tues.trade.dao.HeightDao;
import com.whoiszxl.tues.trade.entity.OmsDeposit;
import com.whoiszxl.tues.trade.entity.OmsHeight;
import com.whoiszxl.tues.trade.entity.dto.OmsCoinDTO;
import com.whoiszxl.tues.trade.entity.dto.OmsDepositDTO;
import com.whoiszxl.tues.trade.entity.dto.OmsHeightDTO;
import com.whoiszxl.tues.trade.service.DepositService;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 充值服务实现
 *
 * @author whoiszxl
 * @date 2021/3/24
 */
@Service
public class DepositServiceImpl implements DepositService {


    @Autowired
    private HeightDao heightDao;

    @Autowired
    private DepositDao depositDao;

    @Autowired
    private DateProvider dateProvider;

    @Override
    public OmsHeightDTO getCurrentHeight(String coinName) {
        OmsHeight omsHeight = heightDao.findByCoinName(coinName);
        return omsHeight == null ? null : omsHeight.clone(OmsHeightDTO.class);
    }

    @Override
    public OmsHeightDTO getOrCreateCurrentHeight(String coinName, OmsCoinDTO coinInfo, Long networkBlockHeight) {
        OmsHeightDTO heightObj = getCurrentHeight(coinName);
        if(heightObj == null) {
            OmsHeight height = new OmsHeight();
            height.setCoinId(coinInfo.getId());
            height.setCoinName(coinInfo.getCoinName());
            height.setHeight(networkBlockHeight);
            height.setUpdatedAt(dateProvider.now());
            height.setCreatedAt(dateProvider.now());
            saveCurrentHeight(height);
            ExceptionCatcher.catchValidateEx(Result.buildError("第一次创建同步区块高度记录"));
        }
        return heightObj;
    }

    @Override
    public OmsDepositDTO getDeposit(String toAddress, String coinName, BigDecimal amount) {
        OmsDeposit omsDeposit = depositDao.findByToAddressAndCoinNameAndDepositActual(toAddress, coinName, amount);
        return omsDeposit == null ? null : omsDeposit.clone(OmsDepositDTO.class);
    }

    public OmsDepositDTO updateDeposit(OmsDeposit omsDeposit) {
        OmsDeposit result = depositDao.save(omsDeposit);
        return result == null ? null : result.clone(OmsDepositDTO.class);
    }

    public OmsDepositDTO saveDeposit(OmsDeposit omsDeposit) {
        OmsDeposit result = depositDao.save(omsDeposit);
        return result == null ? null : result.clone(OmsDepositDTO.class);
    }

    @Override
    public OmsHeightDTO saveCurrentHeight(OmsHeight height) {
        OmsHeight result = heightDao.save(height);
        return result == null ? null : result.clone(OmsHeightDTO.class);
    }

    @Override
    public List<OmsDepositDTO> getWaitConfirmDeposit(String coinName) {
        List<OmsDeposit> depositList = depositDao.findByCoinNameAndUpchainStatus(coinName, UpchainStatusEnum.WAITING_CONFIRM.getCode());
        return ObjectUtils.isEmpty(depositList) ? null : BeanCopierUtils.copyListProperties(depositList, OmsDepositDTO::new);
    }

    @Override
    public boolean checkTxIsExist(String txHash, String coinName) {
        return depositDao.existsByTxHashAndCoinName(txHash, coinName);
    }
}
