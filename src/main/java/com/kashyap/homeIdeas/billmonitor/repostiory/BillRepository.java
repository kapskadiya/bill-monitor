package com.kashyap.homeIdeas.billmonitor.repostiory;

import com.kashyap.homeIdeas.billmonitor.model.Bill;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.Optional;

public interface BillRepository extends ElasticsearchRepository<Bill, String> {

    @Override
    Bill save(Bill bill);

    @Override
    Optional<Bill> findById(String billId);
}
