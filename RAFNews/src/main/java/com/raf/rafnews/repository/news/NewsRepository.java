package com.raf.rafnews.repository.news;

import com.raf.rafnews.entities.News;

import java.util.List;

public interface NewsRepository {
    int countAllNews();
    int countAllNewsInCategory(Integer id);
    int countAllNewsWithTag(Integer id);
    int countAllSearched(String query);
    List<News> allNews(int offset, int limit);
    List<News> searchedNews(String query, int offset, int perPage);
    List<News> allNewsByViews(int offset, int limit);
    List<News> allNewsInCategory(Integer category_id, int offset, int limit);
    List<News> allNewsInCategory(Integer category_id);
    List<News> allNewsWithTag(Integer id, int offset, int limit);
    List<News> mostReacted();
    List<News> getRelatedNews(News news);
    News addNews(News news);
    News updateNews(News news, Integer id);
    News findNews(Integer id, String viewerIp);
    News findOne(int id);
    News findNewsForCreator(Integer id);
    void deleteNews(Integer id);

}
