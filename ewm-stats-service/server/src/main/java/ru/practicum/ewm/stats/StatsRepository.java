package ru.practicum.ewm.stats;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.ewm.stats.dto.response.StatsDto;
import ru.practicum.ewm.stats.model.Stats;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsRepository extends JpaRepository<Stats, Integer> {
    @Query("SELECT new ru.practicum.ewm.stats.dto.response.StatsDto(s.app, s.uri, COUNT(DISTINCT s.ip)) " +
            "FROM Stats s " +
            "WHERE s.timestamp BETWEEN :start AND :end " +
            "AND (:uris IS NULL OR s.uri IN :uris) " +
            "GROUP BY s.app, s.uri " +
            "ORDER BY COUNT(DISTINCT s.ip) DESC")
    List<StatsDto> findUniqueStats(@Param("start") LocalDateTime start,
                                   @Param("end") LocalDateTime end,
                                   @Param("uris") List<String> uris);

    @Query("SELECT new ru.practicum.ewm.stats.dto.response.StatsDto(s.app, s.uri, COUNT(s.ip)) " +
            "FROM Stats s " +
            "WHERE s.timestamp BETWEEN :start AND :end " +
            "AND (:uris IS NULL OR s.uri IN :uris) " +
            "GROUP BY s.app, s.uri " +
            "ORDER BY COUNT(s.ip) DESC")
    List<StatsDto> findAllStats(@Param("start") LocalDateTime start,
                                @Param("end") LocalDateTime end,
                                @Param("uris") List<String> uris);
}
