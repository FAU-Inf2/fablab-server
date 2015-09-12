package de.fau.cs.mad.fablab.rest.server.resources;


import de.fau.cs.mad.fablab.rest.api.CategoryApi;
import de.fau.cs.mad.fablab.rest.core.Category;
import de.fau.cs.mad.fablab.rest.server.core.CategoryFacade;
import io.dropwizard.hibernate.UnitOfWork;

import java.util.List;

public class CategoryResource implements CategoryApi{

    private CategoryFacade mCategoryFacade;

    public CategoryResource(CategoryFacade aCategoryFacade){
        mCategoryFacade = aCategoryFacade;
    }

    @UnitOfWork
    @Override
    public List<Category> findAll() {
        return mCategoryFacade.findAll();
    }

    @UnitOfWork
    @Override
    public List<String> getAutoCompletions() {
        return mCategoryFacade.getAutoCompletions();
    }
}
