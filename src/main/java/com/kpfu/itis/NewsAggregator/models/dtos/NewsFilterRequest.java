package com.kpfu.itis.NewsAggregator.models.dtos;


import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
public class NewsFilterRequest {

    private List<String> sources; // список источников, например ["BBC", "CNN"]
   // private String region;        // регион, если вы это где-то указывали в сущности
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate startDate;  // начальная дата публикации
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate endDate;    // конечная дата публикации

    // можно добавить и другие поля. Например, ключевые слова и т.п.
}