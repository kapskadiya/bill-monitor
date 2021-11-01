package com.kashyap.homeIdeas.billmonitor.repostiory;

import com.kashyap.homeIdeas.billmonitor.constant.BillType;
import com.kashyap.homeIdeas.billmonitor.model.Bill;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

/**
 * @author Kashyap Kadiya
 * @since 2021-06
 */
public interface BillRepository extends ElasticsearchRepository<Bill, String>, BillCustomRepository {

    List<Bill> findByCustomerId(String customerName);

    List<Bill> findByType(BillType type);

    List<Bill> findByIsDeleted(boolean isDeleted);

    List<Bill> findByBillId(String billId);

}
