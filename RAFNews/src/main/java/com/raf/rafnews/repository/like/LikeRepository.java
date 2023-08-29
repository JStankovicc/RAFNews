package com.raf.rafnews.repository.like;

import com.raf.rafnews.entities.Comment;
import com.raf.rafnews.entities.News;
import com.raf.rafnews.entities.User;

import javax.enterprise.inject.New;

public interface LikeRepository {
    int isNewsLiked(String user, News news);
    int isCommentLiked(String user, Comment comment);

    int likeNews(String user, News news, int like);
    int likeComment(String user, Comment comment, int like);
    int countLikesForNews(News news);
    int countDislikesForNews(News news);
    void removeLike(Integer newsId,String userIp);
    int countLikesForComment(Comment comment);
    int countDislikesForComment(Comment comment);
    void removeCommentLike(Integer commentId, String userIp);

}
