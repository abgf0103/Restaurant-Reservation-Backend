package kr.re.kh.service;

import kr.re.kh.exception.BadRequestException;
import kr.re.kh.mapper.FileMapMapper;
import kr.re.kh.mapper.ReviewMapper;
import kr.re.kh.mapper.UploadFileMapper;
import kr.re.kh.model.CustomUserDetails;
import kr.re.kh.model.User;
import kr.re.kh.model.payload.request.FileDeleteRequest;
import kr.re.kh.model.payload.response.UserResponse;
import kr.re.kh.model.vo.FileMap;
import kr.re.kh.model.vo.UploadFile;
import kr.re.kh.repository.UserRepository;
import kr.re.kh.util.UploadFileUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class UploadFileService {

    private final Path rootLocation;

    private final UploadFileMapper uploadFileMapper;

    private final FileMapMapper fileMapMapper;

    private  final UserRepository userRepository;

    public UploadFileService(String uploadPath, UploadFileMapper uploadFileMapper, FileMapMapper fileMapMapper, UserRepository userRepository) {
        this.rootLocation = Paths.get(uploadPath);
        this.uploadFileMapper = uploadFileMapper;
        this.fileMapMapper = fileMapMapper;
        this.userRepository = userRepository;
    }

    private Path loadPath(String fileName) {
        return rootLocation.resolve(fileName);
    }

    public UploadFile load(Long fileId) {
        return uploadFileMapper.selectFileById(fileId)
                .orElseThrow(() -> new BadRequestException("파일을 찾지 못했습니다. " + fileId));
    }

    public UploadFile loadAsSaveFileName(String saveFileName) {
        return uploadFileMapper.selectFileAsSaveFileName(saveFileName)
                .orElseThrow(() -> new BadRequestException("파일을 찾지 못했습니다. " + saveFileName));
    }

    /**
     * 파일 읽기
     * @param fileName
     * @return
     * @throws Exception
     */
    public Resource loadAsResource(String fileName) throws Exception {
        try {
            if (fileName.toCharArray()[0] == '/') fileName = fileName.substring(1);
            Path file = loadPath(fileName);
            log.info(file.toUri().toString());
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new Exception("파일을 찾지 못했습니다. " + fileName);
            }
        } catch (Exception e) {
            throw new Exception("파일을 찾지 못했습니다. " + fileName);
        }
    }

    /**
     * 파일 저장
     * @param files
     * @return
     * @throws Exception
     */
    public List<UploadFile> store(List<MultipartFile> files, String fileTarget, String username, CustomUserDetails currentUser) throws Exception {
        if (files.isEmpty()) throw new BadRequestException("저장할 파일이 없습니다.");

        List<UploadFile> uploadFileList = new ArrayList<>();
        for(MultipartFile file : files) {
            String saveFileName = UploadFileUtil.fileSave(rootLocation.toString(), file);
            log.info("saveFileName -{}", saveFileName);


            String[] saveFileNameArray = saveFileName.split("/");
            StringBuilder fileDirString = new StringBuilder();

            for (int i = 0; i < saveFileNameArray.length; i++) {
                if (i < saveFileNameArray.length - 1) {
                    fileDirString.append(saveFileNameArray[i]).append("/");
                }
            }


            if (saveFileName.toCharArray()[0] == '/') saveFileName = saveFileName.substring(1);

            Resource resource = loadAsResource(saveFileName);

            UploadFile saveFile = UploadFile.builder()
                    .fileName(file.getOriginalFilename())
                    .filePath(rootLocation.toString().replace(File.separatorChar, '/') + File.separator + saveFileName)
                    .contentType(file.getContentType())
                    .saveFileName(saveFileNameArray[saveFileNameArray.length - 1])
                    .fileDir(fileDirString.toString())
                    .fileSize(resource.contentLength())
                    .fileTarget(fileTarget)
                    .username(username)
                    .build();
            log.info(saveFile.toString());


            uploadFileMapper.insertFile(saveFile);

            // user 테이블에 file_id 컬럼 값을 넣는다.
            //  User user = new User();
            //  user.setFileId(saveFile.getId());
            // 토큰값에서 user_id 값 취득
            // currentUser.getId();
            // user.setId(currentUser.getId());
            // userRepository.save(user);

            // 파일이 프로필 이미지인 경우
            if (fileTarget.equals("profileImage")) {
                // user 테이블에 file_id 컬럼 값을 넣는다.
                Long currentUserId = currentUser.getId(); // currentUser 객체로부터 user_id 취득

                // User 객체 생성
                User user = userRepository.findById(currentUserId).orElse(null); // ID로 사용자 조회
                if (user != null) {
                    user.setFileId(saveFile.getId()); // file_id 저장

                    userRepository.save(user); // User 테이블 업데이트
                } else {
                    log.error("User not found with ID: {}", currentUserId);
                }
            }

            uploadFileList.add(saveFile);
        }
        return uploadFileList;
    }

    /**
     * 파일 삭제
     * @param fileDeleteRequest
     * @return
     */
    public boolean deleteAsResource(FileDeleteRequest fileDeleteRequest) {
        log.info(fileDeleteRequest.toString());
        UploadFile uploadFile = new UploadFile();
        uploadFile.setId(fileDeleteRequest.getId());
        if (fileDeleteRequest.getFileTarget() != null) uploadFile.setFileTarget(fileDeleteRequest.getFileTarget());

        log.info("8888");
        Optional<UploadFile> uploadFileVO = uploadFileMapper.selectFileByIdAndFileTarget(uploadFile);
        log.info("8ioioojioji");
        if (uploadFileVO.isPresent()) {
            File deleteFile = new File(uploadFileVO.get().getFilePath());
            if (deleteFile.exists()) deleteFile.delete();
            uploadFileMapper.deleteByFileByIdAndFileTarget(uploadFile);

            // filemap에도 삭제
            if (fileDeleteRequest.getReserveId() != null) {
                uploadFileMapper.deleteOneFiles(fileDeleteRequest.getId(), fileDeleteRequest.getReserveId());

            }

            // USER 테이블에서 fileId 값을 NULL로 설정
            if (fileDeleteRequest.getFileTarget().equals("profileImage")) {
                Long fileId = fileDeleteRequest.getId();  // 삭제할 파일의 ID

                log.info("파일 삭제 요청을 받은 fileId: {}", fileId);

                // USER 테이블에서 해당 fileId 값을 가진 사용자를 찾고, 해당 사용자의 fileId를 null로 설정
                User user = userRepository.findByFileId(fileId);  // fileId로 사용자 찾기

                if (user != null) {
                    // 파일 ID를 null로 설정
                    user.setFileId(null);
                    userRepository.save(user);  // 업데이트된 User 정보 저장
                    log.info("USER 테이블에서 fileId 값을 null로 설정 완료, userId: {}", user.getId());
                } else {
                    log.error("사용자를 찾을 수 없습니다. fileId: {}", fileId);
                }
            }


            log.info("리뷰 삭제 완료, reserveId({})를 기준으로 관련 파일 삭제됨", fileDeleteRequest.getReserveId());

            return true;
        } else {
            return false;
        }
    }

    public void save(FileMap fileMapVO) {
        fileMapMapper.insertFileMap(fileMapVO);
    }

    public List<UploadFile> selectFileByBoardId(Long id) {
        return uploadFileMapper.selectFileByBoardId(id);
    }


}
