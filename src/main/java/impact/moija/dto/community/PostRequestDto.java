package impact.moija.dto.community;

import impact.moija.domain.community.Category;
import impact.moija.domain.community.Post;
import impact.moija.domain.user.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PostRequestDto {
    String title;
    String content;
    String category;
    boolean anonymous;

    public Post toEntity(long userId, Category category) {
        return Post.builder()
                .title(title)
                .content(content)
                .anonymous(anonymous)
                .user(User.builder().id(userId).build())
                .category(category)
                .build();
    }

    public void updatePost(Post post) {
        post.setTitle(title);
        post.setContent(content);
    }
}
