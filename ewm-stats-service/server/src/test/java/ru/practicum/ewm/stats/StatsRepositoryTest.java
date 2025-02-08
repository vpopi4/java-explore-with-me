package ru.practicum.ewm.stats;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.practicum.ewm.stats.dto.response.StatsDto;
import ru.practicum.ewm.stats.model.Stats;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ExtendWith(SpringExtension.class)
class StatsRepositoryTest {
    @Autowired
    private StatsRepository statsRepository;

    private LocalDateTime start;
    private LocalDateTime end;

    @BeforeEach
    void setUp() {
        start = LocalDateTime.of(2022, 9, 1, 10, 0, 0);
        end = LocalDateTime.of(2022, 9, 6, 18, 0, 0);
    }

    @Test
    void findUniqueStats_shouldReturnUniqueStats() {
        LocalDateTime now = LocalDateTime.of(2022, 9, 3, 12, 0, 0);
        Stats stat1 = new Stats(null, "ewm-main-service", "/events/1", "192.168.0.1", now);
        Stats stat2 = new Stats(null, "ewm-main-service", "/events/1", "192.168.0.2", now);
        statsRepository.saveAll(Arrays.asList(stat1, stat2));

        List<String> uris = List.of("/events/1");

        List<StatsDto> result = statsRepository.findUniqueStats(start, end, uris);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("ewm-main-service", result.get(0).getApp());
        assertEquals("/events/1", result.get(0).getUri());
        assertEquals(2, result.get(0).getHits());
    }

    @Test
    void findAllStats_shouldReturnAllStats() {
        LocalDateTime now = LocalDateTime.of(2022, 9, 3, 12, 0, 0);
        Stats stat1 = new Stats(null, "ewm-main-service", "/events/1", "192.168.0.1", now);
        Stats stat2 = new Stats(null, "ewm-main-service", "/events/1", "192.168.0.2", now);
        Stats stat3 = new Stats(null, "ewm-main-service", "/events/2", "192.168.0.3", now);
        statsRepository.saveAll(Arrays.asList(stat1, stat2, stat3));

        List<String> uris = List.of("/events/1");

        List<StatsDto> result = statsRepository.findAllStats(start, end, uris);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("ewm-main-service", result.get(0).getApp());
        assertEquals("/events/1", result.get(0).getUri());
        assertEquals(2, result.get(0).getHits());
    }

    @Test
    void findUniqueStats_shouldReturnEmptyWhenNoMatchingStats() {
        List<String> uris = List.of("/events/999");

        List<StatsDto> result = statsRepository.findUniqueStats(start, end, uris);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void findAllStats_shouldReturnEmptyWhenNoMatchingStats() {
        List<String> uris = List.of("/events/999");

        List<StatsDto> result = statsRepository.findAllStats(start, end, uris);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void findUniqueStats_shouldHandleNullUris() {
        LocalDateTime now = LocalDateTime.of(2022, 9, 3, 12, 0, 0);
        Stats stat1 = new Stats(null, "ewm-main-service", "/events/1", "192.168.0.1", now);
        Stats stat2 = new Stats(null, "ewm-main-service", "/events/2", "192.168.0.2", now);
        statsRepository.saveAll(Arrays.asList(stat1, stat2));

        List<StatsDto> result = statsRepository.findUniqueStats(start, end, null);

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void findAllStats_shouldHandleNullUris() {
        LocalDateTime now = LocalDateTime.of(2022, 9, 3, 12, 0, 0);
        Stats stat1 = new Stats(null, "ewm-main-service", "/events/1", "192.168.0.1", now);
        Stats stat2 = new Stats(null, "ewm-main-service", "/events/2", "192.168.0.2", now);
        statsRepository.saveAll(Arrays.asList(stat1, stat2));

        List<StatsDto> result = statsRepository.findAllStats(start, end, null);

        assertNotNull(result);
        assertEquals(2, result.size());
    }
}
