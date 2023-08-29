package com.raf.rafnews.repository.category;

import com.raf.rafnews.entities.Category;
import com.raf.rafnews.repository.MySqlAbstractRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class MySqlCategoryRepository extends MySqlAbstractRepository implements CategoryRepository {

    @Override
    public int countAllCategories() {
        int totalCount = 0;
        Connection connection = null;
        ResultSet resultSet = null;
        PreparedStatement statement = null;
        try {
            connection = this.newConnection();
            statement = connection.prepareStatement("SELECT COUNT(*) FROM category");
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
    public List<Category> allCategories() {
        List<Category> categories = new ArrayList<>();

        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            connection = this.newConnection();
            statement = connection.createStatement();

            String query = "SELECT * FROM category";
            resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                Category category = new Category(resultSet.getInt(1),
                        resultSet.getString("name"),
                        resultSet.getString("description"));
                categories.add(category);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.closeStatement(statement);
            this.closeResultSet(resultSet);
            this.closeConnection(connection);
        }
        return categories;
    }

    @Override
    public List<Category> paginatedCategories(int offset, int perPage) {
        List<Category> categories = new ArrayList<>();

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = this.newConnection();
            statement = connection.prepareStatement("SELECT * FROM category LIMIT ?,?");
            statement.setInt(1,offset);
            statement.setInt(2,perPage);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Category category = new Category(resultSet.getInt(1),
                        resultSet.getString("name"),
                        resultSet.getString("description"));
                categories.add(category);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.closeStatement(statement);
            this.closeResultSet(resultSet);
            this.closeConnection(connection);
        }
        return categories;
    }

    @Override
    public Category addCategory(Category category) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = this.newConnection();

            String query = "SELECT * FROM category WHERE name=?";

            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, category.getName());

            resultSet = preparedStatement.executeQuery();

            if (!resultSet.next()) {
                String[] generatedColumns = {"id"};

                preparedStatement = connection.prepareStatement(
                        "INSERT INTO category (name, description) VALUES (?, ?)", generatedColumns
                );
                preparedStatement.setString(1, category.getName());
                preparedStatement.setString(2, category.getDescription());
                preparedStatement.executeUpdate();

                resultSet = preparedStatement.getGeneratedKeys();

                if (resultSet.next()) {
                    category.setId(resultSet.getInt(1));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.closeStatement(preparedStatement);
            this.closeResultSet(resultSet);
            this.closeConnection(connection);
        }
        return category;
    }

    @Override
    public Category findCategory(Integer id) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Category category = null;
        try {
            connection = this.newConnection();
            preparedStatement = connection.prepareStatement("SELECT * FROM Category WHERE id = ?");
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                category = new Category(resultSet.getInt(1),
                        resultSet.getString("name"),
                        resultSet.getString("description"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.closeStatement(preparedStatement);
            this.closeResultSet(resultSet);
            this.closeConnection(connection);
        }
        return category;
    }

    @Override
    public Category updateCategory(Category category, String name) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = this.newConnection();
            preparedStatement = connection.prepareStatement(
                    "UPDATE category SET category.name = ?, category.description = ? WHERE category.name = ?"
            );
            preparedStatement.setString(1, category.getName());
            preparedStatement.setString(2, category.getDescription());
            preparedStatement.setString(3, name);
            preparedStatement.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.closeStatement(preparedStatement);
            this.closeConnection(connection);
        }
        return category;
    }

    @Override
    public void deleteCategory(Integer id) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = this.newConnection();
            preparedStatement = connection.prepareStatement("DELETE FROM category WHERE id = ?");
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
