package com.kashyap.homeIdeas.billmonitor.service.impl;

import com.kashyap.homeIdeas.billmonitor.exception.NoRecordFoundException;
import com.kashyap.homeIdeas.billmonitor.model.Bill;
import com.kashyap.homeIdeas.billmonitor.model.BillType;
import com.kashyap.homeIdeas.billmonitor.repostiory.BillTypeRepository;
import com.kashyap.homeIdeas.billmonitor.service.BillTypeService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.IterableUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BillTypeServiceImpl implements BillTypeService {

    @Autowired
    private BillTypeRepository repository;

    @Override
    public BillType getBillType(String billType) {
        final List<BillType> billTypeList = repository.findByType(billType);

        if (CollectionUtils.isEmpty(billTypeList)) {
            return null;
        }
        return billTypeList.get(0);
    }

    @Override
    public List<BillType> getAllBillType() {
        return IterableUtils.toList(repository.findAll());
    }

    @Override
    public void save(BillType billType) {
        repository.save(billType);
    }
}
