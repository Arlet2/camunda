package org.joulin.delegate;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.variable.Variables;
import org.camunda.bpm.engine.variable.value.ObjectValue;
import org.joulin.core.AdPost;
import org.joulin.core.enums.AdPostStatus;
import org.joulin.repos.AdPostRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class LoadAds implements JavaDelegate {
    @Autowired
    private final AdPostRepo adPostRepo;

    public LoadAds(AdPostRepo adPostRepo) {
        this.adPostRepo = adPostRepo;
    }

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        List<AdPost> ad_posts = adPostRepo.findAllByStatus(AdPostStatus.READY_TO_PUBLISH);
        System.out.println(ad_posts);
        ObjectValue ad_posts_value =
                Variables.objectValue(ad_posts).serializationDataFormat("application/json").create();

        delegateExecution.setVariable("ad_posts", ad_posts_value);
        delegateExecution.setVariable("has_ad_posts", !ad_posts.isEmpty());
    }
}
