package impact.moija.api;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@Builder(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BaseResponse<T> {
    @JsonIgnore
    MoijaHttpStatus status;

    int statusCode;
    String message;
    T data;

    public static BaseResponse<Void> ok() {
        return BaseResponse.<Void>builder()
                .status(MoijaHttpStatus.OK)
                .build();
    }

    public static <T> BaseResponse<T> ok(T data) {
        return BaseResponse.<T>builder()
                .status(MoijaHttpStatus.OK)
                .data(data)
                .build();
    }

    public static <T> BaseResponse<T> created(T data) {
        return BaseResponse.<T>builder()
                .status(MoijaHttpStatus.CREATED)
                .data(data)
                .build();
    }

    public static BaseResponse<Void> fail(ApiException e) {
        return BaseResponse.<Void>builder()
                .status(e.getStatus())
                .build();
    }

    private static class CustomBaseResponseBuilder<T> extends BaseResponseBuilder<T> {
        @Override
        public BaseResponse<T> build() {
            BaseResponse<T> baseResponse = super.build();

            MoijaHttpStatus status = baseResponse.status;
            baseResponse.statusCode = status.getStatusCode();
            baseResponse.message = status.getMessage();
            return baseResponse;
        }
    }
}