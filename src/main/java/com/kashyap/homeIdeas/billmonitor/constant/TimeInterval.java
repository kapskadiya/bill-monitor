package com.kashyap.homeIdeas.billmonitor.constant;

import com.kashyap.homeIdeas.billmonitor.exception.BillMonitorValidationException;
import org.apache.commons.lang3.StringUtils;

public enum TimeInterval {
   DAY, MONTH, YEAR;

   public static TimeInterval getTimeDuration(String timeIn) {
      if (StringUtils.isBlank(timeIn)) {
         timeIn = "month";
      }

      switch (timeIn.toLowerCase()) {
         case "day": return TimeInterval.DAY;
         case "month": return TimeInterval.MONTH;
         case "year": return TimeInterval.YEAR;
         default: throw new BillMonitorValidationException("Time interval value is empty");
      }
   }
}
