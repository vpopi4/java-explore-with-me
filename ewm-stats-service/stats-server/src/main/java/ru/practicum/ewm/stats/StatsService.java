package ru.practicum.ewm.stats;

import org.springframework.lang.Nullable;
import ru.practicum.ewm.exception.ClientException;
import ru.practicum.ewm.stats.dto.request.HitDto;
import ru.practicum.ewm.stats.dto.response.StatsDto;
import ru.practicum.ewm.stats.model.Stats;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsService {
    Stats hit(HitDto dto) throws ClientException;

    List<StatsDto> getStats(LocalDateTime start,
                            LocalDateTime end,
                            @Nullable List<String> uris,
                            Boolean isUnique) throws ClientException;
}
