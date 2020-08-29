package com.app.manager.service.implementClass;

import com.app.manager.context.repository.ExerciseRepository;
import com.app.manager.service.interfaceClass.ExerciseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExerciseServiceImp implements ExerciseService {
    @SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
    @Autowired
    ExerciseRepository exerciseRepository;
}
