package kr.re.kh.service;

import kr.re.kh.model.vo.ReservationVO;

import java.util.List;
import java.util.Optional;

public interface ReservationService {

    void createReservation(ReservationVO reservation);

    Optional<ReservationVO> getReservationById(Long reserved);

    List<ReservationVO> getAllReservationsByUserId(Long userId);

    List<ReservationVO> getAllReservationsByStoreId(Long storeId);

    void updateReservation(ReservationVO reservation);

    void updateReservationStatus(Long reserveId, String status);

    void deleteReservation(Long reserveId);

    boolean isReservationOwner(Long reserveId, Long userId); // 사용자 권한 확인 메서드
}
