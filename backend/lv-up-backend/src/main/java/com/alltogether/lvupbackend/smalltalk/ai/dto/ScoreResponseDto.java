package com.alltogether.lvupbackend.smalltalk.ai.dto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class ScoreResponseDto {
    private int month;
    private int days;

    @JsonProperty("daily_detail")
    private List<List<Integer>> dailyDetail;

    @JsonProperty("daily_overall")
    private List<Integer> dailyOverall;
}

