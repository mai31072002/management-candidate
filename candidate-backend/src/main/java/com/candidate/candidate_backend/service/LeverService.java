package com.candidate.candidate_backend.service;

import com.candidate.candidate_backend.dto.CommonsRep;
import com.candidate.candidate_backend.dto.lever.DtoLeverRep;
import com.candidate.candidate_backend.dto.lever.DtoLeverReq;
import com.candidate.candidate_backend.entity.DtbLever;
import com.candidate.candidate_backend.exception.BusinessException;
import com.candidate.candidate_backend.mapper.LeverMapper;
import com.candidate.candidate_backend.repositorry.LeverRepository;
import com.candidate.candidate_backend.repositorry.PositionRepository;
import com.candidate.candidate_backend.util.Helper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class LeverService {

    @Autowired
    private LeverRepository leverRepository;

    @Autowired
    private PositionRepository positionRepository;

    public CommonsRep createLever(DtoLeverReq dtoLeverReq) {
        if (leverRepository.existsByLeverNumber(dtoLeverReq.getLeverNumber())) {
            return Helper.getServerResponse(HttpStatus.BAD_REQUEST, "lever đã tồn tại", null);
        }

        // Map từ DTO sang Entity
        DtbLever dtbLever = LeverMapper.toEntity(dtoLeverReq);

        // save vào DB
        DtbLever saved = leverRepository.save(dtbLever);

        // map Entity sang Dto
        DtoLeverRep response = LeverMapper.toDto(saved);

        return Helper.getServerResponse(HttpStatus.OK, "Thêm lever Thành công", response);
    }

    public CommonsRep getLever() {
        List<DtbLever> dtbLevers = leverRepository.findAllByIsDeletedFalse();

        List<DtoLeverRep> dtoLeverReps =
                dtbLevers
                        .stream()
                        .map(LeverMapper::toDto)
                        .toList()
                ;
        return Helper.getServerResponse(HttpStatus.OK, "Lấy ra danh sách lever thành công", dtbLevers);
    }

    public CommonsRep deleteLever(String leverId) {
        UUID uuidLeverId = UUID.fromString(leverId);

        long count = positionRepository.countByLeverIdAndIsDeletedFalse(uuidLeverId);

        if (count > 0) {
            return Helper.getServerResponse(HttpStatus.BAD_GATEWAY, "Không thể xóa lever vì vẫn còn gắn với chức " +
                            "vụ",
                    null);
        }

        DtbLever dtbLever = leverRepository.findByIdAndIsDeletedFalse(uuidLeverId)
                        .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "leverId không được để " +
                                "trống"));

        dtbLever.setDeleted(true);
        leverRepository.save(dtbLever);

        return Helper.getServerResponse(HttpStatus.OK, "xóa lever thành công", null);
    }

    public CommonsRep updateLever(String leverId, DtoLeverReq dtoLeverReq) {
        UUID uuidLeverId = UUID.fromString(leverId);

        DtbLever dtbLever = leverRepository.findById(uuidLeverId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "leverId not found", null));

        if (!hasChangeLever(dtbLever, dtoLeverReq)) {
            return Helper.getServerResponse(HttpStatus.OK, "Không có thay đổi", null);
        }

        dtbLever.setLeverNumber(dtoLeverReq.getLeverNumber());
        dtbLever.setDescription(dtoLeverReq.getDescription());

//        dtbPosition.setDepartment(dtbDepartment);

        DtbLever save = leverRepository.save(dtbLever);

        DtoLeverRep dtoLeverRep = LeverMapper.toDto(save);

        return Helper.getServerResponse(HttpStatus.OK, "cập nhật thành công", dtoLeverRep);
    }

    public boolean hasChangeLever(DtbLever dtbLever, DtoLeverReq dtoLeverReq) {
        return !Objects.equals(dtbLever.getLeverNumber(), dtoLeverReq.getLeverNumber()) ||
                !Objects.equals(dtbLever.getDescription(), dtoLeverReq.getDescription());
    }
}
