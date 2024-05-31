
package org.joulin.delegate;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.spin.impl.json.jackson.JacksonJsonNode;
import org.joulin.core.AdPost;
import org.joulin.core.AdRequest;
import org.joulin.core.Article;
import org.joulin.core.Auditory;
import org.joulin.core.enums.AdPostStatus;
import org.joulin.core.enums.AdRequestStatus;
import org.joulin.core.enums.ArticleStatus;
import org.joulin.repos.AdPostRepo;
import org.joulin.repos.AdRequestRepo;
import org.joulin.repos.ArticleRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class PublishAdPost implements JavaDelegate {
    private final AdPostRepo adPostRepo;
    private final AdRequestRepo adRequestRepo;

    public PublishAdPost(AdPostRepo adPostRepo, AdRequestRepo adRequestRepo) {
        this.adPostRepo = adPostRepo;
        this.adRequestRepo = adRequestRepo;
    }

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        String auditory = (String) delegateExecution.getVariable("auditory");
        int hour_count = (int) delegateExecution.getVariable("hour_count");
        String ad_request_text = (String) delegateExecution.getVariable("request_text");
        String title = (String) delegateExecution.getVariable("title");
        String ad_text = (String) delegateExecution.getVariable("text");
        String targetLink = (String) delegateExecution.getVariable("target_link");

        AdRequest adRequest = new AdRequest(
                0, new Auditory(auditory, null, null, null),
                ad_request_text, null, null, hour_count, AdRequestStatus.PUBLISHED
        );
        adRequest = adRequestRepo.save(adRequest);

        AdPost adPost = new AdPost(
                0, title, ad_text, targetLink, LocalDateTime.now(),
                null, adRequest, AdPostStatus.PUBLISHED, null
        );

        adPostRepo.save(adPost);
    }
}
