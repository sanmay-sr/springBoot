package com.bfh.qualifier.startup;

import com.bfh.qualifier.config.AppProperties;
import com.bfh.qualifier.webhook.GenerateWebhookResponse;
import com.bfh.qualifier.webhook.WebhookClient;
import com.bfh.qualifier.solution.SolutionProvider;
import com.bfh.qualifier.store.Submission;
import com.bfh.qualifier.store.SubmissionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class StartupRunner implements ApplicationRunner {
    private static final Logger log = LoggerFactory.getLogger(StartupRunner.class);

    private final WebhookClient webhookClient;
    private final SolutionProvider solutionProvider;
    private final SubmissionRepository submissionRepository;
    private final AppProperties props;

    public StartupRunner(WebhookClient webhookClient,
                         SolutionProvider solutionProvider,
                         SubmissionRepository submissionRepository,
                         AppProperties props) {
        this.webhookClient = webhookClient;
        this.solutionProvider = solutionProvider;
        this.submissionRepository = submissionRepository;
        this.props = props;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("Starting qualifier flow for {} ({})", props.getName(), props.getRegNo());

        GenerateWebhookResponse resp = webhookClient.generateWebhook(props.getName(), props.getRegNo(), props.getEmail());

        String finalQuery = solutionProvider.provideFinalQuery(props.getRegNo());

        Submission submission = new Submission();
        submission.setRegNo(props.getRegNo());
        submission.setEmail(props.getEmail());
        submission.setWebhookUrl(resp.webhook());
        submission.setAccessToken(resp.accessToken());
        submission.setFinalQuery(finalQuery);
        submissionRepository.save(submission);

        webhookClient.postFinalQuery(resp.webhook(), resp.accessToken(), finalQuery);

        log.info("Submitted final query successfully");
    }
}


