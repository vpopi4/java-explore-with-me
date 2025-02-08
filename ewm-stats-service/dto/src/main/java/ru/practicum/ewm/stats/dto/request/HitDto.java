package ru.practicum.ewm.stats.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.ewm.util.ValidIpAddress;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HitDto {
    @NotBlank
    @Size(max = 30)
    private String app;

    @NotBlank
    @Size(max = 50)
    private String uri;

    @ValidIpAddress
    @NotBlank
    @Size(max = 39)
    private String ip;

    @NotNull
    @PastOrPresent
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;
}
