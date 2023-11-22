package impact.moija.api;

import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@RestControllerAdvice
public class TransformStatusBodyAdvice<T> implements ResponseBodyAdvice<BaseResponse<T>> {
    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return returnType.getParameterType().isAssignableFrom(BaseResponse.class);
    }

    @Override
    public BaseResponse<T> beforeBodyWrite(BaseResponse<T> body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        if (body != null) {
            response.setStatusCode(body.getStatus().getHttpStatus());
        }
        return body;
    }
}
