package com.raf.rafnews.service;

import com.raf.rafnews.entities.Comment;
import com.raf.rafnews.entities.News;
import com.raf.rafnews.entities.User;
import com.raf.rafnews.repository.like.LikeRepository;

import javax.inject.Inject;

public class LikeService {
    @Inject
    LikeRepository likeRepository;

    public int isNewsLiked(String user, News news){
        return this.likeRepository.isNewsLiked(user,news);
    }
    public int isCommentLiked(String user, Comment comment){
        return this.likeRepository.isCommentLiked(user,comment);
    }
    public int addNewsLike(String user, News news, int like){
        return this.likeRepository.likeNews(user,news,like);
    }
    public int addCommentLike(String user, Comment comment, int like){
        return this.likeRepository.likeComment(user, comment, like);
    }

    public int countLikesNews(News news){
        return this.likeRepository.countLikesForNews(news);
    }

    public int countDislikesNews(News news){
        return this.likeRepository.countDislikesForNews(news);
    }

    public int countLikesComment(Comment comment){
        return this.likeRepository.countLikesForComment(comment);
    }

    public int countDislikesComment(Comment comment){
        return this.likeRepository.countDislikesForComment(comment);
    }

    public void deleteLike(Integer newsId,String userIp){
        this.likeRepository.removeLike(newsId,userIp);
    }
    public void deleteLikeComment(Integer commentId,String userIp){
        this.likeRepository.removeCommentLike(commentId,userIp);
    }
}
