package com.raf.rafnews.resource;

import com.raf.rafnews.entities.News;
import com.raf.rafnews.service.NewsService;

import javax.enterprise.inject.New;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/news")

public class NewsResource {
    @Inject
    private NewsService newsService;


    @GET
    @Path("/count")
    public int getNewsCount(){
        return this.newsService.getCount();
    }

    @GET
    @Path("/count/category/{id}")
    public int getNewsCountCategory(@PathParam("id") Integer id){
        return this.newsService.getCategoryCount(id);
    }

    @GET
    @Path("/count/tag/{id}")
    public int getNewsCountTag(@PathParam("id") Integer id){
        return this.newsService.getTagCount(id);
    }

    @GET
    @Path("/search/count/{query}")
    public int getSearchedCount(@PathParam("query") String query){
        return this.newsService.getSearchCount(query);
    }

    @GET
    @Path("/search/{query}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<News> searchedNews(@PathParam("query") String query,
                                   @QueryParam("page") @DefaultValue("1") int page,
                                   @QueryParam("perPage") @DefaultValue("10") int perPage){
        return this.newsService.getSearchedNews(query, page, perPage);
    }

    @GET
    @Path("/allNews")
    @Produces(MediaType.APPLICATION_JSON)
    public List<News> allNews(
            @QueryParam("page") @DefaultValue("1") int page,
            @QueryParam("perPage") @DefaultValue("10") int perPage
    ) {
        return this.newsService.allNews(page, perPage);
    }
    @GET
    @Path("/mostreactedto")
    @Produces(MediaType.APPLICATION_JSON)
    public List<News> reactedToNews(){
        return this.newsService.getMostReacted();
    }

    @GET
    @Path("/similartags")
    @Produces(MediaType.APPLICATION_JSON)
    public List<News> relatedNews(@QueryParam("newsId") int newsId){
        return this.newsService.getRelatedNews(newsId);
    }

    @GET
    @Path("/byViews")
    @Produces(MediaType.APPLICATION_JSON)
    public List<News> allNewsByViews(@QueryParam("page") @DefaultValue("1") int page,
                                     @QueryParam("perPage") @DefaultValue("10") int perPage){
        return this.newsService.allNewsByViews(page, perPage);
    }

    @GET
    @Path("/category/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<News> allNewsInCategory(@PathParam("id") Integer id,
                                        @QueryParam("page") @DefaultValue("1") int page,
                                        @QueryParam("perPage") @DefaultValue("10") int perPage){
        return this.newsService.allNewsInCategory(id,page,perPage);
    }


    @GET
    @Path("/tag/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<News> allNewsWithTag(@PathParam("id") Integer id,
                                     @QueryParam("page") @DefaultValue("1") int page,
                                     @QueryParam("perPage") @DefaultValue("10") int perPage){
        return this.newsService.allNewsWithTag(id,page,perPage);
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public News addNews(@Valid News news){
        return this.newsService.addNews(news);
    }

    @POST
    @Path("/update/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public News updateNews(@Valid News news, @PathParam("id") Integer id){
        return this.newsService.updateNews(news, id);
    }

    @GET
    @Path("/find/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public News findNews(@PathParam("id") Integer id, @HeaderParam("Origin") String viewerIp) {
        return this.newsService.findNews(id, viewerIp);
    }

    @GET
    @Path("/findforcreator/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public News findNewsForCreator(@PathParam("id") Integer id) { return this.newsService.findNewsForCreator(id) ;}

    @DELETE
    @Path("/delete/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public void deleteNews(@PathParam("id") Integer id){
        this.newsService.deleteNews(id);
    }
}
