package com.raf.rafnews.repository.comment;

import com.raf.rafnews.entities.Comment;

import java.util.List;

public interface CommentRepository {
    List<Comment> allComments(Integer newsId);
    Comment addComment(Comment comment, Integer newsId);
    Comment findComment(Integer id);
    void deleteComment(Integer id);
}
