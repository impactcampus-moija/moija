package impact.moija.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import impact.moija.api.ApiException;
import impact.moija.api.BaseResponse;
import impact.moija.api.MoijaHttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        BaseResponse<Void> baseResponse = BaseResponse.fail(new ApiException(MoijaHttpStatus.UNAUTHORIZED));

        response.setContentType("application/json;charset=UTF-8");
        String result = mapper.writeValueAsString(baseResponse);
        response.getWriter().write(result);
    }
}
