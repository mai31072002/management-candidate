package com.candidate.candidate_backend.service;

import com.candidate.candidate_backend.dto.CommonsRep;
import com.candidate.candidate_backend.dto.PageDataRep;
import com.candidate.candidate_backend.dto.otday.DtoOtDayRep;
import com.candidate.candidate_backend.dto.timeKeeping.DtoTimeKeepingRep;
import com.candidate.candidate_backend.dto.timeKeeping.DtoTimeKeepingReq;
import com.candidate.candidate_backend.entity.DtbTimeKeeping;
import com.candidate.candidate_backend.exception.BusinessException;
import com.candidate.candidate_backend.mapper.TimeKeepingMapper;
import com.candidate.candidate_backend.repositorry.EmployeeRepository;
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
public class TimeKeepingService {
    @Autowired
    private TimeKeepingRepository timeKeepingRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private UserRepository userRepository;

    public CommonsRep createTimekeeping(DtoTimeKeepingReq dtoTimeKeepingReq) {
        boolean checkUserId = userRepository.existsById(dtoTimeKeepingReq.getUserId());

        if (!checkUserId) {
            return Helper.getServerResponse(HttpStatus.NOT_FOUND, "Nhân viên không tồn tại", null);
        }

        DtbTimeKeeping dtbTimeKeeping = TimeKeepingMapper.toEntity(dtoTimeKeepingReq);

        DtbTimeKeeping save = timeKeepingRepository.save(dtbTimeKeeping);

        DtoTimeKeepingRep dtoTimeKeepingRep = TimeKeepingMapper.toDto(save);

        return Helper.getServerResponse(HttpStatus.OK, "Tạo chấm công cho nhân viên thành công", dtoTimeKeepingRep);
    }

    public CommonsRep getTimeKeeping(int page, int size) {
        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by("UpdateAt").descending()
        );

        Page<DtbTimeKeeping> dtbTimeKeeping = timeKeepingRepository.findAllByIsDeletedFalse(pageable);

        PageDataRep<DtoTimeKeepingRep> pageData = new PageDataRep<>();

        pageData.setData(
                dtbTimeKeeping.getContent()
                        .stream()
                        .map(TimeKeepingMapper::toDto)
                        .toList()
        );

        pageData.setPageableRep(
                Helper.mapToPageableRep(dtbTimeKeeping)
        );

        return Helper.getServerResponse(HttpStatus.OK, "Lấy danh sách thành công", pageData);
    }

    public CommonsRep getTimeKeepingByEmployee(String userId, YearMonth yearMonth) {
        UUID uuiUserId = UUID.fromString(userId);

        int month = yearMonth.getMonthValue();
        int year = yearMonth.getYear();

        List<DtbTimeKeeping> dtbTimeKeepings = timeKeepingRepository.findWorkDaysByUserAndMonth(uuiUserId,
                month, year);

        List<DtoTimeKeepingRep> dtoTimeKeepingReps = dtbTimeKeepings
                .stream()
                .map(TimeKeepingMapper::toDto)
                .toList();

        return Helper.getServerResponse(HttpStatus.OK, "Lấy chấm công theo tháng thành công", dtoTimeKeepingReps);
    }

    public CommonsRep deleteTimeKeeping(String timeKeepingId) {
        UUID uuidTimeKeepingId = UUID.fromString(timeKeepingId);

        DtbTimeKeeping dtbTimeKeeping = timeKeepingRepository.findByIdAndIsDeletedFalse(uuidTimeKeepingId)
                .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "timeKeeping not found"));

        dtbTimeKeeping.setDeleted(true);

        timeKeepingRepository.save(dtbTimeKeeping);

        return Helper.getServerResponse(HttpStatus.OK, "Xóa chấm công thành công", null);
    }

    public CommonsRep updateTimeKeeping(String timeKeepingId, DtoTimeKeepingReq dtoTimeKeepingReq) {
        UUID uuidTimeKeepingId = UUID.fromString(timeKeepingId);

        DtbTimeKeeping dtbTimeKeepingData = timeKeepingRepository.findByIdAndIsDeletedFalse(uuidTimeKeepingId)
                .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "timeKeeping Không tồn tại"));

        if (!Objects.equals(dtbTimeKeepingData.getUserId(), dtoTimeKeepingReq.getUserId())) {
            return Helper.getServerResponse(HttpStatus.NOT_FOUND, "Chấm công không đúng nhân viên", null);
        }

        if (!hasChangeTimeKeeping(dtbTimeKeepingData, dtoTimeKeepingReq)) {
            return Helper.getServerResponse(HttpStatus.OK, "Không có sự thay đổi", null);
        }

        DtbTimeKeeping dtbTimeKeeping = TimeKeepingMapper.toEntity(dtoTimeKeepingReq);

        DtbTimeKeeping save  = timeKeepingRepository.save(dtbTimeKeeping);

        DtoTimeKeepingRep dtoTimeKeepingRep = TimeKeepingMapper.toDto(save);

        return Helper.getServerResponse(HttpStatus.OK, "Cập nhật chấm công cho nhân viên thành công",
                dtoTimeKeepingRep);
    }

    private boolean hasChangeTimeKeeping(DtbTimeKeeping dtbTimeKeeping, DtoTimeKeepingReq dtoTimeKeepingReq) {
        return !Objects.equals(dtbTimeKeeping.getWorkDate(), dtoTimeKeepingReq.getWorkDate()) ||
                !Objects.equals(dtbTimeKeeping.getCheckIn(), dtoTimeKeepingReq.getCheckIn()) ||
                !Objects.equals(dtbTimeKeeping.getCheckOut(), dtoTimeKeepingReq.getCheckIn()) ||
                !Objects.equals(dtbTimeKeeping.getStatus(), dtoTimeKeepingReq.getStatus());

    }
}
