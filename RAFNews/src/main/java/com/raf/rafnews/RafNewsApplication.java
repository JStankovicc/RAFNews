package com.raf.rafnews;

import com.raf.rafnews.repository.category.CategoryRepository;
import com.raf.rafnews.repository.category.MySqlCategoryRepository;
import com.raf.rafnews.repository.comment.CommentRepository;
import com.raf.rafnews.repository.comment.MySqlCommentRepository;
import com.raf.rafnews.repository.like.LikeRepository;
import com.raf.rafnews.repository.like.MySqlLikeRepository;
import com.raf.rafnews.repository.news.MySqlNewsRepository;
import com.raf.rafnews.repository.news.NewsRepository;
import com.raf.rafnews.repository.newstag.MySqlNewsTagRepository;
import com.raf.rafnews.repository.newstag.NewsTagRepository;
import com.raf.rafnews.repository.tag.MySqlTagRepository;
import com.raf.rafnews.repository.tag.TagRepository;
import com.raf.rafnews.repository.user.MySqlUserRepository;
import com.raf.rafnews.repository.user.UserRepository;
import com.raf.rafnews.repository.view.MySqlViewRepository;
import com.raf.rafnews.repository.view.ViewRepository;
import com.raf.rafnews.service.*;
import org.glassfish.jersey.internal.inject.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;

import javax.inject.Singleton;
import javax.ws.rs.ApplicationPath;

@ApplicationPath("/api")
public class RafNewsApplication extends ResourceConfig {
    public RafNewsApplication(){
        property(ServerProperties.BV_SEND_ERROR_IN_RESPONSE, true);

        AbstractBinder binder = new AbstractBinder() {
            @Override
            protected void configure() {
                this.bind(MySqlUserRepository.class).to(UserRepository.class).in(Singleton.class);
                this.bind(MySqlCategoryRepository.class).to(CategoryRepository.class).in(Singleton.class);
                this.bind(MySqlTagRepository.class).to(TagRepository.class).in(Singleton.class);
                this.bind(MySqlNewsRepository.class).to(NewsRepository.class).in(Singleton.class);
                this.bind(MySqlCommentRepository.class).to(CommentRepository.class).in(Singleton.class);
                this.bind(MySqlNewsTagRepository.class).to(NewsTagRepository.class).in(Singleton.class);
                this.bind(MySqlViewRepository.class).to(ViewRepository.class).in(Singleton.class);
                this.bind(MySqlLikeRepository.class).to(LikeRepository.class).in(Singleton.class);

                this.bindAsContract(UserService.class);
                this.bindAsContract(CategoryService.class);
                this.bindAsContract(TagService.class);
                this.bindAsContract(NewsService.class);
                this.bindAsContract(CommentService.class);
                this.bindAsContract(NewsTagService.class);
                this.bindAsContract(ViewService.class);
                this.bindAsContract(LikeService.class);

            }
        };
        register(binder);
        packages("com.raf.rafnews");
    }
}