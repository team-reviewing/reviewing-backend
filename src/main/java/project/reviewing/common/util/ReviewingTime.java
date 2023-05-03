package project.reviewing.common.util;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ReviewingTime implements Time {

    @Override
    public LocalDateTime now() {
        return LocalDateTime.now();
    }
}
