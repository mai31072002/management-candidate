package com.candidate.candidate_backend.service;

import com.candidate.candidate_backend.dto.CommonsRep;
import com.candidate.candidate_backend.dto.PageDataRep;
import com.candidate.candidate_backend.dto.otday.DtoOtDayRep;
import com.candidate.candidate_backend.dto.otday.DtoOtDayReq;
import com.candidate.candidate_backend.entity.DtbOtDate;
import com.candidate.candidate_backend.entity.DtbTimeKeeping;
import com.candidate.candidate_backend.entity.DtbUser;
import com.candidate.candidate_backend.exception.BusinessException;
import com.candidate.candidate_backend.mapper.OtDayMapper;
import com.candidate.candidate_backend.repositorry.OtDayRepository;
import com.candidate.candidate_backend.repositorry.TimeKeepingRepository;
import com.candidate.candidate_backend.repositorry.UserRepository;
import com.candidate.candidate_backend.util.Helper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@Slf4j
public class OtDayService {

    @Autowired
    private OtDayRepository otDayRepository;

    @Autowired
    private TimeKeepingRepository timeKeepingRepository;

    @Autowired
    private UserRepository userRepository;

    // CREATE
    public CommonsRep createOtDay(DtoOtDayReq req) {
        try {
//            DtbTimeKeeping timeKeeping = timeKeepingRepository
//                    .findByIdAndIsDeletedFalse(req.getTimeKeepingId())
//                    .orElseThrow(() ->
//                            new BusinessException(HttpStatus.NOT_FOUND, "TimeKeeping not found"));

            // kiểu tra có trùng ot nào không
            // start_time < end_time_new và end_time > start_time_new
            boolean isOverlap = otDayRepository.existsOverlapOt(
                req.getUserId(),
                req.getWorkDate(),
                req.getStartTime(),
                req.getEndTime()
            );

            if (isOverlap) {
                return Helper.getServerResponse(
                        HttpStatus.BAD_GATEWAY,
                        "Khoảng thời gian OT đã tồn tại, vui lòng cập nhật OT cũ",
                        null);
            }

            DtbUser dtbUser = userRepository.findById(req.getUserId())
                    .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "Nhân viên Không tồn tại"));

            DtbOtDate otDay = OtDayMapper.toEntity(req, dtbUser);

            DtbOtDate save = otDayRepository.save(otDay);

            DtoOtDayRep dtoOtDayRep = OtDayMapper.toDto(save);

            return Helper.getServerResponse(
                    HttpStatus.OK,
                    "Tạo OT thành công",
                    dtoOtDayRep
            );
        } catch (Exception e) {
            log.error("Create OT error", e);
            return Helper.getServerResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Lỗi hệ thống", null);
        }
    }

    // GET
    public CommonsRep getOtDay(int page, int size, LocalDate fromDate, LocalDate toDate, Integer status) {
        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by("updateAt").descending()
        );

//            Page<DtbOtDate> dtbOtDates;
//
//            if (fromDate != null && toDate != null) {
//                // 🔥 Có cả from & to
//                dtbOtDates = otDayRepository
//                    .findOtWithEmployee(
//                            fromDate,
//                            toDate,
//                            pageable
//                    );
//            } else {
//                // 🔥 Không truyền ngày → lấy tất cả
//                dtbOtDates = otDayRepository
//                        .findAllByIsDeletedFalse(pageable);
//            }

        Page<DtbOtDate> dtbOtDates = otDayRepository.findOtWithUser(
                status,
                fromDate,
                toDate,
                pageable
        );

        PageDataRep<DtoOtDayRep> pageData = new PageDataRep<>();

        pageData.setData(
                dtbOtDates.getContent()
                        .stream()
                        .map(OtDayMapper::toDto)
                        .toList()
        );

        pageData.setPageableRep(Helper.mapToPageableRep(dtbOtDates));

        return Helper.getServerResponse(HttpStatus.OK, "Lấy tất cả danh sách ot thành công", pageData);
    }

    public CommonsRep getOtDayByUserId(String userId, YearMonth month) {
//        try {
        UUID uuidUserId = UUID.fromString(userId);

        LocalDate startDate = month.atDay(1);
        LocalDate endDate = month.atEndOfMonth();

        List<DtbOtDate> dtbOtDates = otDayRepository.findOtByUserAndMonth(uuidUserId, startDate, endDate);

        List<DtoOtDayRep> dtoOtDayRep = dtbOtDates
                .stream()
                .map(OtDayMapper::toDto)
                .toList();

        return Helper.getServerResponse(HttpStatus.OK, "Lấy ra danh sách ot theo nhân viên thành công",
                dtoOtDayRep);
//        } catch (Exception e) {
//            return Helper.getServerResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Lỗi hệ thống", null);
//        }
    }

    // UPDATE
    public CommonsRep updateOtDay(String otId, DtoOtDayReq dtoOtDayReq) {
//        try {
            UUID uuidOtId = UUID.fromString(otId);

            DtbOtDate dtbOtDate = otDayRepository.findById(uuidOtId)
                    .orElseThrow(() ->
                            new BusinessException(HttpStatus.NOT_FOUND, "OT not found"));

            if (!Objects.equals(
                    dtbOtDate.getUser().getUserId(),
                    dtoOtDayReq.getUserId())) {
                return Helper.getServerResponse(
                        HttpStatus.BAD_REQUEST,
                        "Không được thay đổi nhân viên chấm công của OT",
                        null
                );
            }


            if (!hasChangeOtDay(dtbOtDate, dtoOtDayReq)) {
                return Helper.getServerResponse(HttpStatus.OK, "Không có sự thay đổi", null);
            }

            DtoOtDayRep dtoOtDayRep = OtDayMapper.toDto(otDayRepository.save(dtbOtDate));

            return Helper.getServerResponse(
                    HttpStatus.OK,
                    "Cập nhật OT thành công",
                    dtoOtDayRep
            );
//        } catch (Exception e) {
//            log.error("Update OT error", e);
//            return Helper.getServerResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Lỗi hệ thống", null);
//        }
    }

    // DELETE (soft delete)
    public CommonsRep deleteOtDay(String otId) {
//        try {
            UUID uuidOtId = UUID.fromString(otId);

            DtbOtDate otDay = otDayRepository.findById(uuidOtId)
                    .orElseThrow(() ->
                            new BusinessException(HttpStatus.NOT_FOUND, "OT not found"));

            otDay.setDeleted(true);
            otDayRepository.save(otDay);

            return Helper.getServerResponse(HttpStatus.OK, "Xóa OT thành công", null);
//        } catch (Exception e) {
//            log.error("Delete OT error", e);
//            return Helper.getServerResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Lỗi hệ thống", null);
//        }
    }

    private boolean hasChangeOtDay(DtbOtDate dtbOtDate, DtoOtDayReq dtoOtDayReq) {
        return !Objects.equals(dtbOtDate.getOtType(), dtoOtDayReq.getOtType()) ||
                !Objects.equals(dtbOtDate.getOtMinutes(), dtoOtDayReq.getOtMinutes()) ||
                !Objects.equals(dtbOtDate.getStartTime(), dtoOtDayReq.getStartTime()) ||
                !Objects.equals(dtbOtDate.getEndTime(), dtoOtDayReq.getEndTime()) ||
                !Objects.equals(dtbOtDate.getJobTitle(), dtoOtDayReq.getJobTitle()) ||
                !Objects.equals(dtbOtDate.getOtRate(), dtoOtDayReq.getOtRate());
    }
}

