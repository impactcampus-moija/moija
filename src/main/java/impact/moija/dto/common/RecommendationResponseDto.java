package impact.moija.dto.common;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RecommendationResponseDto {
    boolean hasRecommend;
    long recommendationCount;
}
