package com.kashyap.homeIdeas.billmonitor.repostiory;

import com.kashyap.homeIdeas.billmonitor.model.AttachmentDetail;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;
import java.util.Optional;

public interface AttachmentRepository extends ElasticsearchRepository<AttachmentDetail, String>,
        AttachmentESRepository {

    @Override
    AttachmentDetail save(AttachmentDetail attachmentDetail);

    @Override
    void deleteById(String attachmentDetailId);

    @Override
    Optional<AttachmentDetail> findById(String attachmentDetailId);

}
