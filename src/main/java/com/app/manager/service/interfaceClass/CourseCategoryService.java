package com.app.manager.service.interfaceClass;


import com.app.manager.model.payload.request.CourseCategoryRequest;
import com.app.manager.model.payload.response.CourseCategoryResponse;
import com.app.manager.model.returnResult.DatabaseQueryResult;

import java.util.List;

public interface CourseCategoryService {
    List<CourseCategoryResponse> getAll();
    DatabaseQueryResult save(CourseCategoryRequest courseCategoryRequest);
    DatabaseQueryResult update(String courseCategoryId,
                               CourseCategoryRequest courseCategoryRequest);
}
