package impact.moija.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.google.gson.Gson;
import impact.moija.api.ApiException;
import impact.moija.api.BaseResponse;
import impact.moija.api.MoijaHttpStatus;
import impact.moija.config.SecurityConfig;
import impact.moija.domain.mentoring.MentoringRecruitment;
import impact.moija.dto.common.PageResponse;
import impact.moija.dto.common.PkResponseDto;
import impact.moija.dto.mentoring.MentoringRecruitmentDetailResponseDto;
import impact.moija.dto.mentoring.MentoringRecruitmentListResponseDto;
import impact.moija.dto.mentoring.MentoringRecruitmentRequestDto;
import impact.moija.jwt.JwtAccessDeniedHandler;
import impact.moija.jwt.JwtAuthenticationEntryPoint;
import impact.moija.jwt.TokenProvider;
import impact.moija.mock.WithMockCustomUser;
import impact.moija.service.MentoringRecruitmentService;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpMethod;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@Import({SecurityConfig.class, TokenProvider.class, JwtAccessDeniedHandler.class, JwtAuthenticationEntryPoint.class })
@WebMvcTest(MentoringRecruitmentController.class)
public class MentoringRecruitmentControllerTest {

    @MockBean
    private MentoringRecruitmentService service;
    @Autowired
    private MockMvc mockMvc;
    private Gson gson;
    private MockMultipartFile image;

    @BeforeEach
    public void init() {
        gson = new Gson();
        image = new MockMultipartFile("image",
                "image.jpg",
                "multipart/form-data",
                "image".getBytes(StandardCharsets.UTF_8)
        );
    }

    @ParameterizedTest
    @MethodSource("invalidMentoringRecruitmentParameter")
    @WithMockCustomUser(email = "mentor@google.com", password = "password", roles = {"ROLE_USER", "ROLE_MENTOR"})
    public void 멘토링모집서등록실패_잘못된파라미터(MentoringRecruitmentRequestDto requestDto) throws Exception {
        // given
        final String url = "/api/mentoring/recruitment";

        MockMultipartFile recruitment = new MockMultipartFile("recruitment",
                null,
                "application/json",
                gson.toJson(requestDto).getBytes(StandardCharsets.UTF_8));

        // when
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.multipart(HttpMethod.POST, url)
                        .file(image)
                        .file(recruitment)
        );

        // then
        resultActions.andDo(print())
                .andExpect(status().isBadRequest());
    }
    private static Stream<Arguments> invalidMentoringRecruitmentParameter() {
        return Stream.of(
                Arguments.of(MentoringRecruitmentRequestDto.builder()
                        .category(null)
                        .name("멘토1")
                        .brief("한줄 소개1")
                        .career("경력1")
                        .introduction("멘토링 소개1")
                        .occupation("직업")
                        .build()),
                Arguments.of(MentoringRecruitmentRequestDto.builder()
                        .category("주거, ,일자리")
                        .name("멘토1")
                        .brief("한줄 소개1")
                        .career("경력1")
                        .introduction("멘토링 소개1")
                        .occupation("직업")
                        .build()),
                Arguments.of(MentoringRecruitmentRequestDto.builder()
                        .category("주너,일자리")
                        .name("멘토1")
                        .brief("한줄 소개1")
                        .career("경력1")
                        .introduction("멘토링 소개1")
                        .occupation("직업")
                        .build())
        );
    }

    @Test
    @WithMockCustomUser(email = "mentor@google.com", password = "password", roles = {"ROLE_USER", "ROLE_MENTOR"})
    public void 멘토링모집서등록성공() throws Exception {
        // given
        final String url = "/api/mentoring/recruitment";

        MockMultipartFile recruitment = new MockMultipartFile("recruitment",
                null,
                "application/json",
                gson.toJson(recruitment()).getBytes(StandardCharsets.UTF_8));

        doReturn(PkResponseDto.of(1L))
                .when(service)
                .createMentoringRecruitment(any(MentoringRecruitmentRequestDto.class), any(MockMultipartFile.class));

        // when
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.multipart(HttpMethod.POST, url)
                        .file(image)
                        .file(recruitment)
        );

        // then
        resultActions.andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    @WithAnonymousUser
    public void 모집서목록조회실패_미인증사용자접근() throws Exception {
        // given
        final String url = "/api/mentoring/recruitments";
        // when
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.request(HttpMethod.GET, url)
        );
        // then
        resultActions.andDo(print())
                .andExpect(jsonPath("$.statusCode").value(401));
    }

    @Test
    @WithMockCustomUser(email = "mentor@google.com", password = "password")
    public void 멘토링모집서목록조회성공() throws Exception {
        // given
        final String url = "/api/mentoring/recruitments";
        Pageable pageable = PageRequest.of(0, 8);
        doReturn(PageResponse.of(
                new PageImpl<>(List.of(
                        MentoringRecruitmentListResponseDto.builder().category("알자리").brief("도움1").build(),
                        MentoringRecruitmentListResponseDto.builder().category("알자리").brief("도움2").build(),
                        MentoringRecruitmentListResponseDto.builder().category("알자리").brief("도움3").build()
                )
            ,pageable, 3)
        )).when(service).getMentoringRecruitments(pageable, "일자리", "도움");

        // when
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.request(HttpMethod.GET, url)
                        .param("category", "일자리")
                        .param("keyword", "도움")
        );
        // then
        resultActions.andDo(print());
        // TODO : Body에 data 안담기는 문제 해결해야 함...
    }

    @Test
    @WithMockCustomUser(email = "mentor@google.com", password = "password")
    public void 멘토링모집서상세조회실패_모집서없음() throws Exception {
        // given
        final String url = "/api/mentoring/recruitments/1";
        doThrow(new ApiException(MoijaHttpStatus.NOT_FOUND_MENTORING_RECRUITMENT))
                .when(service)
                .getMentoringRecruitment(anyLong());
        // when
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.request(HttpMethod.GET, url)
        );
        // then
        resultActions.andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockCustomUser(email = "mentor@google.com", password = "password")
    public void 멘토링모집서상세조회성공() throws Exception {
        // given
        final String url = "/api/mentoring/recruitments/1";
        doReturn(MentoringRecruitmentDetailResponseDto.builder().build())
                .when(service)
                .getMentoringRecruitment(anyLong());
        // when
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.request(HttpMethod.GET, url)
        );

        // then
        resultActions
                .andDo(print())
                .andExpect(jsonPath("$.statusCode").value("200"));
    }

    @Test
    @WithMockCustomUser(email = "mentor@google.com", password = "password", roles = {"ROLE_USER", "ROLE_MENTOR"})
    public void 멘토링모집서수정성공() throws Exception {
        // given
        final String url = "/api/mentoring/recruitments/1";

        MockMultipartFile recruitment = new MockMultipartFile("recruitment",
                null,
                "application/json",
                gson.toJson(recruitment()).getBytes(StandardCharsets.UTF_8));

        doReturn(PkResponseDto.of(1L))
                .when(service)
                .updateMentoringRecruitment(
                        any(MentoringRecruitmentRequestDto.class),
                        any(MockMultipartFile.class),
                        anyLong()
                );

        // when
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.multipart(HttpMethod.PATCH, url)
                        .file(recruitment)
                        .file(image)
        );

        // then
        resultActions.andDo(print())
                .andExpect(jsonPath("$.statusCode").value("200"));
    }
    private MentoringRecruitmentRequestDto recruitment() {
        return MentoringRecruitmentRequestDto.builder()
                .category("주거,일자리")
                .name("멘토1")
                .brief("한줄 소개1")
                .career("경력1")
                .introduction("멘토링 소개1")
                .occupation("직업")
                .build();
    }

    @Test
    @WithMockCustomUser(email = "mentor@google.com", password = "password", roles = {"ROLE_USER", "ROLE_MENTOR"})
    public void 멘토링모집서활성화설정성공() throws Exception {
        // given
        final String url = "/api/mentoring/recruitments/1/activate";
        // when
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.request(HttpMethod.PATCH, url)
        );
        // then
        resultActions.andDo(print())
                .andExpect(jsonPath("$.statusCode").value("200"));
    }


    @Test
    @WithMockCustomUser(email = "mentor@google.com", password = "password", roles = {"ROLE_USER", "ROLE_MENTOR"})
    public void 멘토링모집서삭제성공() throws Exception {
        // given
        final String url = "/api/mentoring/recruitments/1";
        // when
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.request(HttpMethod.DELETE, url)
        );
        // then
        resultActions.andDo(print())
                .andExpect(jsonPath("$.statusCode").value("200"));
    }
}
