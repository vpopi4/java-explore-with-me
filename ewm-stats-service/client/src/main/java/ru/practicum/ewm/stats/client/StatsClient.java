package ru.practicum.ewm.stats.client;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import ru.practicum.ewm.stats.dto.request.HitDto;
import ru.practicum.ewm.stats.dto.response.StatsDto;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class StatsClient {
    private final RestTemplate restTemplate;
    private final String statsServerUrl = "http://stats-server:8080";

    public void sendHit(HitDto request) {
        String url = statsServerUrl + "/hit";
        HttpEntity<HitDto> requestEntity = new HttpEntity<>(request);
        restTemplate.postForEntity(url, requestEntity, Void.class);
    }

    public List<StatsDto> getStats(LocalDateTime start,
                                   LocalDateTime end,
                                   List<String> uris,
                                   boolean unique) throws UnsupportedEncodingException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String encodedStart = encode(start.format(formatter));
        String encodedEnd = encode(end.format(formatter));

        UriComponentsBuilder urlBuilder = UriComponentsBuilder.fromHttpUrl(statsServerUrl + "/stats")
                .queryParam("start", encodedStart)
                .queryParam("end", encodedEnd)
                .queryParam("unique", unique);

        if (uris != null && !uris.isEmpty()) {
            String encodedUris = uris.stream()
                    .map(this::encode)
                    .collect(Collectors.joining(","));

            urlBuilder.queryParam("uris", encodedUris);
        }

        ResponseEntity<StatsDto[]> response = restTemplate.exchange(
                urlBuilder.toUriString(),
                HttpMethod.GET,
                null,
                StatsDto[].class
        );

        return List.of(Objects.requireNonNull(response.getBody()));
    }

    private String encode(String uri) {
        if (uri == null) {
            return "";
        }

        return URLEncoder.encode(uri, StandardCharsets.UTF_8);
    }
}
