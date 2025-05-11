package com.github.nits42.corebankingservice.service;

import com.github.nits42.corebankingservice.dto.BranchDTO;
import com.github.nits42.corebankingservice.request.BranchRequest;
import com.github.nits42.corebankingservice.request.BranchUpdateRequest;

import java.util.List;
import java.util.UUID;

public interface BranchService {

    String createBranch(BranchRequest request);

    String updateBranch(BranchUpdateRequest request);

    String deleteBranch(UUID branchId);

    List<BranchDTO> getAllBranch();

    BranchDTO getBranchByID(UUID branchId);

}
