package com.raf.rafnews.service;

import com.raf.rafnews.entities.News;
import com.raf.rafnews.repository.news.NewsRepository;

import javax.inject.Inject;
import java.util.List;

public class NewsService {
    @Inject
    NewsRepository newsRepository;

    public int getCount(){
        return this.newsRepository.countAllNews();
    }

    public List<News> getMostReacted(){
        return this.newsRepository.mostReacted();
    }

    public List<News> getRelatedNews(int newsId){
        return this.newsRepository.getRelatedNews(this.newsRepository.findOne(newsId));
    }
    public int getCategoryCount(Integer id){
        return this.newsRepository.countAllNewsInCategory(id);
    }

    public int getTagCount(Integer id){
        return this.newsRepository.countAllNewsWithTag(id);
    }

    public int getSearchCount(String query){
        return this.newsRepository.countAllSearched(query);
    }

    public List<News> allNews(int page, int perPage) {
        int offset = (page - 1) * perPage;
        return this.newsRepository.allNews(offset, perPage);
    }

    public List<News> getSearchedNews(String query, int page, int perPage){
        int offset = (page - 1) * perPage;
        return this.newsRepository.searchedNews(query, offset, perPage);
    }
    public List<News> allNewsByViews(int page, int perPage){
        int offset = (page - 1) * perPage;
        return this.newsRepository.allNewsByViews(offset, perPage);
    }
    public List<News> allNewsInCategory(Integer category_id, int page, int perPage){
        int offset = (page - 1) * perPage;
        return this.newsRepository.allNewsInCategory(category_id, offset, perPage);
    }

    public List<News> allNewsInCategory(Integer id){
        return this.newsRepository.allNewsInCategory(id);
    }
    public List<News> allNewsWithTag(Integer id, int page, int perPage){
        int offset = (page - 1) * perPage;
        return this.newsRepository.allNewsWithTag(id, offset, perPage);
    }
    public News addNews(News news){
        return this.newsRepository.addNews(news);
    }
    public News updateNews(News news, Integer id){
        return this.newsRepository.updateNews(news, id);
    }
    public News findNews(Integer id, String viewerIp){
        return this.newsRepository.findNews(id,viewerIp);
    }
    public News findOne(int id){return this.newsRepository.findOne(id);}
    public News findNewsForCreator(Integer id) { return  this.newsRepository.findNewsForCreator(id); }
    public void deleteNews(Integer id){
        this.newsRepository.deleteNews(id);
    }
}
