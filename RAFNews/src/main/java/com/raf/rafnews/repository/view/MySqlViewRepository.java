package com.raf.rafnews.repository.view;

import com.raf.rafnews.entities.View;
import com.raf.rafnews.repository.MySqlAbstractRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;

public class MySqlViewRepository extends MySqlAbstractRepository implements ViewRepository {
    @Override
    public View addView(int newsId, String viewerIp) {
        View view = null;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = this.newConnection();

            String createdAt = LocalDate.now().toString();

            preparedStatement = connection.prepareStatement("SELECT * FROM views WHERE news_id=? AND viewer_ip=?");
            preparedStatement.setInt(1, newsId);
            preparedStatement.setString(2, viewerIp);
            resultSet = preparedStatement.executeQuery();

            if (!resultSet.next()) {
                String[] generatedColumns = {"id"};
                preparedStatement = connection.prepareStatement("INSERT INTO views (news_id, viewer_ip, created_at) VALUES (?, ?, ?)", generatedColumns);
                preparedStatement.setInt(1, newsId);
                preparedStatement.setString(2, viewerIp);
                preparedStatement.setString(3, createdAt);
                preparedStatement.executeUpdate();

                resultSet = preparedStatement.getGeneratedKeys();

                if (resultSet.next()) {
                    view = new View();
                    view.setId(resultSet.getInt(1));
                    view.setNewsId(newsId);
                    view.setViewerIp(viewerIp);
                    view.setCreatedAt(createdAt);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.closeStatement(preparedStatement);
            this.closeResultSet(resultSet);
            this.closeConnection(connection);
        }

        return view;
    }

    @Override
    public int countOfViews(int newsId) {
        int viewCount = 0;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = this.newConnection();

            preparedStatement = connection.prepareStatement("SELECT COUNT(*) FROM views WHERE news_id=?");
            preparedStatement.setInt(1, newsId);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                viewCount = resultSet.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.closeStatement(preparedStatement);
            this.closeResultSet(resultSet);
            this.closeConnection(connection);
        }

        return viewCount;
    }
}
