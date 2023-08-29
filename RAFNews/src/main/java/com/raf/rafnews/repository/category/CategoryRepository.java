package com.raf.rafnews.repository.category;

import com.raf.rafnews.entities.Category;

import java.util.List;

public interface CategoryRepository {
    List<Category> allCategories();
    List<Category> paginatedCategories(int offset, int perPage);
    int countAllCategories();
    Category addCategory(Category category);
    Category findCategory(Integer id);
    Category updateCategory(Category category, String name);
    void deleteCategory(Integer id);
}
