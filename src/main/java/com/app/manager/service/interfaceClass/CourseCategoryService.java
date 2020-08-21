package com.app.manager.service.interfaceClass;


import com.app.manager.entity.CourseCategory;

import java.util.Optional;

public interface CourseCategoryService {
    Optional<CourseCategory> getBasicCategory();
    void generateCategory();
}
