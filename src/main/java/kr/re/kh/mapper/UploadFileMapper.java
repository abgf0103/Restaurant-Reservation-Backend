package kr.re.kh.mapper;

import kr.re.kh.model.vo.UploadFile;
import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Mapper
public interface UploadFileMapper {

    Optional<UploadFile> selectFileById(Long id);

    Optional<UploadFile> selectFileByIdAndFileTarget(UploadFile uploadFile);

    Optional<UploadFile> selectFileAsSaveFileName(String saveFileName);

    void insertFile(UploadFile uploadFile);

    void deleteByFileByIdAndFileTarget(UploadFile uploadFile);

    List<UploadFile> selectFileByBoardId(Long id);

    void deleteOneFiles(Long id, Long reserveId);

}
