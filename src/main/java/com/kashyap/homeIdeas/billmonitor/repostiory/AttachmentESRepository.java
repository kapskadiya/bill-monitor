package com.kashyap.homeIdeas.billmonitor.repostiory;

import java.io.IOException;
import java.util.List;

public interface AttachmentESRepository {
    void removeAttachments(String billId, List<String> attachmentIds) throws IOException;
}
