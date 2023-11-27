package impact.moija.dto.common;

import impact.moija.domain.common.Image;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ImageResponseDto {
    private Long id;
    private String url;
    private String name;

    public static ImageResponseDto of(Image image) {
        return ImageResponseDto.builder()
                .id(image.getId())
                .url(image.getUrl())
                .name(image.getName())
                .build();
    }

}
