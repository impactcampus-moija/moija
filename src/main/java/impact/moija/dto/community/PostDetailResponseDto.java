package impact.moija.dto.community;

import impact.moija.domain.community.Comment;
import impact.moija.domain.community.Post;
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
    int thumbsUpCount;
    String content;
    List<CommentResponseDto> comments;

    public static PostDetailResponseDto of(long loginUserId, Post post) {
        return PostDetailResponseDto.builder()
                .id(post.getId())
                .category(post.getCategory().getName())
                .title(post.getTitle())
                .nickname(post.getUser().getNickname())
                .content(post.getContent())
                .independenceYear(post.getUser().calculateIndependenceStatus())
                .formattedCreatedAt(post.getCreatedDateTimeToString())
                .createdAt(post.getFormattedCreatedAt())
                .thumbsUpCount(0)
                .comments(post.getComments().stream().map(
                        comment -> CommentResponseDto.of(loginUserId, comment)).collect(Collectors.toList())
                )
                .build();
    }
}
