package de.fau.cs.mad.fablab.rest.server.core;


import de.fau.cs.mad.fablab.rest.core.Category;

import java.util.ArrayList;
import java.util.List;

public class CategoryFacade {

    private CategoryDAO mCategoryDao;

    public CategoryFacade(CategoryDAO aCategoryDao){
        mCategoryDao = aCategoryDao;
    }


    public List<Category> findAll(){
        return mCategoryDao.findAll();
    }

    public List<String> getAutoCompletions(){
        List<Category> categories = mCategoryDao.findAll();
        List<String> categoryNames = new ArrayList<>();
        for(Category category : categories){
            categoryNames.add(category.getName());
        }
        return categoryNames;
    }
}
