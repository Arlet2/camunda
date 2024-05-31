package org.joulin.delegate;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.variable.value.TypedValue;
import org.camunda.spin.impl.json.jackson.JacksonJsonNode;
import org.joulin.core.AdPost;
import org.joulin.core.Article;
import org.joulin.core.enums.AdPostStatus;
import org.joulin.core.enums.ArticleStatus;
import org.joulin.repos.AdPostRepo;
import org.joulin.repos.ArticleRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.*;

@Component
public class PublishArticle implements JavaDelegate {
    @Autowired
    private final ArticleRepo articleRepo;
    private final AdPostRepo adPostRepo;

    public PublishArticle(ArticleRepo articleRepo, AdPostRepo adPostRepo) {
        this.articleRepo = articleRepo;
        this.adPostRepo = adPostRepo;
    }

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        String title = (String) delegateExecution.getVariable("article_title");
        String text = (String) delegateExecution.getVariable("article_text");
        Long article_ad_id = (Long) delegateExecution.getVariable("article_ad_id");
        JacksonJsonNode img_links_json = (JacksonJsonNode) delegateExecution.getVariable("img_links");
        List<String> images = img_links_json.elements().stream()
                .map(map -> (String) map.prop("link").value()).toList();

        List<AdPost> adPosts = new ArrayList<>();
        if (article_ad_id != null) {
            adPostRepo.findById(article_ad_id).ifPresent(adPosts::add);
        }

        Article article = new Article(
            0, title, text, images, adPosts, ArticleStatus.PUBLISHED, null, null
        );
        articleRepo.save(article);
    }

    class ImageLinks implements Serializable {
        public List<ImageLink> links;
        public ImageLinks(List<ImageLink> links) {
            this.links = links;
        }

        public List<ImageLink> getLinks() {
            return links;
        }

        public void setLinks(List<ImageLink> links) {
            this.links = links;
        }
    }
    class ImageLink implements Serializable {
        public String link;
        public ImageLink(String link) {
            this.link = link;
        }

        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
        }
    }
}
