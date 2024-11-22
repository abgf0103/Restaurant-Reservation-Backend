package kr.re.kh.model.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class FavoriteVO {
    private Long favoriteId;            //고유 ID
    private Long userId;                //유저 ID
    private Long storeId;               //가게 ID
    private LocalDateTime createdAt;    //등록 시간
    private LocalDateTime updatedAt;    //수정 시간
}
