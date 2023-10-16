package com.wjy.wantedpreonboarding.controller;

import com.wjy.wantedpreonboarding.dto.req.RecruitmentApplyDto;
import com.wjy.wantedpreonboarding.dto.req.RecruitmentCreateDto;
import com.wjy.wantedpreonboarding.dto.req.RecruitmentEditDto;
import com.wjy.wantedpreonboarding.dto.res.RecruitmentCreateResponseDto;
import com.wjy.wantedpreonboarding.dto.res.RecruitmentDetailResponseDto;
import com.wjy.wantedpreonboarding.dto.res.RecruitmentSearchResponseDto;
import com.wjy.wantedpreonboarding.service.RecruitmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/recruitments")
@RequiredArgsConstructor
public class RecruitmentController {

    private final RecruitmentService recruitmentService;

    @PostMapping
    public ResponseEntity<RecruitmentCreateResponseDto> create(
            @RequestBody RecruitmentCreateDto createDto
    ) {
        Long createdId = recruitmentService.createRecruitment(createDto);
        return ResponseEntity.ok(new RecruitmentCreateResponseDto(createdId));
    }

    @PatchMapping("/{recruitmentId}")
    public ResponseEntity<Void> edit(
            @PathVariable Long recruitmentId,
            @RequestBody RecruitmentEditDto editDto
            ) {
        recruitmentService.editRecruitment(recruitmentId, editDto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{recruitmentId}")
    public ResponseEntity<Void> delete(
            @PathVariable Long recruitmentId) {
        recruitmentService.deleteRecruitment(recruitmentId);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<RecruitmentSearchResponseDto>> search(
            @RequestParam String search) {
        List<RecruitmentSearchResponseDto> searchResponses = recruitmentService.searchRecruitment(search);
        return ResponseEntity.ok(searchResponses);
    }

    @GetMapping("/{recruitmentId}")
    public ResponseEntity<RecruitmentDetailResponseDto> getDetail(
            @PathVariable Long recruitmentId) {
        RecruitmentDetailResponseDto detailResponse = recruitmentService.getRecruitmentDetail(recruitmentId);
        return ResponseEntity.ok(detailResponse);
    }

    @PostMapping("/{recruitmentId}")
    public ResponseEntity<Void> apply(
            @PathVariable Long recruitmentId,
            @RequestBody RecruitmentApplyDto applyDto
    ) {
        recruitmentService.applyRecruitment(recruitmentId, applyDto.getMemberId());
        return ResponseEntity.ok().build();
    }

}
