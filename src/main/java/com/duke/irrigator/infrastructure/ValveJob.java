package com.duke.irrigator.infrastructure;

import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

@Component
public class ValveJob extends QuartzJobBean {
    private static final Logger logger = LoggerFactory.getLogger(ValveJob.class);

//    @Autowired
//    private JavaMailSender mailSender;
//
//    @Autowired
//    private MailProperties mailProperties;

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        logger.info("Executing Job with key {}", jobExecutionContext.getJobDetail().getKey());

        JobDataMap jobDataMap = jobExecutionContext.getMergedJobDataMap();
        String subject = jobDataMap.getString("subject");
        String body = jobDataMap.getString("body");
        String recipientEmail = jobDataMap.getString("email");

        controlValve("TurnOn");
    }

    private void controlValve(String input) {
            logger.info("Control Valve");
//        try {
//            logger.info("Sending Email to {}", toEmail);
//            MimeMessage message = mailSender.createMimeMessage();
//
//            MimeMessageHelper messageHelper = new MimeMessageHelper(message, StandardCharsets.UTF_8.toString());
//            messageHelper.setSubject(subject);
//            messageHelper.setText(body, true);
//            messageHelper.setFrom(fromEmail);
//            messageHelper.setTo(toEmail);
//
//            mailSender.send(message);
//        } catch (MessagingException ex) {
//            logger.error("Failed to send email to {}", toEmail);
//        }
    }
}
