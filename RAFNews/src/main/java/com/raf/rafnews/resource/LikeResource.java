package com.raf.rafnews.resource;

import com.raf.rafnews.service.CommentService;
import com.raf.rafnews.service.LikeService;
import com.raf.rafnews.service.NewsService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/like")
public class LikeResource {

    @Inject
    LikeService likeService;
    @Inject
    NewsService newsService;
    @Inject
    CommentService commentService;

    @GET
    @Path("/news")
    public int isNewsLiked(@HeaderParam("Origin") String user,
                           @QueryParam("newsId") int newsId){
        return this.likeService.isNewsLiked(user,this.newsService.findOne(newsId));
    }

    @GET
    @Path("/comment")
    public int isCommentLiked(@HeaderParam("Origin") String user,
                              @QueryParam("commentId") int commentId){
        return this.likeService.isCommentLiked(user,this.commentService.findComment(commentId));
    }

    @POST
    @Path("/news")
    public int likeNews(@HeaderParam("Origin") String origin,
                        @QueryParam("newsId") int newsId,
                        @QueryParam("likeValue") int likeValue){
        return this.likeService.addNewsLike(origin,this.newsService.findOne(newsId),likeValue);
    }

    @POST
    @Path("/comment")
    public int likeComment(@HeaderParam("Origin") String origin,
                           @QueryParam("commentId") int commentId,
                           @QueryParam("likeValue") int likeValue){
        return this.likeService.addCommentLike(origin,this.commentService.findComment(commentId),likeValue);
    }

    @GET
    @Path("/news/all")
    public int allLikesNews(@QueryParam("newsId") int newsId){
        return this.likeService.countLikesNews(this.newsService.findOne(newsId));
    }

    @GET
    @Path("/news/dislikes/all")
    public int allDislikesNews(@QueryParam("newsId") int newsId){
        return this.likeService.countDislikesNews(this.newsService.findOne(newsId));
    }

    @GET
    @Path("/comment/all")
    public int allLikesComment(@QueryParam("commentId") Integer commentId){
        return this.likeService.countLikesComment(this.commentService.findComment(commentId));
    }

    @GET
    @Path("/comment/dislikes/all")
    public int allDislikesComment(@QueryParam("commentId") int commentId){
        return this.likeService.countDislikesComment(this.commentService.findComment(commentId));
    }

    @DELETE
    @Path("/delete")
    @Produces(MediaType.APPLICATION_JSON)
    public void deleteNews(@QueryParam("newsId") Integer newsId,@HeaderParam("Origin") String origin){
        this.likeService.deleteLike(newsId,origin);
    }

    @DELETE
    @Path("/deleteComment")
    @Produces(MediaType.APPLICATION_JSON)
    public void deleteComment(@QueryParam("commentId") Integer commentId,@HeaderParam("Origin") String origin){
        this.likeService.deleteLikeComment(commentId,origin);
    }
}
