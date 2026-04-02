package com.candidate.candidate_backend.service;

import com.candidate.candidate_backend.dto.CommonsRep;
import com.candidate.candidate_backend.dto.position.DtoPositionRep;
import com.candidate.candidate_backend.dto.position.DtoPositionReq;
import com.candidate.candidate_backend.entity.DtbLever;
import com.candidate.candidate_backend.entity.DtbPosition;
import com.candidate.candidate_backend.exception.BusinessException;
import com.candidate.candidate_backend.mapper.PositionMapper;
import com.candidate.candidate_backend.repositorry.EmployeeRepository;
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
public class PositionService {

    @Autowired
    private PositionRepository positionRepository;

    @Autowired
    private LeverRepository leverRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    public CommonsRep createPosition(DtoPositionReq dtoPositionReq) {
        if (positionRepository.existsByPositionName(dtoPositionReq.getPositionName())) {
            throw new BusinessException(HttpStatus.BAD_REQUEST, "positionName đã tồn tại");
        }

        UUID uuidLeverId = UUID.fromString(dtoPositionReq.getLeverId());

        DtbLever dtbLever = leverRepository.findById(uuidLeverId)
                .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "leverId không được để trống"));

        // Map từ DTO sang Entity
        DtbPosition dtbPosition = PositionMapper.toEntity(dtoPositionReq, dtbLever);

        // save vào DB
        DtbPosition saved = positionRepository.save(dtbPosition);

        // map Entity sang Dto
        DtoPositionRep response = PositionMapper.toDto(saved);

        return Helper.getServerResponse(HttpStatus.OK, "Thêm Position Thành công", response);
    }

    public CommonsRep getPosition() {
        List<DtbPosition> dtbPositions = positionRepository.findAllByIsDeletedFalse();

        List<DtoPositionRep> dtoPositionRep =
                dtbPositions
                        .stream()
                        .map(PositionMapper::toDto)
                        .toList()
                ;
        return Helper.getServerResponse(HttpStatus.OK, "Lấy ra danh sách position thành công", dtoPositionRep);
    }

    public CommonsRep getPositionById(String positionId) {
        UUID uuidPositionId = UUID.fromString(positionId);

        DtbPosition dtbPosition = positionRepository.findByIdAndIsDeletedFalse(uuidPositionId)
                .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "positionId not found"));

        DtoPositionRep dtoPositionRep = PositionMapper.toDto(dtbPosition);

        return Helper.getServerResponse(HttpStatus.OK, "Lấy chi tiết position thành công", dtoPositionRep);
    }

    public CommonsRep deletePosition(String positionId) {
        UUID uuidPositionId = UUID.fromString(positionId);

        DtbPosition dtbPosition = positionRepository.findByIdAndIsDeletedFalse(uuidPositionId)
                        .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "positionId không được để trống"));

        long count = employeeRepository.countByPositionIdAndIsDeletedFalse(uuidPositionId);

        if (count > 0) {
            return Helper.getServerResponse(HttpStatus.BAD_GATEWAY, "Không thể xóa chức vụ vì vẫn còn nhân " +
                    "viên", null);
        }

        dtbPosition.setDeleted(true);
        positionRepository.save(dtbPosition);

        return Helper.getServerResponse(HttpStatus.OK, "xóa position thành công", null);
    }

    public CommonsRep updatePosition(String positionId, DtoPositionReq dtoPositionReq) {
        UUID uuidPositionId = UUID.fromString(positionId);
        UUID uuidLeverId = UUID.fromString(dtoPositionReq.getLeverId());

        DtbPosition dtbPosition = positionRepository.findById(uuidPositionId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "positionId not found", null));

        DtbLever dtbLever = leverRepository.findById(uuidLeverId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "leverId not found", null));

        if (!hasChangePosition(dtbPosition, dtoPositionReq)) {
            return Helper.getServerResponse(HttpStatus.OK, "Không có thay đổi", null);
        }

        dtbPosition.setPositionName(dtoPositionReq.getPositionName());
        dtbPosition.setDescription(dtoPositionReq.getDescription());
        dtbPosition.setLever(dtbLever);

//        dtbPosition.setDepartment(dtbDepartment);

        DtbPosition save = positionRepository.save(dtbPosition);

        DtoPositionRep dtoPositionRep = PositionMapper.toDto(save);

        return Helper.getServerResponse(HttpStatus.OK, "cập nhật thành công", dtoPositionRep);

    }

    public boolean hasChangePosition(DtbPosition entity, DtoPositionReq dto) {

        boolean nameChanged =
                !Objects.equals(entity.getPositionName(), dto.getPositionName());

        boolean descriptionChanged =
                !Objects.equals(entity.getDescription(), dto.getDescription());

        boolean leverChanged;

        if (entity.getLever() == null && dto.getLeverId() == null) {
            leverChanged = false;
        } else if (entity.getLever() == null || dto.getLeverId() == null) {
            leverChanged = true;
        } else {
            leverChanged = !entity.getLever().getId()
                    .equals(UUID.fromString(dto.getLeverId()));
        }

        return nameChanged || descriptionChanged || leverChanged;
    }

}
