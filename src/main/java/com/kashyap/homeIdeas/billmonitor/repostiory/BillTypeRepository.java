package com.kashyap.homeIdeas.billmonitor.repostiory;

import com.kashyap.homeIdeas.billmonitor.model.BillType;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface BillTypeRepository extends ElasticsearchRepository<BillType, String> {

    List<BillType> findByType(String type);
}
