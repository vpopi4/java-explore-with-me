package ru.practicum.ewm.stats;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.exception.ClientException;
import ru.practicum.ewm.stats.dto.request.HitDto;
import ru.practicum.ewm.stats.dto.response.StatsDto;
import ru.practicum.ewm.stats.model.Stats;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class StatsServiceImpl implements StatsService {
    private final StatsRepository statsRepository;

    @Override
    public Stats hit(HitDto dto) throws ClientException {
        Stats stats = Stats.builder()
                .app(dto.getApp())
                .uri(dto.getUri())
                .ip(dto.getIp())
                .timestamp(dto.getTimestamp())
                .build();

        return statsRepository.save(stats);
    }

    @Override
    @Transactional(readOnly = true)
    public List<StatsDto> getStats(LocalDateTime start,
                                   LocalDateTime end,
                                   List<String> uris,
                                   Boolean isUnique) throws ClientException {
        if (isUnique) {
            return statsRepository.findUniqueStats(start, end, uris);
        } else {
            return statsRepository.findAllStats(start, end, uris);
        }
    }
}
