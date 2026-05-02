package org.example.spring.course.model;

import lombok.Builder;
import lombok.Getter;

public class CourseDto {

    @Builder
    @Getter
    public static class CourseRes {
        private Long idx;
        private String title;
        private String contents;
        private int price;

        public static CourseRes from (Course entity) {
            return CourseRes.builder()
                    .idx(entity.getIdx())
                    .title(entity.getTitle())
                    .contents(entity.getContents())
                    .price(entity.getPrice())
                    .build();
        }
    }

}
