package com.kashyap.homeIdeas.billmonitor.repostiory;

import com.kashyap.homeIdeas.billmonitor.model.Bill;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface BillRepository extends ElasticsearchRepository<Bill, String> {

    @Override
    Bill save(Bill bill);
}
