package com.candidate.candidate_backend.controller;

import com.candidate.candidate_backend.dto.CommonsRep;
import com.candidate.candidate_backend.dto.lever.DtoLeverReq;
import com.candidate.candidate_backend.service.LeverService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/lever")
public class LeverController {
    @Autowired
    private LeverService leverService;

    @PreAuthorize("@ss.hasPermission('LEVER_CREATE')")
    @PostMapping
    public CommonsRep createLever(@RequestBody @Valid DtoLeverReq dtoLeverReq) {
        return leverService.createLever(dtoLeverReq);
    }

    @PreAuthorize("@ss.hasPermission('LEVER_VIEW')")
    @GetMapping
    public CommonsRep getLever() {
        return leverService.getLever();
    }


    @PreAuthorize("@ss.hasPermission('LEVER_DELETE')")
    @DeleteMapping("/{leverId}")
    public CommonsRep deleteLever(@PathVariable String leverId) {
        return leverService.deleteLever(leverId);
    }

    @PreAuthorize("@ss.hasPermission('LEVER_UPDATE')")
    @PutMapping("/{leverId}")
    public CommonsRep updateLever(
            @Valid
            @PathVariable(name = "leverId") String leverId,
            @RequestBody DtoLeverReq dtoLeverReq
            ) {
        return leverService.updateLever(leverId, dtoLeverReq);
    }
}
