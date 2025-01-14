package com.ll.simpleDb;

import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@AllArgsConstructor
public class Article {
    private long id;
    private String title;
    private String body;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
    private boolean isBlind;

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public LocalDateTime getModifiedDate() {
        return modifiedDate;
    }

    public boolean isBlind() {
        return isBlind;
    }

    public static Article fromMap(Map<String, Object> map) {
        return new Article(
                (long) map.get("id"),
                (String) map.get("title"),
                (String) map.get("body"),
                (LocalDateTime) map.get("createdDate"),
                (LocalDateTime) map.get("modifiedDate"),
                (Boolean) map.get("isBlind")
        );
    }
}
