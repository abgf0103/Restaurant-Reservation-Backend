package kr.re.kh.service.impl;

import kr.re.kh.mapper.ReservationMapper;
import kr.re.kh.model.vo.ReservationVO;
import kr.re.kh.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReservationServiceImpl implements ReservationService {

    private final ReservationMapper reservationMapper;

    @Autowired
    public ReservationServiceImpl(ReservationMapper reservationMapper) {
        this.reservationMapper = reservationMapper;
    }

    @Override
    public void createReservation(ReservationVO reservation) {
        reservationMapper.insertReservation(reservation);
    }

    @Override
    public Optional<ReservationVO> getReservationById(Long reserveId) {
        return reservationMapper.findReservationById(reserveId);
    }

    @Override
    public List<ReservationVO> getAllReservationsByUserId(Long userId) {
        return reservationMapper.findAllReservationsByUserId(userId);
    }

    @Override
    public List<ReservationVO> getAllReservationsByStoreId(Long storeId) {
        return reservationMapper.findAllReservationsByStoreId(storeId);
    }

    @Override
    public void updateReservation(ReservationVO reservation) {
        reservationMapper.updateReservation(reservation);
    }

    @Override
    public void updateReservationStatus(Long reserveId, String status) {
        reservationMapper.updateReservationStatus(reserveId, status);
    }

    @Override
    public void deleteReservation(Long reserveId) {
        reservationMapper.deleteReservation(reserveId);
    }

    @Override
    public boolean isReservationOwner(Long reserveId, Long userId) {
        Optional<ReservationVO> reservation = reservationMapper.findReservationById(reserveId);
        return reservation.map(res -> res.getUserId().equals(userId)).orElse(false);
    }

    /**
     * 예약 확정
     * @param reserveId
     */
    @Override
    public void confirmReservation(Long reserveId) {
        reservationMapper.confirmReservation(reserveId);
    }

    /**
     * 예약 취소
     * @param reserveId
     */
    @Override
    public void cancelReservation(Long reserveId) {
        reservationMapper.cancelReservation(reserveId);
    }

    @Override
    public void completeReservation(Long reserveId) {
        reservationMapper.completeReservation(reserveId);
    }
}
