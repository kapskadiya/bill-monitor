package com.kashyap.homeIdeas.billmonitor.repostiory;

import com.kashyap.homeIdeas.billmonitor.model.AttachmentDetail;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @author Kashyap Kadiya
 * @since 2021-06
 */
public interface AttachmentRepository extends ElasticsearchRepository<AttachmentDetail, String> {

}
