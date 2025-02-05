package ru.practicum.ewm.stats;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.exception.ClientException;
import ru.practicum.ewm.stats.dto.request.HitDto;
import ru.practicum.ewm.stats.dto.response.StatsDto;
import ru.practicum.ewm.stats.model.Stats;

import java.time.LocalDateTime;
import java.util.List;


@Slf4j
@RestController
@RequiredArgsConstructor
public class StatsController {
    private final StatsService statsService;

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public Stats hit(@Valid @RequestBody HitDto dto) throws ClientException {
        log.info("--> POST /hit: {}", dto);

        return statsService.hit(dto);
    }

    @GetMapping("/stats")
    @ResponseStatus(HttpStatus.OK)
    public List<StatsDto> getStats(
            @RequestParam("start")
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
            LocalDateTime start,

            @RequestParam("end")
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
            LocalDateTime end,

            @RequestParam(name = "uris", required = false)
            @Size(min = 1, message = "At least one URI must be provided")
            List<String> uris,

            @RequestParam(name = "unique", defaultValue = "false")
            Boolean isUnique
    ) throws ClientException {
        log.info("--> GET /stats: start={}, end={}, uris={}, unique={}", start, end, uris, isUnique);

        return statsService.getStats(start, end, uris, isUnique);
    }
}
