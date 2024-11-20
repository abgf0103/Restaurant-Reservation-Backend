package kr.re.kh.model.vo;

import kr.re.kh.util.AppConstants;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class SearchHelper {

    private String searchCode;
    private String searchKeyword;
    private String searchType;
    private int size = Integer.parseInt(AppConstants.DEFAULT_PAGE_SIZE);
    private int page = Integer.parseInt(AppConstants.DEFAULT_PAGE_NUMBER);
    private Long storeId;
    private Long userId;
    private Long reserveId;
    private String username;

    @Builder
    public SearchHelper(String searchCode, String searchKeyword, String searchType, int size, int page, Long storeId, Long userId, Long reserveId, String username) {
        this.searchCode = searchCode;
        this.searchKeyword = searchKeyword;
        this.searchType = searchType;
        this.size = size;
        this.page = page;
        this.storeId = storeId;
        this.userId = userId;
        this.reserveId = reserveId;
        this.username = username;
    }

}
