package com.kashyap.homeIdeas.billmonitor.repostiory;

import com.kashyap.homeIdeas.billmonitor.model.Bill;

import java.io.IOException;
import java.util.List;

public interface BillCustomRepository {
    boolean bulkInsert(List<Bill> billList) throws IOException;
}
