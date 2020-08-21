package com.app.manager.service.implementClass;

import com.app.manager.entity.CourseCategory;
import com.app.manager.repository.CourseCategoryRepository;
import com.app.manager.service.interfaceClass.CourseCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class CourseCategoryServiceImp implements CourseCategoryService {
    @SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
    @Autowired
    CourseCategoryRepository coursecategoryRepository;

    private static final String basicCategory = "Undefine";
    private static final List<String> categoriesExample =
            Arrays.asList(basicCategory, "Category 1", "Category 2", "Category 3", "Category 4");

    @Override
    public Optional<CourseCategory> getBasicCategory() {
        try {
            var category = coursecategoryRepository.findFirstByName(basicCategory);
            if(category == null){
                var newCategory = new CourseCategory();
                newCategory.setName(basicCategory);
                coursecategoryRepository.save(newCategory);
                return Optional.of(newCategory);
            }
            return Optional.of(category);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public void generateCategory() {
        for (String name: categoriesExample
             ) {
            try {
                var category = coursecategoryRepository.findFirstByName(name);
                if(category == null){
                    var newCategory = new CourseCategory();
                    newCategory.setName(name);
                    newCategory.setDescription(name);
                    coursecategoryRepository.save(newCategory);
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Can create role: " + name);
                System.out.println("Reason: " + e.getMessage());
                System.out.println("Cause by: " + e.getCause().toString());
            }
        }
    }
}
