package impact.moija.service;

import impact.moija.api.ApiException;
import impact.moija.api.MoijaHttpStatus;
import impact.moija.domain.common.Image;
import impact.moija.dto.common.ImageResponseDto;
import impact.moija.repository.ImageRepository;
import impact.moija.s3.S3Uploader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Service
public class ImageService {

    private final ImageRepository imageRepository;
    private final S3Uploader uploader;

    private Image findImage(String table, Long id) {
        return imageRepository.findByTargetTableAndTargetId(table, id)
                .orElse(new Image());
    }

    @Transactional
    public void createImage(String table, Long id, MultipartFile file) {
        String url = uploader.uploadFile(file);

        imageRepository.save(
                Image.builder()
                        .targetTable(table)
                        .targetId(id)
                        .name(file.getOriginalFilename())
                        .url(url)
                        .build()
        );
    }

    @Transactional
    public ImageResponseDto getImage(String table, Long id) {
        return ImageResponseDto.of(findImage(table, id));
    }

    @Transactional
    public void deleteImage(String table, Long id) {
        Image image = findImage(table, id);
        uploader.deleteFile(image.getUrl());
        imageRepository.delete(image);
    }
}
