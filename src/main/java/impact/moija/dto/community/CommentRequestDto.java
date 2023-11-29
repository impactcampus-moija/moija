package impact.moija.dto.community;

import impact.moija.domain.community.Comment;
import impact.moija.domain.community.Post;
import impact.moija.domain.user.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommentRequestDto {
    String content;
    boolean anonymous;

    public Comment toEntity(Long userId, Long postId) {
        return Comment.builder()
                .user(User.builder().id(userId).build())
                .post(Post.builder().id(postId).build())
                .anonymous(anonymous)
                .content(content)
                .build();
    }
}
