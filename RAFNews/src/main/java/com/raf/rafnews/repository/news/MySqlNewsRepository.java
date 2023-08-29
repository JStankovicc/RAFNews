package com.raf.rafnews.repository.news;

import com.raf.rafnews.entities.Comment;
import com.raf.rafnews.entities.News;
import com.raf.rafnews.entities.Tag;
import com.raf.rafnews.helpers.CommentSortDate;
import com.raf.rafnews.helpers.NewsSortDate;
import com.raf.rafnews.repository.MySqlAbstractRepository;
import com.raf.rafnews.service.ViewService;

import javax.inject.Inject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public class MySqlNewsRepository extends MySqlAbstractRepository implements NewsRepository {

    @Inject
    ViewService viewService;
    @Override
    public int countAllNews() {
        int totalCount = 0;
        Connection connection = null;
        ResultSet resultSet = null;
        PreparedStatement statement = null;
        try {
            connection = this.newConnection();
            statement = connection.prepareStatement("SELECT COUNT(*) FROM news");
            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                totalCount = resultSet.getInt(1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return totalCount;
    }

    @Override
    public int countAllNewsInCategory(Integer id) {
        int totalCount = 0;
        Connection connection = null;
        ResultSet resultSet = null;
        PreparedStatement statement = null;
        try {
            connection = this.newConnection();
            statement = connection.prepareStatement("SELECT COUNT(*) FROM news WHERE category_id = ?");
            statement.setInt(1,id);
            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                totalCount = resultSet.getInt(1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return totalCount;
    }

    @Override
    public int countAllNewsWithTag(Integer id) {
        int totalCount = 0;
        Connection connection = null;
        ResultSet resultSet = null;
        PreparedStatement statement = null;
        try {
            connection = this.newConnection();
            statement = connection.prepareStatement("SELECT COUNT(*) FROM news_tags WHERE tag_id = ?");
            statement.setInt(1,id);
            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                totalCount = resultSet.getInt(1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return totalCount;
    }

    @Override
    public int countAllSearched(String query) {
        int totalCount = 0;
        Connection connection = null;
        ResultSet resultSet = null;
        PreparedStatement statement = null;
        try {
            connection = this.newConnection();
            statement = connection.prepareStatement("SELECT COUNT(*) FROM news n WHERE UPPER(n.title) LIKE UPPER(?) OR UPPER(n.content) LIKE UPPER(?)");
            statement.setString(1, "%" + query.toUpperCase() + "%");
            statement.setString(2, "%" + query.toUpperCase() + "%");
            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                totalCount = resultSet.getInt(1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return totalCount;
    }

    @Override
    public List<News> searchedNews(String query, int offset, int perPage){
        Connection connection = null;
        ResultSet resultSet = null;
        ResultSet resultSet2 = null;
        PreparedStatement statement = null;
        List<News> news = new ArrayList<>();
        try{
            connection = this.newConnection();
            statement = connection.prepareStatement("SELECT * FROM news  WHERE UPPER(title) LIKE UPPER(?) OR UPPER(content) LIKE UPPER(?) LIMIT ? , ?");
            statement.setString(1, "%" + query.toUpperCase() + "%");
            statement.setString(2, "%" + query.toUpperCase() + "%");
            statement.setInt(3, offset);
            statement.setInt(4, perPage);
            resultSet = statement.executeQuery();

            while(resultSet.next()){
                News newsObj = new News(resultSet.getInt(1),
                        resultSet.getString("title"),
                        resultSet.getString("author"),
                        resultSet.getString("content"),
                        resultSet.getString("createdAt"),
                        resultSet.getInt("category_id"));

                statement = connection.prepareStatement("SELECT * FROM category WHERE id = ?");
                statement.setInt(1, newsObj.getCategory_id());
                resultSet2 = statement.executeQuery();
                if (resultSet2.next()) {
                    newsObj.setCategoryName(resultSet2.getString("name"));
                }
                news.add(newsObj);
            }
        }catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.closeStatement(statement);
            this.closeResultSet(resultSet);
            if (resultSet2 != null) {
                this.closeResultSet(resultSet2);
            }
            this.closeConnection(connection);
        }
        return news;
    }

    @Override
    public List<News> allNews(int offset, int limit) {
        Connection connection = null;
        ResultSet resultSet = null;
        PreparedStatement statement = null;
        ResultSet resultSet2 = null;

        List<News> news = new ArrayList<>();
        List<News> finalNews = new ArrayList<>();

        try {
            connection = this.newConnection();
            statement = connection.prepareStatement("SELECT * FROM news LIMIT ?, ?");
            statement.setInt(1, offset);
            statement.setInt(2, limit);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                News newsObj = new News(resultSet.getInt(1),
                        resultSet.getString("title"),
                        resultSet.getString("author"),
                        resultSet.getString("content"),
                        resultSet.getString("createdAt"),
                        resultSet.getInt("category_id"));

                statement = connection.prepareStatement("SELECT * FROM category WHERE id = ?");
                statement.setInt(1, newsObj.getCategory_id());
                resultSet2 = statement.executeQuery();
                if (resultSet2.next()) {
                    newsObj.setCategoryName(resultSet2.getString("name"));
                }
                news.add(newsObj);
            }
            news.sort(new NewsSortDate().reversed());
            int i = 0;
            int bound = 0;
            if (news.size() < 10) {
                bound = news.size();
            } else
                bound = 10;
            for (i = 0; i < bound; i++) {
                finalNews.add(news.get(i));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.closeStatement(statement);
            this.closeResultSet(resultSet);
            if (resultSet2 != null) {
                this.closeResultSet(resultSet2);
            }
            this.closeConnection(connection);
        }
        return finalNews;
    }

    @Override
    public List<News> mostReacted() {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        List<News> popularNews = new ArrayList<>();

        try {
            connection = this.newConnection();
            statement = connection.prepareStatement(
                    "SELECT news.id, news.title, news.author, news.content, news.createdAt, news.category_id, " +
                            "COUNT(likes.id) AS likes_count " +
                            "FROM news " +
                            "LEFT JOIN likes ON news.id = likes.post_id AND likes.post_or_comment = 1 " +
                            "GROUP BY news.id, news.title, news.author, news.content, news.createdAt, news.category_id " +
                            "ORDER BY likes_count DESC " +
                            "LIMIT 3");
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                News newsObj = new News(resultSet.getInt("id"),
                        resultSet.getString("title"),
                        resultSet.getString("author"),
                        resultSet.getString("content"),
                        resultSet.getString("createdAt"),
                        resultSet.getInt("category_id"));

                statement = connection.prepareStatement("SELECT name FROM category WHERE id = ?");
                statement.setInt(1, newsObj.getCategory_id());
                ResultSet categoryResultSet = statement.executeQuery();
                if (categoryResultSet.next()) {
                    newsObj.setCategoryName(categoryResultSet.getString("name"));
                }
                popularNews.add(newsObj);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.closeStatement(statement);
            this.closeResultSet(resultSet);
            this.closeConnection(connection);
        }

        return popularNews;
    }

    @Override
    public List<News> allNewsByViews(int offset, int perPage) {
        List<News> newsList = new ArrayList<>();
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = this.newConnection();

            statement = connection.prepareStatement("SELECT n.*, COALESCE(v.num_views, 0) AS num_views, c.name AS category_name " +
                                    "FROM news n " +
                                    "LEFT JOIN (" +
                                    "   SELECT news_id, COUNT(*) AS num_views FROM views GROUP BY news_id" +
                                    ") v ON n.id = v.news_id " +
                                    "LEFT JOIN category c ON n.category_id = c.id " +
                                    "ORDER BY num_views DESC, n.id ASC " +
                                    "LIMIT ?, ?");
            statement.setInt(1, offset);
            statement.setInt(2, perPage);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                News news = new News(
                        resultSet.getInt("id"),
                        resultSet.getString("title"),
                        resultSet.getString("author"),
                        resultSet.getString("content"),
                        resultSet.getString("createdAt"),
                        resultSet.getInt("category_id")
                );
                news.setCategoryName(resultSet.getString("category_name"));
                newsList.add(news);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.closeStatement(statement);
            if (resultSet != null) {
                this.closeResultSet(resultSet);
            }
            this.closeConnection(connection);
        }

        return newsList;
    }

    @Override
    public List<News> allNewsInCategory(Integer category_id, int offset, int perPage) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        ResultSet resultSet2 = null;
        List<News> news = new ArrayList<>();
        try {
            connection = this.newConnection();
            preparedStatement = connection.prepareStatement("SELECT * FROM news WHERE category_id = ? LIMIT ?,?");
            preparedStatement.setInt(1, category_id);
            preparedStatement.setInt(2, offset);
            preparedStatement.setInt(3, perPage);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                News newsObj = new News(resultSet.getInt(1),
                        resultSet.getString("title"),
                        resultSet.getString("author"),
                        resultSet.getString("content"),
                        resultSet.getString("createdAt"),
                        category_id);

                preparedStatement = connection.prepareStatement("SELECT * FROM category WHERE id = ?");
                preparedStatement.setInt(1, category_id);
                resultSet2 = preparedStatement.executeQuery();
                if (resultSet2.next()) {
                    newsObj.setCategoryName(resultSet2.getString("name"));
                }
                news.add(newsObj);
            }
            news.sort(new NewsSortDate().reversed());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.closeStatement(preparedStatement);
            if (resultSet != null) {
                this.closeResultSet(resultSet);
            }
            if (resultSet2 != null) {
                this.closeResultSet(resultSet2);
            }
            this.closeConnection(connection);
        }

        return news;
    }

    @Override
    public List<News> allNewsInCategory(Integer category_id) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        ResultSet resultSet2 = null;
        List<News> news = new ArrayList<>();
        try {
            connection = this.newConnection();
            preparedStatement = connection.prepareStatement("SELECT * FROM news WHERE category_id = ?");
            preparedStatement.setInt(1, category_id);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                News newsObj = new News(resultSet.getInt(1),
                        resultSet.getString("title"),
                        resultSet.getString("author"),
                        resultSet.getString("content"),
                        resultSet.getString("createdAt"),
                        category_id);

                preparedStatement = connection.prepareStatement("SELECT * FROM category WHERE id = ?");
                preparedStatement.setInt(1, category_id);
                resultSet2 = preparedStatement.executeQuery();
                if (resultSet2.next()) {
                    newsObj.setCategoryName(resultSet2.getString("name"));
                }
                news.add(newsObj);
            }
            news.sort(new NewsSortDate().reversed());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.closeStatement(preparedStatement);
            if (resultSet != null) {
                this.closeResultSet(resultSet);
            }
            if (resultSet2 != null) {
                this.closeResultSet(resultSet2);
            }
            this.closeConnection(connection);
        }

        return news;
    }

    @Override
    public List<News> allNewsWithTag(Integer id, int offset, int perPage) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        PreparedStatement preparedStatement2 = null;
        ResultSet resultSet = null;
        ResultSet resultSet2 = null;
        List<News> news = new ArrayList<>();
        List<Integer> newsIDs = new ArrayList<>();
        String searchedTag = "";
        try {
            connection = this.newConnection();
            preparedStatement = connection.prepareStatement("SELECT * FROM news_tags WHERE tag_id = ? LIMIT ?,?");
            preparedStatement.setInt(1, id);
            preparedStatement.setInt(2, offset);
            preparedStatement.setInt(3, perPage);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                newsIDs.add(resultSet.getInt("news_id"));
            }
            preparedStatement = connection.prepareStatement("SELECT * FROM tag WHERE id = ?");
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                searchedTag = resultSet.getString("tag");
            }
            for (Integer newsId : newsIDs) {
                preparedStatement = connection.prepareStatement("SELECT * FROM news WHERE id = ?");
                preparedStatement.setInt(1, newsId);

                resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    News newsObj = new News(resultSet.getInt(1),
                            resultSet.getString("title"),
                            resultSet.getString("author"),
                            resultSet.getString("content"),
                            resultSet.getString("createdAt"),
                            resultSet.getInt("category_id"));

                    preparedStatement2 = connection.prepareStatement("SELECT * FROM category WHERE id = ?");
                    preparedStatement2.setInt(1, newsObj.getCategory_id());
                    resultSet2 = preparedStatement2.executeQuery();
                    if (resultSet2.next()) {
                        newsObj.setCategoryName(resultSet2.getString("name"));
                    }
                    newsObj.setSearchedTag(searchedTag);
                    news.add(newsObj);
                }
            }
            news.sort(new NewsSortDate().reversed());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.closeStatement(preparedStatement);
            this.closeStatement(preparedStatement2);
            if (resultSet != null) {
                this.closeResultSet(resultSet);
            }
            if (resultSet2 != null) {
                this.closeResultSet(resultSet2);
            }
            this.closeConnection(connection);
        }
        return news;
    }

    @Override
    public List<News> getRelatedNews(News news) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Set<Integer> uniqueNewsIds = new HashSet<>();
        List<News> relatedNews = new ArrayList<>();

        try {
            connection = this.newConnection();

            List<Integer> tagIds = news.getTagIds();
            for (Integer tagId : tagIds) {
                preparedStatement = connection.prepareStatement("SELECT news_id FROM news_tags WHERE tag_id = ?");
                preparedStatement.setInt(1, tagId);
                resultSet = preparedStatement.executeQuery();

                while (resultSet.next()) {
                    int relatedNewsId = resultSet.getInt("news_id");

                    if (relatedNewsId != news.getId() && !uniqueNewsIds.contains(relatedNewsId)) {
                        preparedStatement = connection.prepareStatement("SELECT * FROM news WHERE id = ?");
                        preparedStatement.setInt(1, relatedNewsId);
                        ResultSet newsResultSet = preparedStatement.executeQuery();

                        if (newsResultSet.next()) {
                            News relatedNewsObj = new News(newsResultSet.getInt("id"),
                                    newsResultSet.getString("title"),
                                    newsResultSet.getString("author"),
                                    newsResultSet.getString("content"),
                                    newsResultSet.getString("createdAt"),
                                    newsResultSet.getInt("category_id"));


                            relatedNews.add(relatedNewsObj);
                            uniqueNewsIds.add(relatedNewsId);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.closeStatement(preparedStatement);
            if (resultSet != null) {
                this.closeResultSet(resultSet);
            }
            this.closeConnection(connection);
        }

        Collections.shuffle(relatedNews);
        return relatedNews.subList(0, Math.min(3, relatedNews.size()));
    }

    @Override
    public News addNews(News news) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = this.newConnection();
            String[] generatedColumns = {"id"};
            preparedStatement = connection.prepareStatement(
                    "INSERT INTO news (title, author, content, createdAt, category_id, user_id) VALUES (?, ?, ?, ?, ?, ?)",
                    generatedColumns);
            preparedStatement.setString(1, news.getTitle());
            preparedStatement.setString(2, news.getAuthor());
            preparedStatement.setString(3, news.getContent());
            preparedStatement.setString(4, LocalDate.now().toString());
            preparedStatement.setInt(5, news.getCategory_id());
            preparedStatement.setInt(6, news.getUser_id());
            preparedStatement.executeUpdate();
            resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                news.setId(resultSet.getInt(1));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.closeStatement(preparedStatement);
            this.closeResultSet(resultSet);
            this.closeConnection(connection);
        }
        return news;
    }

    @Override
    public News updateNews(News news, Integer id) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = this.newConnection();
            preparedStatement = connection.prepareStatement(
                    "UPDATE news SET news.title = ?, news.content = ?, news.category_id = ? WHERE id = ?");
            preparedStatement.setString(1, news.getTitle());
            preparedStatement.setString(2, news.getContent());
            preparedStatement.setInt(3, news.getCategory_id());
            preparedStatement.setInt(4, id);
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.closeStatement(preparedStatement);
            this.closeConnection(connection);
        }
        return null;
    }

    @Override
    public News findOne(int id){
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        News news = null;
        try {
            connection = this.newConnection();
            preparedStatement = connection.prepareStatement("SELECT * FROM news WHERE id = ?");
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                news = new News(resultSet.getInt(1),
                        resultSet.getString("title"),
                        resultSet.getString("author"),
                        resultSet.getString("content"),
                        resultSet.getString("createdAt"),
                        resultSet.getInt("category_id"));
            }
            preparedStatement = connection.prepareStatement("SELECT * FROM category WHERE id = ?");
            preparedStatement.setInt(1, news.getCategory_id());
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                news.setCategoryName(resultSet.getString("name"));
            }

            preparedStatement = connection.prepareStatement("SELECT * FROM comment WHERE news_id = ?");
            preparedStatement.setInt(1, news.getId());
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                news.getComments().add(new Comment(resultSet.getInt(1),
                        resultSet.getString("author"),
                        resultSet.getString("text"),
                        resultSet.getString("postedAt"),
                        news.getId()));
            }
            news.getComments().sort(new CommentSortDate().reversed());

            preparedStatement = connection.prepareStatement("SELECT * FROM news_tags WHERE news_id = ?");
            preparedStatement.setInt(1, news.getId());
            resultSet = preparedStatement.executeQuery();
            List<Integer> tagIds = new ArrayList<>();
            while (resultSet.next()) {
                tagIds.add(resultSet.getInt("tag_id"));
            }
            for (Integer tagId : tagIds) {
                preparedStatement = connection.prepareStatement("SELECT * FROM tag WHERE id = ?");
                preparedStatement.setInt(1, tagId);
                resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    news.getTags().add(new Tag(resultSet.getInt(1), resultSet.getString("tag")));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.closeStatement(preparedStatement);
            this.closeResultSet(resultSet);
            this.closeConnection(connection);
        }
        return news;
    }
    @Override
    public News findNews(Integer id, String viewerIp) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        News news = null;
        try {
            connection = this.newConnection();
            preparedStatement = connection.prepareStatement("SELECT * FROM news WHERE id = ?");
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                news = new News(resultSet.getInt(1),
                        resultSet.getString("title"),
                        resultSet.getString("author"),
                        resultSet.getString("content"),
                        resultSet.getString("createdAt"),
                        resultSet.getInt("category_id"));
                viewService.addView(news.getId(),viewerIp);
            }

            preparedStatement = connection.prepareStatement("SELECT * FROM category WHERE id = ?");
            preparedStatement.setInt(1, news.getCategory_id());
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                news.setCategoryName(resultSet.getString("name"));
            }

            preparedStatement = connection.prepareStatement("SELECT * FROM comment WHERE news_id = ?");
            preparedStatement.setInt(1, news.getId());
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                news.getComments().add(new Comment(resultSet.getInt(1),
                        resultSet.getString("author"),
                        resultSet.getString("text"),
                        resultSet.getString("postedAt"),
                        news.getId()));
            }
            news.getComments().sort(new CommentSortDate().reversed());

            preparedStatement = connection.prepareStatement("SELECT * FROM news_tags WHERE news_id = ?");
            preparedStatement.setInt(1, news.getId());
            resultSet = preparedStatement.executeQuery();
            List<Integer> tagIds = new ArrayList<>();
            while (resultSet.next()) {
                tagIds.add(resultSet.getInt("tag_id"));
            }
            for (Integer tagId : tagIds) {
                preparedStatement = connection.prepareStatement("SELECT * FROM tag WHERE id = ?");
                preparedStatement.setInt(1, tagId);
                resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    news.getTags().add(new Tag(resultSet.getInt(1), resultSet.getString("tag")));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.closeStatement(preparedStatement);
            this.closeResultSet(resultSet);
            this.closeConnection(connection);
        }
        return news;
    }

    @Override
    public News findNewsForCreator(Integer id) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        News news = null;
        try {
            connection = this.newConnection();
            preparedStatement = connection.prepareStatement("SELECT * FROM news WHERE id = ?");
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                news = new News(resultSet.getInt(1), resultSet.getString("title"), resultSet.getString("author"), resultSet.getString("content"), resultSet.getString("createdAt"), resultSet.getInt("category_id"));
            }
            preparedStatement = connection.prepareStatement("SELECT * FROM category WHERE id = ?");
            preparedStatement.setInt(1, news.getCategory_id());
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                news.setCategoryName(resultSet.getString("name"));
            }

            preparedStatement = connection.prepareStatement("SELECT * FROM comment WHERE news_id = ?");
            preparedStatement.setInt(1, news.getId());
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                news.getComments().add(new Comment(resultSet.getInt(1), resultSet.getString("author"), resultSet.getString("text"), resultSet.getString("postedAt"), news.getId()));
            }
            news.getComments().sort(new CommentSortDate().reversed());

            preparedStatement = connection.prepareStatement("SELECT * FROM news_tags WHERE news_id = ?");
            preparedStatement.setInt(1, news.getId());
            resultSet = preparedStatement.executeQuery();
            List<Integer> tagIds = new ArrayList<>();
            while (resultSet.next()) {
                tagIds.add(resultSet.getInt("tag_id"));
            }
            for (Integer tagId : tagIds) {
                preparedStatement = connection.prepareStatement("SELECT * FROM tag WHERE id = ?");
                preparedStatement.setInt(1, tagId);
                resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    news.getTags().add(new Tag(resultSet.getInt(1), resultSet.getString("tag")));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.closeStatement(preparedStatement);
            this.closeResultSet(resultSet);
            this.closeConnection(connection);
        }
        return news;
    }

    @Override
    public void deleteNews(Integer id) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = this.newConnection();
            preparedStatement = connection.prepareStatement("DELETE FROM news_tags WHERE news_id = ?");
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
            preparedStatement = connection.prepareStatement("DELETE FROM comment WHERE news_id = ?");
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
            preparedStatement = connection.prepareStatement("DELETE FROM news WHERE id = ?");
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.closeStatement(preparedStatement);
            this.closeConnection(connection);
        }
    }
}
