package impact.moija.dto.community;

import impact.moija.domain.community.Comment;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommentResponseDto {
    long id;
    String content;
    String createdAt;
    boolean isAuthor;

    public static CommentResponseDto of(long loginUserId, Comment comment) {
        return CommentResponseDto.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .createdAt(comment.getCreatedDateTimeToString())
                .isAuthor(comment.isAuthor(loginUserId))
                .build();
    }
}
