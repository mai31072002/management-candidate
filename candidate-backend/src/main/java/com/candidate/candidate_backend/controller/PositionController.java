package com.candidate.candidate_backend.controller;

import com.candidate.candidate_backend.dto.CommonsRep;
import com.candidate.candidate_backend.dto.position.DtoPositionReq;
import com.candidate.candidate_backend.service.PositionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/position")
public class PositionController {
    @Autowired
    private PositionService positionService;

    @PreAuthorize("@ss.hasPermission('POSITION_CREATE')")
    @PostMapping
    public CommonsRep createPosition(@RequestBody @Valid DtoPositionReq dtoPositionReq) {
        return positionService.createPosition(dtoPositionReq);
    }

    @PreAuthorize("@ss.hasPermission('POSITION_VIEW')")
    @GetMapping
    public CommonsRep getPosition() {
        return positionService.getPosition();
    }

    @PreAuthorize("@ss.hasPermission('POSITION_VIEW_DETAIL')")
    @GetMapping("/{positionId}")
    public CommonsRep getPositionById(@PathVariable(name = "positionId") String positionId) {
        return positionService.getPositionById(positionId);
    }

    @PreAuthorize("@ss.hasPermission('POSITION_DELETE')")
    @DeleteMapping("/{positionId}")
    public CommonsRep deletePosition(@PathVariable String positionId) {
        return positionService.deletePosition(positionId);
    }

    @PreAuthorize("@ss.hasPermission('POSITION_UPDATE')")
    @PutMapping("/{positionId}")
    public CommonsRep updatePosition(
            @Valid
            @PathVariable(name = "positionId") String positionId,
            @RequestBody DtoPositionReq dtoPositionReq
            ) {
        return positionService.updatePosition(positionId, dtoPositionReq);
    }
}
