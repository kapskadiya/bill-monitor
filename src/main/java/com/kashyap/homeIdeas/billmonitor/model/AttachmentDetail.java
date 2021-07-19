package com.kashyap.homeIdeas.billmonitor.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.List;

@Document(indexName = "attachment")
public class AttachmentDetail {

    @Id
    @ReadOnlyProperty
    private String id;

    @Field(type = FieldType.Text)
    private String billId;

    @Field(type = FieldType.Long)
    private long totalAttachmentSize;

    @Field(type = FieldType.Nested)
    private List<Attachment> attachments;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBillId() {
        return billId;
    }

    public void setBillId(String billId) {
        this.billId = billId;
    }

    public long getTotalAttachmentSize() {
        return totalAttachmentSize;
    }

    public void setTotalAttachmentSize(long totalAttachmentSize) {
        this.totalAttachmentSize = totalAttachmentSize;
    }

    public List<Attachment> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<Attachment> attachments) {
        this.attachments = attachments;
    }
}
