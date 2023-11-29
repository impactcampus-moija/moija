package impact.moija.dto.community;

import impact.moija.domain.community.Post;
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
    int thumbsUpCount;
    String content;

    public static PostPageResponseDto of(Post post) {
        return PostPageResponseDto.builder()
                .id(post.getId())
                .category(post.getCategory().getName())
                .title(post.getTitle())
                .nickname(post.getUser().getNickname())
                .content(post.getContent())
                .independenceYear(post.getUser().calculateIndependenceStatus())
                .formattedCreatedAt(post.getCreatedDateTimeToString())
                .createdAt(post.getFormattedCreatedAt())
                .thumbsUpCount(0)
                .build();
    }
}
