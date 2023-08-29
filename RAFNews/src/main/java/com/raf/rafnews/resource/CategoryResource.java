package com.raf.rafnews.resource;

import com.raf.rafnews.entities.Category;
import com.raf.rafnews.service.CategoryService;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/categories")

public class CategoryResource {

    @Inject
    private CategoryService categoryService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Category> allCategories(){
        return this.categoryService.allCategories();
    }

    @GET
    @Path("/allCategories")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Category> allCategories(@QueryParam("page") @DefaultValue("1") int page,
                                        @QueryParam("perPage") @DefaultValue("10") int perPage){
        return this.categoryService.allCategoriesByPage(page, perPage);
    }

    @GET
    @Path("/count")
    public int getCategoryCount(){
        return this.categoryService.getCount();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Category addCategory(@Valid Category category){
        return this.categoryService.addCategory(category);
    }

    @GET
    @Path("/find/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Category findCategory(@PathParam("id") Integer id){
        return this.categoryService.findCategory(id);
    }

    @POST
    @Path("/update/{name}")
    @Produces(MediaType.APPLICATION_JSON)
    public Category updateCategory(@Valid Category category, @PathParam("name") String name){
        return this.categoryService.updateCategory(category, name);
    }
    @DELETE
    @Path("/delete/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public void deleteCategory(@PathParam("id") Integer id){
        this.categoryService.deleteCategory(id);
    }
}
