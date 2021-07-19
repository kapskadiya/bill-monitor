package com.kashyap.homeIdeas.billmonitor.repostiory;

import com.kashyap.homeIdeas.billmonitor.model.AttachmentDetail;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface AttachmentRepository extends ElasticsearchRepository<AttachmentDetail, String> {

    @Override
    AttachmentDetail save(AttachmentDetail attachmentDetail);
}
