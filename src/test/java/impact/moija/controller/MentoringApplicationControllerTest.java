package impact.moija.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.google.gson.Gson;
import impact.moija.api.ApiException;
import impact.moija.api.MoijaHttpStatus;
import impact.moija.config.SecurityConfig;
import impact.moija.domain.mentoring.MentoringStatus;
import impact.moija.dto.common.PkResponseDto;
import impact.moija.dto.mentoring.MentoringApplicationDetailResponseDto;
import impact.moija.dto.mentoring.MentoringApplicationListResponseDto;
import impact.moija.dto.mentoring.MentoringApplicationRequestDto;
import impact.moija.dto.mentoring.MentoringApplicationHandlingRequestDto;
import impact.moija.jwt.JwtAccessDeniedHandler;
import impact.moija.jwt.JwtAuthenticationEntryPoint;
import impact.moija.jwt.TokenProvider;
import impact.moija.mock.WithMockCustomUser;
import impact.moija.service.MentoringApplicationService;
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
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@Import({SecurityConfig.class, TokenProvider.class, JwtAccessDeniedHandler.class, JwtAuthenticationEntryPoint.class })
@WebMvcTest(MentoringApplicationController.class)
public class MentoringApplicationControllerTest {

    @MockBean
    private MentoringApplicationService applicationService;
    @Autowired
    private MockMvc mockMvc;
    private Gson gson;

    @BeforeEach
    public void init() {
        gson = new Gson();
    }

    @ParameterizedTest
    @MethodSource("invalidMentoringApplicationParameter")
    @WithMockCustomUser(email = "mentor@google.com", password = "password", roles = {"ROLE_USER", "ROLE_MENTOR"})
    public void 멘토링지원서등록실패_잘못된파라미터(MentoringApplicationRequestDto invalid) throws Exception {
        // given
        final String url = "/api/mentoring/recruitments/1/application";

        // when
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.request(HttpMethod.POST, url)
                        .content(gson.toJson(invalid))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        resultActions.andDo(print())
                .andExpect(status().isBadRequest());
    }

    private static Stream<Arguments> invalidMentoringApplicationParameter() {
        return Stream.of(
                Arguments.of(MentoringApplicationRequestDto.builder()
                        .phone("0101234123")
                        .topic("토픽1")
                        .content("내용1")
                        .build()),
                Arguments.of(MentoringApplicationRequestDto.builder()
                        .phone("010123412345")
                        .topic("토픽1")
                        .content("내용1")
                        .build()),
                Arguments.of(MentoringApplicationRequestDto.builder()
                        .phone("01012341234")
                        .topic(null)
                        .content("내용1")
                        .build()),
                Arguments.of(MentoringApplicationRequestDto.builder()
                        .phone("01012341234")
                        .topic("토픽1")
                        .content(null)
                        .build())
        );
    }

    @Test
    @WithMockCustomUser(email = "mentor@google.com", password = "password", roles = {"ROLE_USER", "ROLE_INDEPENDENCE"})
    public void 멘토링지원서등록성공() throws Exception {
        // given
        final String url = "/api/mentoring/recruitments/1/application";

        doReturn(PkResponseDto.of(1L))
                .when(applicationService)
                .createMentoringApplication(anyLong(), any(MentoringApplicationRequestDto.class));

        // when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.request(HttpMethod.POST, url)
                        .content(gson.toJson(createDto()))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        resultActions.andDo(print())
                .andExpect(jsonPath("$.statusCode").value(201));
    }

    @Test
    @WithMockCustomUser(email = "m@google.com", password = "password", roles = {"ROLE_USER", "ROLE_MENTOR"})
    public void 대기중인멘토링지원서목록조회실패_모집서가없음() throws Exception {
        // given
        final String url = "/api/mentoring/recruitments/-1/applications";
        doThrow(new ApiException(MoijaHttpStatus.NOT_FOUND_MENTORING_RECRUITMENT))
                .when(applicationService)
                .getPendingMentoringApplications(anyLong());
        // when
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.request(HttpMethod.GET, url)
        );

        // then
        resultActions.andDo(print())
                .andExpect(jsonPath("$.statusCode").value("40407"));
    }

    @Test
    @WithMockCustomUser(email = "m@google.com", password = "password", roles = {"ROLE_USER", "ROLE_MENTOR","ROLE_INDEPENDENCE"})
    public void 대기중인멘토링지원서목록조회성공() throws Exception {
        // given
        final String url = "/api/mentoring/recruitments/1/applications";

        doReturn(List.of(
                MentoringApplicationListResponseDto.builder().id(1L).build(),
                MentoringApplicationListResponseDto.builder().id(2L).build(),
                MentoringApplicationListResponseDto.builder().id(3L).build()
        ))
                .when(applicationService)
                .getPendingMentoringApplications(anyLong());

        // when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.request(HttpMethod.GET, url)
        );

        // then
        resultActions.andDo(print())
                .andExpect(jsonPath("$.statusCode").value("200"));
    }

    private MentoringApplicationRequestDto createDto() {
        return MentoringApplicationRequestDto
                .builder()
                .phone("01012345678")
                .topic("토픽")
                .content("내용")
                .build();
    }

    private MentoringApplicationRequestDto updateDto() {
        return MentoringApplicationRequestDto
                .builder()
                .phone("01098765432")
                .topic("토픽1")
                .content("내용1")
                .build();
    }

    @Test
    @WithMockCustomUser(email = "m@google.com", password = "password", roles = {"ROLE_USER", "ROLE_MENTOR", "ROLE_INDEPENDENCE"})
    public void 멘토링지원서상세조회실패_지원서없음() throws Exception {
        // given
        final String url = "/api/mentoring/recruitments/1/applications/1";
        doThrow(new ApiException(MoijaHttpStatus.NOT_FOUND_MENTORING_APPLICATION))
                .when(applicationService)
                .getMentoringApplication(anyLong(), anyLong());

        // when
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.request(HttpMethod.GET, url)
        );

        // then
        resultActions.andDo(print())
                .andExpect(jsonPath("$.statusCode").value("40408"));
    }

    @Test
    @WithMockCustomUser(email = "m@google.com", password = "password", roles = {"ROLE_USER", "ROLE_MENTOR", "ROLE_INDEPENDENCE"})
    public void 멘토링지원서상세조회실패_권한없음() throws Exception {
        // given
        final String url = "/api/mentoring/recruitments/1/applications/1";
        doThrow(new ApiException(MoijaHttpStatus.FORBIDDEN))
                .when(applicationService)
                .getMentoringApplication(anyLong(), anyLong());

        // when
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.request(HttpMethod.GET, url)
        );

        // then
        resultActions.andDo(print())
                .andExpect(jsonPath("$.statusCode").value("403"));
    }

    @Test
    @WithMockCustomUser(email = "m@google.com", password = "password", roles = {"ROLE_USER", "ROLE_MENTOR", "ROLE_INDEPENDENCE"})
    public void 멘토링지원서상세조회성공() throws Exception {
        // given
        final String url = "/api/mentoring/recruitments/1/applications/1";
        doReturn(MentoringApplicationDetailResponseDto.builder().build())
                .when(applicationService)
                .getMentoringApplication(anyLong(), anyLong());

        // when
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.request(HttpMethod.GET, url)
        );

        // then
        resultActions.andDo(print())
                .andExpect(jsonPath("$.statusCode").value("200"));
    }

    @Test
    @WithMockCustomUser(email = "m@google.com", password = "password", roles = {"ROLE_USER", "ROLE_INDEPENDENCE"})
    public void 멘토링지원서수정성공() throws Exception {
        // given
        final String url = "/api/mentoring/recruitments/1/applications/1";
        doReturn(PkResponseDto.of(1L))
                .when(applicationService)
                .updateMentoringApplication(anyLong(), any(MentoringApplicationRequestDto.class));

        // when
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.request(HttpMethod.PUT, url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(updateDto()))
        );

        // then
        resultActions.andDo(print())
                .andExpect(jsonPath("$.statusCode").value("200"));
    }

    @Test
    @WithMockCustomUser(email = "mentor@google.com", password = "password", roles = {"ROLE_USER", "ROLE_INDEPENDENCE"})
    public void 멘토링모집서삭제성공() throws Exception {
        // given
        final String url = "/api/mentoring/recruitments/1/applications/1";
        // when
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.request(HttpMethod.DELETE, url)
        );
        // then
        resultActions.andDo(print())
                .andExpect(jsonPath("$.statusCode").value("200"));
    }

    @Test
    @WithMockCustomUser(email = "mentor@google.com", password = "password", roles = {"ROLE_USER", "ROLE_MENTOR"})
    public void 멘토링모집서처리실패_거절이유없음() throws Exception {
        // given
        final String url = "/api/mentoring/recruitments/1/applications/1/handling";
        final MentoringApplicationHandlingRequestDto handling = MentoringApplicationHandlingRequestDto.builder()
                .status(MentoringStatus.REFUSE)
                .build();
        doThrow(new ApiException(MoijaHttpStatus.INVALID_MENTORING_REASON))
                .when(applicationService)
                .handleMentoringApplication(anyLong(), anyLong(), any(MentoringApplicationHandlingRequestDto.class));

        // when
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.request(HttpMethod.PATCH, url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(handling))
        );

        // then
        resultActions.andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockCustomUser(email = "mentor@google.com", password = "password", roles = {"ROLE_USER", "ROLE_MENTOR"})
    public void 멘토링모집서처리실패_멘토링상태없음() throws Exception {
        // given
        final String url = "/api/mentoring/recruitments/1/applications/1/handling";
        final MentoringApplicationHandlingRequestDto handling = MentoringApplicationHandlingRequestDto.builder()
                .status(MentoringStatus.PENDING)
                .build();

        // when
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.request(HttpMethod.PATCH, url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(handling))
        );

        // then
        resultActions.andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockCustomUser(email = "mentor@google.com", password = "password", roles = {"ROLE_USER", "ROLE_MENTOR"})
    public void 멘토링모집서거절성공() throws Exception {
        // given
        final String url = "/api/mentoring/recruitments/1/applications/1/handling";

        final MentoringApplicationHandlingRequestDto handling = MentoringApplicationHandlingRequestDto.builder()
                .status(MentoringStatus.REFUSE)
                .reason("이러이러한 이유로 거절합니다")
                .build();

        doReturn(PkResponseDto.of(1L))
                .when(applicationService)
                .handleMentoringApplication(anyLong(), anyLong(), any(MentoringApplicationHandlingRequestDto.class));

        // when
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.request(HttpMethod.PATCH, url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(handling))
        );

        // then
        resultActions.andDo(print())
                .andExpect(jsonPath("$.statusCode").value("200"));
    }
}
