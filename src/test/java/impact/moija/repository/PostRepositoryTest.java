package impact.moija.repository;

import impact.moija.domain.community.Post;
import impact.moija.domain.community.PostRecommendation;
import impact.moija.domain.user.Gender;
import impact.moija.domain.user.User;
import impact.moija.domain.user.UserRole;
import impact.moija.repository.user.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class PostRepositoryTest {
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private PostRecommendationRepository postRecommendationRepository;
    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("카테고리와 키워드를 통해 게시물을 검색한다.")
    void findAllByFilters() {
        final User user = userRepository.save(createUser());
        final List<Post> posts = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            posts.add(createPost(String.valueOf(i), user));
        }
        postRepository.saveAll(posts);

        final Page<Post> postPage1 = postRepository.findAllByFilters(null, "1", PageRequest.of(0, 10));
        assertThat(postPage1.getTotalElements()).isEqualTo(1L);

        final Page<Post> postPage2 = postRepository.findAllByFilters(null, "", PageRequest.of(0, 10));
        assertThat(postPage2.getTotalElements()).isEqualTo(10L);
    }

    @Test
    @DisplayName("게시물 추천 횟수를 조회한다")
    void countRecommendation() {
        final User user = userRepository.save(createUser());
        final Post post = postRepository.save(createPost("title", user));
        postRecommendationRepository.save(postRecommendation(user, post));

        final long recommendationCount = postRepository.countRecommendation(post.getId());
        assertThat(recommendationCount).isEqualTo(1L);
    }

    private Post createPost(final String title, final User user) {
            return Post.builder()
                    .title(title)
                    .content("content")
                    .anonymous(false)
                    .user(user)
                    .build();
    }

    private User createUser() {
        return User.builder()
                .email("moiza@gmail.com")
                .password("expassword")
                .nickname("moiza")
                .birthday(LocalDate.of(1999, 2, 4))
                .gender(Gender.MALE)
                .roles(Collections.singleton(UserRole.ROLE_USER))
                .build();
    }

    private PostRecommendation postRecommendation(final User user, final Post post) {
        return PostRecommendation.builder()
                .post(post)
                .user(user)
                .build();
    }
}