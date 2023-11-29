package impact.moija.dto.common;

import java.util.List;
import lombok.Getter;
import org.springframework.data.domain.Page;

@Getter
public class PageResponse<T> {
    int currentPage;
    int totalPage;
    Long totalCount;
    List<T> itemList;

    public PageResponse(Page<T> data) {
        currentPage = data.getPageable().getPageNumber();
        totalPage = data.getTotalPages();
        totalCount = data.getTotalElements();
        itemList = data.getContent();
    }

    public static <T> PageResponse<T> of(Page<T> data) {
        return new PageResponse<>(data);
    }
}
