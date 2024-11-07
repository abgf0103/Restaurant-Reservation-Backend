package kr.re.kh.service.impl;

import kr.re.kh.mapper.ReservationMapper;
import kr.re.kh.model.vo.ReservationVO;
import kr.re.kh.model.vo.StoreVO;
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
    public Optional<ReservationVO> getReservationById(Long reservationId) {
        return reservationMapper.findReservationById(reservationId);
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
    public void updateReservationStatus(Long reservationId, String status) {
        reservationMapper.updateReservationStatus(reservationId, status);
    }

    @Override
    public void deleteReservation(Long reservationId) {
        reservationMapper.deleteReservation(reservationId);
    }

    @Override
    public List<ReservationVO> selectAllReservation() {
        return reservationMapper.selectAllReservation();
    }

}
