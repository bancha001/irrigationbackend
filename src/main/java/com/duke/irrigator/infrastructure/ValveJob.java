package com.duke.irrigator.infrastructure;

import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

@Component
public class ValveJob extends QuartzJobBean {
    private static final Logger logger = LoggerFactory.getLogger(ValveJob.class);

    @Autowired
    private BeagleBoneBlackIO beagleBoneBlackIO;

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        logger.info("Executing Job with key {}", jobExecutionContext.getJobDetail().getKey());

        JobDataMap jobDataMap = jobExecutionContext.getMergedJobDataMap();
        int zoneId = jobDataMap.getInt("zoneId");
        String action = jobDataMap.getString("action");

        controlValve(zoneId,action);
    }

    private void controlValve(int zoneId, String action) {
        logger.info("Control Valve Zone: "+ zoneId+ " State: "+action);
        beagleBoneBlackIO.setPin(zoneId,action);
    }
}
