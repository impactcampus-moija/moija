package impact.moija.controller;

import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import com.google.gson.Gson;
import impact.moija.config.SecurityConfig;
import impact.moija.dto.mentoring.MentoringApplicationListResponseDto;
import impact.moija.jwt.JwtAccessDeniedHandler;
import impact.moija.jwt.JwtAuthenticationEntryPoint;
import impact.moija.jwt.TokenProvider;
import impact.moija.mock.WithMockCustomUser;
import impact.moija.service.MentoringApplicationService;
import impact.moija.service.UserService;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@Import({SecurityConfig.class, TokenProvider.class, JwtAccessDeniedHandler.class, JwtAuthenticationEntryPoint.class })
@WebMvcTest(UserController.class)
public class UserControllerTest {

    @MockBean
    private MentoringApplicationService applicationService;
    @MockBean
    private UserService userService;
    @Autowired
    private MockMvc mockMvc;
    private Gson gson;

    @BeforeEach
    public void init() {
        gson = new Gson();
    }

    @Test
    @WithMockCustomUser(email = "mentor@google.com", password = "password", roles = {"ROLE_USER", "ROLE_INDEPENDENCE"})
    public void 나의멘토링지원서목록조회성공() throws Exception {
        // given
        final String url = "/api/my/mentoring/applications";

        doReturn(List.of(
                MentoringApplicationListResponseDto.builder().build(),
                MentoringApplicationListResponseDto.builder().build(),
                MentoringApplicationListResponseDto.builder().build()
        ))
                .when(applicationService)
                .getMyMentoringApplications();

        // when
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.request(HttpMethod.GET, url)
        );

        // then
        resultActions.andDo(print())
                .andExpect(jsonPath("$.statusCode").value(200));
    }
}