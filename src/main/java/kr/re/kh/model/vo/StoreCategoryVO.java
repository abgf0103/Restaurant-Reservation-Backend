package kr.re.kh.model.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity(name = "STORE_CATEGORY")
public class StoreCategoryVO {
    @Id
    @Column(name = "STORE_CATEGORY_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long storeCategoryId;

    @Column(name = "STORE_ID", length = 19)
    private Long storeId;

    @Column(name = "CATEGORY_ID", length = 19)
    private Long categoryId;

    public StoreCategoryVO(StoreCategoryVO storeCategoryVO) {
        storeCategoryId = storeCategoryVO.getStoreCategoryId();
        storeId = storeCategoryVO.getStoreId();
        categoryId = storeCategoryVO.getCategoryId();
    }
}
