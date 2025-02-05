package ru.practicum.ewm.stats.dto.response;

import lombok.Data;

@Data
public class StatsDto {
    private String app;
    private String uri;
    private Integer hits;

    public StatsDto(String app, String uri, Integer hits) {
        this.app = app;
        this.uri = uri;
        this.hits = hits;
    }
}
