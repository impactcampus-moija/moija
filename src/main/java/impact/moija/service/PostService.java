package impact.moija.service;

import impact.moija.api.ApiException;
import impact.moija.api.MoijaHttpStatus;
import impact.moija.domain.community.Category;
import impact.moija.domain.community.Comment;
import impact.moija.domain.community.Post;
import impact.moija.domain.community.PostRecommendation;
import impact.moija.domain.user.User;
import impact.moija.dto.common.PageResponse;
import impact.moija.dto.common.PkResponseDto;
import impact.moija.dto.common.RecommendationResponseDto;
import impact.moija.dto.community.CommentRequestDto;
import impact.moija.dto.community.PostDetailResponseDto;
import impact.moija.dto.community.PostPageResponseDto;
import impact.moija.dto.community.PostRequestDto;
import impact.moija.repository.CategoryRepository;
import impact.moija.repository.CommentRepository;
import impact.moija.repository.PostRecommendationRepository;
import impact.moija.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class PostService {

    private final UserService userService;
    private final PostRepository postRepository;
    private final CategoryRepository categoryRepository;
    private final CommentRepository commentRepository;
    private final PostRecommendationRepository postRecommendationRepository;

    public PkResponseDto createPost(PostRequestDto postRequestDto) {
        Long loginMemberId = userService.getLoginMemberId();
        Category category = categoryRepository.findByName(postRequestDto.getCategory())
                .orElseThrow(() -> new ApiException(MoijaHttpStatus.INVALID_POST_CATEGORY));
        Post post = postRequestDto.toEntity(loginMemberId, category);

        postRepository.save(post);
        return PkResponseDto.of(post.getId());
    }

    @Transactional(readOnly = true)
    public PageResponse<PostPageResponseDto> getAllPosts(
            String categoryFilter, String keywordFilter, Pageable pageable
    ) {
        Category category = categoryRepository.findByName(categoryFilter).orElse(null);

        Page<Post> postsPage = postRepository.findAllByFilters(category, keywordFilter, pageable);

        return PageResponse.of(postsPage.map(PostPageResponseDto::of));
    }

    public PostDetailResponseDto getPostById(Long postId) {
        long loginUserId = userService.getLoginMemberId();
        Post post = postRepository.findById(postId).orElseThrow(() ->
                new ApiException(MoijaHttpStatus.NOT_FOUND_POST)
        );

        return PostDetailResponseDto.of(loginUserId, post);
    }

    public PkResponseDto updatePost(Long postId, PostRequestDto postRequestDto) {
        Post post = postRepository.findById(postId).orElseThrow(() ->
                new ApiException(MoijaHttpStatus.NOT_FOUND_POST)
        );

        if (isAuthorMismatch(post)) {
            throw new ApiException(MoijaHttpStatus.FORBIDDEN);
        }

        postRequestDto.updatePost(post);

        return PkResponseDto.of(post.getId());
    }

    public void deletePost(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() ->
                new ApiException(MoijaHttpStatus.NOT_FOUND_POST)
        );

        if (isAuthorMismatch(post)) {
            throw new ApiException(MoijaHttpStatus.FORBIDDEN);
        }

        postRepository.delete(post);
    }

    private boolean isAuthorMismatch(Post post) {
        return !Objects.equals(post.getUser().getId(), userService.getLoginMemberId());
    }

    public PkResponseDto createComment(Long postId, CommentRequestDto commentRequestDto) {
        Long loginMemberId = userService.getLoginMemberId();

        Comment comment = commentRequestDto.toEntity(loginMemberId, postId);
        commentRepository.save(comment);

        return PkResponseDto.of(comment.getId());
    }

    public void deleteComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ApiException(MoijaHttpStatus.NOT_FOUND_COMMENT));

        if (isAuthorMismatch(comment)) {
            throw new ApiException(MoijaHttpStatus.FORBIDDEN);
        }

        commentRepository.delete(comment);
    }

    private boolean isAuthorMismatch(Comment comment) {
        return !Objects.equals(comment.getUser().getId(), userService.getLoginMemberId());
    }

    public RecommendationResponseDto likePost(Long postId) {
        Long loginUserId = userService.getLoginMemberId();
        Post post = postRepository.findById(postId).orElseThrow(() ->
                new ApiException(MoijaHttpStatus.NOT_FOUND_POST)
        );

        Optional<PostRecommendation> postRecommendation = postRecommendationRepository.findByUserIdAndPost(loginUserId, post);
        if (postRecommendation.isPresent()) {
            unlikePost(postRecommendation.get());
            return RecommendationResponseDto.builder()
                    .recommendationCount(postRepository.countRecommendation(postId))
                    .hasRecommend(false)
                    .build();
        }

        likePost(loginUserId, post);
        return RecommendationResponseDto.builder()
                .recommendationCount(postRepository.countRecommendation(postId))
                .hasRecommend(true)
                .build();
    }

    private void likePost(long loginUserId, Post post) {
        PostRecommendation recommendation = PostRecommendation.builder()
                .user(User.builder().id(loginUserId).build())
                .post(post)
                .build();
        postRecommendationRepository.save(recommendation);
    }

    private void unlikePost(PostRecommendation postRecommendation) {
        postRecommendationRepository.delete(postRecommendation);
    }
}
