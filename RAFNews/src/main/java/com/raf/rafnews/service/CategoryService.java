package com.raf.rafnews.service;

import com.raf.rafnews.entities.Category;
import com.raf.rafnews.repository.category.CategoryRepository;

import javax.inject.Inject;
import java.util.List;

public class CategoryService {
    @Inject
    private CategoryRepository categoryRepository;

    public int getCount(){
        return this.categoryRepository.countAllCategories();
    }
    public List<Category> allCategoriesByPage(int page, int perPage){
        int offset = (page - 1) * perPage;
        return this.categoryRepository.paginatedCategories(offset,perPage);
    }
    public List<Category> allCategories(){
        return this.categoryRepository.allCategories();
    }
    public Category addCategory(Category category){
        return this.categoryRepository.addCategory(category);
    }
    public Category findCategory(Integer id){
        return this.categoryRepository.findCategory(id);
    }
    public Category updateCategory(Category category, String name){
        return this.categoryRepository.updateCategory(category, name);
    }
    public void deleteCategory(Integer id){
        this.categoryRepository.deleteCategory(id);
    }
}
