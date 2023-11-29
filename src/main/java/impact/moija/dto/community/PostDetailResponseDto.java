package impact.moija.dto.community;

import impact.moija.domain.community.Comment;
import impact.moija.domain.community.Post;
import impact.moija.dto.common.RecommendationResponseDto;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PostDetailResponseDto {
    Long id;
    String category;
    String title;
    String nickname;
    String independenceYear;
    String formattedCreatedAt;
    String createdAt;
    String content;
    RecommendationResponseDto recommendation;
    List<CommentResponseDto> comments;

    public static PostDetailResponseDto of(long loginUserId, Post post, RecommendationResponseDto recommendation) {
        return PostDetailResponseDto.builder()
                .id(post.getId())
                .category(post.getCategory().getName())
                .title(post.getTitle())
                .nickname(post.getUser().getNickname())
                .content(post.getContent())
                .independenceYear(post.getUser().calculateIndependenceStatus())
                .formattedCreatedAt(post.getCreatedDateTimeToString())
                .createdAt(post.getFormattedCreatedAt())
                .recommendation(recommendation)
                .comments(post.getComments().stream().map(
                        comment -> CommentResponseDto.of(loginUserId, comment)).collect(Collectors.toList())
                )
                .build();
    }
}
