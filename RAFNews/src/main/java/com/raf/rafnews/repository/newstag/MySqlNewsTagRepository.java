package com.raf.rafnews.repository.newstag;

import com.raf.rafnews.repository.MySqlAbstractRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class MySqlNewsTagRepository extends MySqlAbstractRepository implements NewsTagRepository {
    @Override
    public void addNewsTag(Integer news, Integer tag) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = this.newConnection();
            preparedStatement = connection.prepareStatement("INSERT INTO news_tags (news_id, tag_id) VALUES (?, ?)");
            preparedStatement.setInt(1, news);
            preparedStatement.setInt(2, tag);
            preparedStatement.executeUpdate();
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            this.closeConnection(connection);
            this.closeStatement(preparedStatement);
        }
    }

    @Override
    public void deleteNewsTag(Integer news) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = this.newConnection();
            preparedStatement = connection.prepareStatement("DELETE FROM news_tags WHERE news_id = ?");
            preparedStatement.setInt(1, news);
            preparedStatement.executeUpdate();
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            this.closeConnection(connection);
            this.closeStatement(preparedStatement);
        }
    }

    @Override
    public void deleteTagFromNews(Integer news, Integer tag) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = this.newConnection();
            preparedStatement = connection.prepareStatement("DELETE FROM news_tags WHERE news_id = ? AND tag_id = ?");
            preparedStatement.setInt(1, news);
            preparedStatement.setInt(2, tag);
            preparedStatement.executeUpdate();
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            this.closeConnection(connection);
            this.closeStatement(preparedStatement);
        }
    }
}
