package com.raf.rafnews.repository.like;

import com.raf.rafnews.entities.Comment;
import com.raf.rafnews.entities.Like;
import com.raf.rafnews.entities.News;
import com.raf.rafnews.entities.User;
import com.raf.rafnews.repository.MySqlAbstractRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class MySqlLikeRepository extends MySqlAbstractRepository implements LikeRepository {
    @Override
    public int isNewsLiked(String user, News news) {
        Like likeObj = new Like(user, true, news.getId());
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = this.newConnection();
            preparedStatement = connection.prepareStatement("SELECT * FROM likes WHERE user_ip = ? AND post_id = ? AND post_or_comment = ?");
            preparedStatement.setString(1,user);
            preparedStatement.setInt(2,news.getId());
            preparedStatement.setBoolean(3,true);
            resultSet = preparedStatement.executeQuery();
            preparedStatement = null;
            if (!resultSet.next()){
                likeObj.setLike(0);
            }else {
                likeObj.setLike(resultSet.getInt(5));
            }
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            this.closeStatement(preparedStatement);
            this.closeResultSet(resultSet);
            this.closeConnection(connection);
        }
        return likeObj.getLike();
    }

    @Override
    public int isCommentLiked(String user, Comment comment) {
        Like likeObj = new Like(user, false, comment.getId());
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = this.newConnection();
            preparedStatement = connection.prepareStatement("SELECT * FROM likes WHERE user_ip = ? AND post_id = ? AND post_or_comment = ?");
            preparedStatement.setString(1,user);
            preparedStatement.setInt(2,comment.getId());
            preparedStatement.setBoolean(3,false);
            resultSet = preparedStatement.executeQuery();
            preparedStatement = null;
            if (!resultSet.next()){
                likeObj.setLike(0);
            }else {
                likeObj.setLike(resultSet.getInt(5));
            }
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            this.closeStatement(preparedStatement);
            this.closeResultSet(resultSet);
            this.closeConnection(connection);
        }
        return likeObj.getLike();
    }

    @Override
    public int likeNews(String user, News news, int like) {
        Like likeObj = new Like(user, true, news.getId(), like);
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = this.newConnection();
            preparedStatement = connection.prepareStatement("SELECT * FROM likes WHERE user_ip = ? AND post_id = ? AND post_or_comment = ?");
            preparedStatement.setString(1,user);
            preparedStatement.setInt(2,news.getId());
            preparedStatement.setBoolean(3,true);
            resultSet = preparedStatement.executeQuery();
            preparedStatement = null;
            if (!resultSet.next()){
                preparedStatement = connection.prepareStatement("INSERT INTO likes (user_ip, post_or_comment, post_id, like_value) VALUES (?,?,?,?)");
                preparedStatement.setString(1,likeObj.getUserIp());
                preparedStatement.setInt(3,likeObj.getPostId());
                preparedStatement.setBoolean(2,true);
                preparedStatement.setInt(4,likeObj.getLike());
                int x = preparedStatement.executeUpdate();
            }else {
                preparedStatement = connection.prepareStatement("UPDATE likes SET like_value = ? WHERE user_ip = ? AND post_id = ? AND post_or_comment = ?");
                preparedStatement.setInt(1,like);
                preparedStatement.setString(2, likeObj.getUserIp());
                preparedStatement.setInt(3, likeObj.getPostId());
                preparedStatement.setBoolean(4,true);
                preparedStatement.executeUpdate();
            }
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            this.closeStatement(preparedStatement);
            this.closeResultSet(resultSet);
            this.closeConnection(connection);
        }
        return likeObj.getLike();
    }

    @Override
    public int likeComment(String user, Comment comment, int like) {
        Like likeObj = new Like(user, false, comment.getId(), like);
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = this.newConnection();
            preparedStatement = connection.prepareStatement("SELECT * FROM likes WHERE user_ip = ? AND post_id = ? AND post_or_comment = ?");
            preparedStatement.setString(1,user);
            preparedStatement.setInt(2,comment.getId());
            preparedStatement.setBoolean(3,false);
            resultSet = preparedStatement.executeQuery();
            preparedStatement = null;
            if (!resultSet.next()){
                preparedStatement = connection.prepareStatement("INSERT INTO likes (user_ip,post_or_comment,post_id,like_value) VALUES (?,?,?,?)");
                preparedStatement.setString(1,likeObj.getUserIp());
                preparedStatement.setInt(3,likeObj.getPostId());
                preparedStatement.setBoolean(2,false);
                preparedStatement.setInt(4,likeObj.getLike());
                preparedStatement.executeUpdate();
            }else {
                preparedStatement = connection.prepareStatement("UPDATE likes SET like_value = ? WHERE user_ip = ? AND post_id = ? AND post_or_comment = ?");
                preparedStatement.setInt(1, likeObj.getLike());
                preparedStatement.setString(2, likeObj.getUserIp());
                preparedStatement.setInt(3, likeObj.getPostId());
                preparedStatement.setBoolean(4, false);
                preparedStatement.executeUpdate();
            }
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            this.closeStatement(preparedStatement);
            this.closeResultSet(resultSet);
            this.closeConnection(connection);
        }
        return likeObj.getLike();
    }

    @Override
    public int countLikesForNews(News news){
        int likeCount = 0;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = this.newConnection();

            preparedStatement = connection.prepareStatement("SELECT COUNT(*) FROM likes WHERE post_id = ? AND post_or_comment = ? AND like_value = 1");
            preparedStatement.setInt(1, news.getId());
            preparedStatement.setBoolean(2,true);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                likeCount = resultSet.getInt(1);
            }else {
                likeCount = 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.closeStatement(preparedStatement);
            this.closeResultSet(resultSet);
            this.closeConnection(connection);
        }

        return likeCount;
    }

    @Override
    public int countDislikesForNews(News news){
        int likeCount = 0;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = this.newConnection();

            preparedStatement = connection.prepareStatement("SELECT COUNT(*) FROM likes WHERE post_id = ? AND post_or_comment = ? AND like_value = -1");
            preparedStatement.setInt(1, news.getId());
            preparedStatement.setBoolean(2,true);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                likeCount = resultSet.getInt(1);
            }else {
                likeCount = 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.closeStatement(preparedStatement);
            this.closeResultSet(resultSet);
            this.closeConnection(connection);
        }

        return likeCount;
    }

    @Override
    public void removeLike(Integer newsId, String userIp){
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = this.newConnection();
            preparedStatement = connection.prepareStatement("DELETE FROM likes WHERE post_id = ? AND user_ip = ? AND post_or_comment = true");
            preparedStatement.setInt(1, newsId);
            preparedStatement.setString(2,userIp);
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.closeStatement(preparedStatement);
            this.closeConnection(connection);
        }
    }

    @Override
    public int countLikesForComment(Comment comment){
        int likeCount = 0;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = this.newConnection();

            preparedStatement = connection.prepareStatement("SELECT COUNT(*) FROM likes WHERE post_id = ? AND post_or_comment = ? AND like_value = 1");
            preparedStatement.setInt(1, comment.getId());
            preparedStatement.setBoolean(2,false);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                likeCount = resultSet.getInt(1);
            }else {
                likeCount = 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.closeStatement(preparedStatement);
            this.closeResultSet(resultSet);
            this.closeConnection(connection);
        }

        return likeCount;
    }

    @Override
    public int countDislikesForComment(Comment comment){
        int likeCount = 0;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = this.newConnection();

            preparedStatement = connection.prepareStatement("SELECT COUNT(*) FROM likes WHERE post_id = ? AND post_or_comment = ? AND like_value = -1");
            preparedStatement.setInt(1, comment.getId());
            preparedStatement.setBoolean(2,false);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                likeCount = resultSet.getInt(1);
            }else {
                likeCount = 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.closeStatement(preparedStatement);
            this.closeResultSet(resultSet);
            this.closeConnection(connection);
        }

        return likeCount;
    }

    @Override
    public void removeCommentLike(Integer commentId, String userIp){
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = this.newConnection();
            preparedStatement = connection.prepareStatement("DELETE FROM likes WHERE post_id = ? AND user_ip = ? AND post_or_comment = false");
            preparedStatement.setInt(1, commentId);
            preparedStatement.setString(2,userIp);
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.closeStatement(preparedStatement);
            this.closeConnection(connection);
        }
    }
}
