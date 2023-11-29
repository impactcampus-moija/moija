package impact.moija.dto.community;

import impact.moija.domain.community.Post;
import impact.moija.dto.common.RecommendationResponseDto;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PostPageResponseDto {
    Long id;
    String category;
    String title;
    String nickname;
    String independenceYear;
    String formattedCreatedAt;
    String createdAt;
    String content;
    RecommendationResponseDto recommendation;

    public static PostPageResponseDto of(Post post, RecommendationResponseDto recommendation) {
        return PostPageResponseDto.builder()
                .id(post.getId())
                .category(post.getCategory().getName())
                .title(post.getTitle())
                .nickname(post.getUser().getNickname())
                .content(post.getContent())
                .independenceYear(post.getUser().calculateIndependenceStatus())
                .formattedCreatedAt(post.getCreatedDateTimeToString())
                .createdAt(post.getFormattedCreatedAt())
                .recommendation(recommendation)
                .build();
    }
}
