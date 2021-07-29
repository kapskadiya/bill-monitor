package com.kashyap.homeIdeas.billmonitor.repostiory;

import com.kashyap.homeIdeas.billmonitor.model.Bill;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface BillRepository extends ElasticsearchRepository<Bill, String>, BillCustomRepository {

    List<Bill> findByCustomerId(String customerId);

    List<Bill> findByCustomerName(String customerName);
}
