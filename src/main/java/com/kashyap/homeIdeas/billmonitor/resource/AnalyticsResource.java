package com.kashyap.homeIdeas.billmonitor.resource;

import com.kashyap.homeIdeas.billmonitor.model.Bill;
import com.kashyap.homeIdeas.billmonitor.model.BillType;
import com.kashyap.homeIdeas.billmonitor.service.AnalyticsService;
import com.kashyap.homeIdeas.billmonitor.service.BillService;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/rest/analytics")
public class AnalyticsResource {

    @Autowired
    private AnalyticsService service;

    @GetMapping(value = "/monthAndAmount/{billType}")
    public Map<String, Integer> getMonthAndAmount(@PathVariable String billType) {
        final BillType type = BillType.getBillType(billType);

         return service.getMonthAndAmount(type);
    }
}
