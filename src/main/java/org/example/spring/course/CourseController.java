package org.example.spring.course;

import lombok.RequiredArgsConstructor;
import org.example.spring.course.model.Course;
import org.example.spring.course.model.CourseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class CourseController {

    private final CourseService courseService;

    @GetMapping("/course/list")
    public ResponseEntity<List<CourseDto.CourseRes>> getAllCourseList() {
        List<CourseDto.CourseRes> courseDtoList = new ArrayList<>();
        List<Course> courseList = courseService.getAllCourse();

        for (Course course : courseList) {
            courseDtoList.add(CourseDto.CourseRes.from(course));
        }
        return ResponseEntity.ok(courseDtoList);
    }
}
