package impact.moija.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import impact.moija.api.ApiException;
import impact.moija.api.BaseResponse;
import impact.moija.api.MoijaHttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
        BaseResponse<Void> baseResponse = BaseResponse.fail(new ApiException(MoijaHttpStatus.FORBIDDEN));

        response.setContentType("application/json;charset=UTF-8");
        String result = mapper.writeValueAsString(baseResponse);
        response.getWriter().write(result);
    }
}