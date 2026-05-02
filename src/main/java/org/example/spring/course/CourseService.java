package org.example.spring.course;

import kotlinx.serialization.Required;
import lombok.RequiredArgsConstructor;
import org.example.spring.course.model.Course;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CourseService {
    private final CourseRepository courseRepository;

    public List<Course> getAllCourse() {
        return courseRepository.findAll();
    }
}
