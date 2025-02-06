package ru.practicum.ewm.stats.dto.response;

import lombok.Data;

@Data
public class StatsDto {
    private String app;
    private String uri;
    private Long hits;

    public StatsDto(String app, String uri, Long hits) {
        this.app = app;
        this.uri = uri;
        this.hits = hits;
    }
}
