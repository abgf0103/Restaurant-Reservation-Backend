package kr.re.kh.service;

import kr.re.kh.model.vo.ReservationVO;

import java.util.List;
import java.util.Optional;

public interface ReservationService {

    void createReservation(ReservationVO reservation);

    Optional<ReservationVO> getReservationById(Long reservationId);

    List<ReservationVO> getAllReservationsByUserId(Long userId);

    List<ReservationVO> getAllReservationsByStoreId(Long storeId);

    void updateReservation(ReservationVO reservation);

    void updateReservationStatus(Long reservationId, String status);

    void deleteReservation(Long reservationId);

    List<ReservationVO> selectAllReservation();
}
