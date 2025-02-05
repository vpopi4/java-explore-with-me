package ru.practicum.ewm.stats;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.ewm.exception.ClientException;
import ru.practicum.ewm.stats.dto.request.HitDto;
import ru.practicum.ewm.stats.dto.response.StatsDto;
import ru.practicum.ewm.stats.model.Stats;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class StatsServiceImplTest {
    @Mock
    private StatsRepository statsRepository;

    @InjectMocks
    private StatsServiceImpl statsService;

    private HitDto hitDto;
    private LocalDateTime start;
    private LocalDateTime end;

    @BeforeEach
    void setUp() {
        hitDto = HitDto.builder()
                .app("ewm-main-service")
                .uri("/events/1")
                .ip("192.168.0.1")
                .timestamp(LocalDateTime.now())
                .build();
        start = LocalDateTime.of(2022, 9, 1, 10, 0, 0);
        end = LocalDateTime.of(2022, 9, 6, 18, 0, 0);
    }

    @Test
    void hit_shouldSaveStatWhenValidDtoIsGiven() throws ClientException {
        // Arrange
        Stats expectedStat = Stats.builder()
                .app(hitDto.getApp())
                .uri(hitDto.getUri())
                .ip(hitDto.getIp())
                .timestamp(hitDto.getTimestamp())
                .build();
        when(statsRepository.save(any(Stats.class))).thenReturn(expectedStat);

        // Act
        Stats result = statsService.hit(hitDto);

        // Assert
        assertNotNull(result);
        assertEquals(hitDto.getApp(), result.getApp());
        assertEquals(hitDto.getUri(), result.getUri());
        assertEquals(hitDto.getIp(), result.getIp());
        verify(statsRepository, times(1)).save(any(Stats.class));
    }

    @Test
    void getStats_shouldReturnStatsWhenValidParametersGiven() throws ClientException {
        // Arrange
        List<String> uris = Arrays.asList("/events/1", "/events/2");
        boolean isUnique = false;
        StatsDto statsDto1 = new StatsDto("ewm-main-service", "/events/1", 6L);
        StatsDto statsDto2 = new StatsDto("ewm-main-service", "/events/2", 4L);
        List<StatsDto> expectedStats = Arrays.asList(statsDto1, statsDto2);

        when(statsRepository.findAllStats(start, end, uris)).thenReturn(expectedStats);

        // Act
        List<StatsDto> result = statsService.getStats(start, end, uris, isUnique);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(statsDto1.getApp(), result.get(0).getApp());
        assertEquals(statsDto1.getUri(), result.get(0).getUri());
        assertEquals(statsDto1.getHits(), result.get(0).getHits());
        verify(statsRepository, times(1)).findAllStats(start, end, uris);
    }

    @Test
    void getStats_shouldReturnUniqueStatsWhenUniqueFlagIsTrue() throws ClientException {
        // Arrange
        List<String> uris = List.of("/events/1");
        boolean isUnique = true;
        StatsDto uniqueStats = new StatsDto("ewm-main-service", "/events/1", 1L);
        List<StatsDto> expectedStats = List.of(uniqueStats);

        when(statsRepository.findUniqueStats(start, end, uris)).thenReturn(expectedStats);

        // Act
        List<StatsDto> result = statsService.getStats(start, end, uris, isUnique);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(uniqueStats.getApp(), result.get(0).getApp());
        assertEquals(uniqueStats.getUri(), result.get(0).getUri());
        assertEquals(uniqueStats.getHits(), result.get(0).getHits());
        verify(statsRepository, times(1)).findUniqueStats(start, end, uris);
    }

    @Test
    void getStats_shouldThrowExceptionWhenStartIsAfterEnd() {
        // Arrange
        LocalDateTime invalidStart = LocalDateTime.of(2022, 9, 10, 10, 0, 0);
        List<String> uris = List.of("/events/1");
        boolean isUnique = false;

        // Act & Assert
        assertThrows(ClientException.class, () -> statsService.getStats(invalidStart, end, uris, isUnique));
    }

    @Test
    void getStats_shouldReturnEmptyWhenNoStatsFound() throws ClientException {
        List<String> uris = List.of("/events/3"); // URI, которого нет в базе
        boolean isUnique = false;

        when(statsRepository.findAllStats(start, end, uris)).thenReturn(List.of());

        List<StatsDto> result = statsService.getStats(start, end, uris, isUnique);

        assertNotNull(result);
        assertTrue(result.isEmpty()); // Ожидаем пустой список
        verify(statsRepository, times(1)).findAllStats(start, end, uris);
    }

    @Test
    void getStats_shouldReturnEmptyWhenUrisIsNull() throws ClientException {
        boolean isUnique = false;
        StatsDto statsDto = new StatsDto("ewm-main-service", "/events/1", 6L);
        List<StatsDto> expectedStats = List.of(statsDto);

        when(statsRepository.findAllStats(start, end, null)).thenReturn(expectedStats);

        List<StatsDto> result = statsService.getStats(start, end, null, isUnique);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(statsDto.getApp(), result.get(0).getApp());
        assertEquals(statsDto.getUri(), result.get(0).getUri());
        assertEquals(statsDto.getHits(), result.get(0).getHits());
        verify(statsRepository, times(1)).findAllStats(start, end, null);
    }

    @Test
    void getStats_shouldReturnStatsWhenUrisIsEmpty() throws ClientException {
        List<String> uris = List.of(); // пустой список
        boolean isUnique = false;
        StatsDto statsDto = new StatsDto("ewm-main-service", "/events/1", 6L);
        List<StatsDto> expectedStats = List.of(statsDto);

        when(statsRepository.findAllStats(start, end, uris)).thenReturn(expectedStats);

        List<StatsDto> result = statsService.getStats(start, end, uris, isUnique);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(statsDto.getApp(), result.get(0).getApp());
        assertEquals(statsDto.getUri(), result.get(0).getUri());
        assertEquals(statsDto.getHits(), result.get(0).getHits());
        verify(statsRepository, times(1)).findAllStats(start, end, uris);
    }

    @Test
    void getStats_shouldReturnStatsWhenStartEqualsEnd() throws ClientException {
        // Arrange
        LocalDateTime sameStartEnd = LocalDateTime.of(2022, 9, 1, 10, 0, 0);
        List<String> uris = List.of("/events/1");
        boolean isUnique = false;
        StatsDto statsDto = new StatsDto("ewm-main-service", "/events/1", 6L);
        List<StatsDto> expectedStats = List.of(statsDto);

        when(statsRepository.findAllStats(sameStartEnd, sameStartEnd, uris)).thenReturn(expectedStats);

        // Act
        List<StatsDto> result = statsService.getStats(sameStartEnd, sameStartEnd, uris, isUnique);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(statsDto.getApp(), result.get(0).getApp());
        assertEquals(statsDto.getUri(), result.get(0).getUri());
        assertEquals(statsDto.getHits(), result.get(0).getHits());
        verify(statsRepository, times(1)).findAllStats(sameStartEnd, sameStartEnd, uris);
    }

    @Test
    void getStats_shouldReturnStatsWhenStartAndEndAreEqualToTimestamps() throws ClientException {
        // Arrange
        LocalDateTime timestamp = LocalDateTime.of(2022, 9, 1, 10, 0, 0);
        List<String> uris = List.of("/events/1");
        boolean isUnique = false;
        StatsDto statsDto = new StatsDto("ewm-main-service", "/events/1", 6L);
        List<StatsDto> expectedStats = List.of(statsDto);

        when(statsRepository.findAllStats(timestamp, timestamp, uris)).thenReturn(expectedStats);

        // Act
        List<StatsDto> result = statsService.getStats(timestamp, timestamp, uris, isUnique);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(statsDto.getApp(), result.get(0).getApp());
        assertEquals(statsDto.getUri(), result.get(0).getUri());
        assertEquals(statsDto.getHits(), result.get(0).getHits());
        verify(statsRepository, times(1)).findAllStats(timestamp, timestamp, uris);
    }

    @Test
    void getStats_shouldHandleEmptyUrisListForUniqueStats() throws ClientException {
        // Arrange
        List<String> uris = List.of(); // пустой список
        boolean isUnique = true;
        StatsDto uniqueStats = new StatsDto("ewm-main-service", "/events/1", 1L);
        List<StatsDto> expectedStats = List.of(uniqueStats);

        when(statsRepository.findUniqueStats(start, end, uris)).thenReturn(expectedStats);

        // Act
        List<StatsDto> result = statsService.getStats(start, end, uris, isUnique);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(uniqueStats.getApp(), result.get(0).getApp());
        assertEquals(uniqueStats.getUri(), result.get(0).getUri());
        assertEquals(uniqueStats.getHits(), result.get(0).getHits());
        verify(statsRepository, times(1)).findUniqueStats(start, end, uris);
    }
}
