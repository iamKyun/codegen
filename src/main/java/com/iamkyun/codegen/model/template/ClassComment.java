package com.iamkyun.codegen.model.template;

import lombok.Data;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
public class ClassComment {
    private String comment;
    private String author;
    private String date;

    public static ClassComment of(String comment, String author) {
        ClassComment classComment = new ClassComment();
        classComment.setComment(comment);
        classComment.setAuthor(author);
        classComment.setDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        return classComment;
    }
}