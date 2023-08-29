package com.raf.rafnews.entities;

public class Like {
    private int id;
    private String userIp;
    private boolean postOrComment; //POST -> TRUE
    private int postId;
    private int like; //-1,0,1

    public Like(String userIp, boolean postOrComment, int postId) {
        this.id = id;
        this.userIp = userIp;
        this.postOrComment = postOrComment;
        this.postId = postId;
    }

    public Like(String userIp, boolean postOrComment, int postId, int like) {
        this.userIp = userIp;
        this.postOrComment = postOrComment;
        this.postId = postId;
        this.like = like;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserIp() {
        return userIp;
    }

    public void setUserIp(String userIp) {
        this.userIp = userIp;
    }

    public boolean isPostOrComment() {
        return postOrComment;
    }

    public void setPostOrComment(boolean postOrComment) {
        this.postOrComment = postOrComment;
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public int getLike() {
        return like;
    }

    public void setLike(int like) {
        this.like = like;
    }
}
