package com.github.nits42.corebankingservice.controller;

import com.github.nits42.corebankingservice.dto.BranchDTO;
import com.github.nits42.corebankingservice.request.BranchRequest;
import com.github.nits42.corebankingservice.request.BranchUpdateRequest;
import com.github.nits42.corebankingservice.service.BranchService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/v1/branch")
@RequiredArgsConstructor
@Tag(name = "Branch API", description = "This contains apis related to branch")
public class BranchController {

    private final BranchService branchService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> createBranch(@Valid @RequestBody BranchRequest request) {
        return new ResponseEntity<>(branchService.createBranch(request), HttpStatus.CREATED);
    }

    @PutMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> updateBranch(@Valid @RequestBody BranchUpdateRequest request) {
        return new ResponseEntity<>(branchService.updateBranch(request), HttpStatus.OK);
    }

    @DeleteMapping("/{branchId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteBranch(@PathVariable UUID branchId) {
        return new ResponseEntity<>(branchService.deleteBranch(branchId), HttpStatus.OK);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<BranchDTO>> getAllBranch() {
        return new ResponseEntity<>(branchService.getAllBranch(), HttpStatus.OK);
    }

    @GetMapping("/{branchId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BranchDTO> getBranchByID(@PathVariable UUID branchId) {
        return new ResponseEntity<>(branchService.getBranchByID(branchId), HttpStatus.OK);
    }

}
