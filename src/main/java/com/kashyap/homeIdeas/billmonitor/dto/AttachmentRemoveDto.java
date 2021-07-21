package com.kashyap.homeIdeas.billmonitor.dto;

import java.util.List;

public class AttachmentRemoveDto {

    private String billId;
    private List<String> attachmentIds;

    public String getBillId() {
        return billId;
    }

    public void setBillId(String billId) {
        this.billId = billId;
    }

    public List<String> getAttachmentIds() {
        return attachmentIds;
    }

    public void setAttachmentIds(List<String> attachmentIds) {
        this.attachmentIds = attachmentIds;
    }
}
