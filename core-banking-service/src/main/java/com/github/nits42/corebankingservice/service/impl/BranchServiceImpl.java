package com.github.nits42.corebankingservice.service.impl;

import com.github.nits42.corebankingservice.dto.BranchDTO;
import com.github.nits42.corebankingservice.entities.Branch;
import com.github.nits42.corebankingservice.enums.Status;
import com.github.nits42.corebankingservice.exceptions.BankingAppCoreServiceApiException;
import com.github.nits42.corebankingservice.exceptions.ResourceNotFoundException;
import com.github.nits42.corebankingservice.repository.BranchRepository;
import com.github.nits42.corebankingservice.request.BranchRequest;
import com.github.nits42.corebankingservice.request.BranchUpdateRequest;
import com.github.nits42.corebankingservice.service.BranchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service("branchService")
@RequiredArgsConstructor
public class BranchServiceImpl implements BranchService {

    private final BranchRepository branchRepository;
    private final ModelMapper modelMapper;

    @Override
    public String createBranch(BranchRequest request) {
        log.info("Branch creation process is progress");

        if (branchRepository.existsByBranchName(request.getBranchName()))
            throw BankingAppCoreServiceApiException.builder()
                    .message(request.getBranchName() + " is already registered")
                    .httpStatus(HttpStatus.FOUND)
                    .build();

        Branch branch = Branch.builder()
                .branchName(request.getBranchName())
                .state(request.getState())
                .city(request.getCity())
                .country(request.getCountry())
                .postalCode(request.getPostalCode())
                .landmark(request.getLandmark())
                .status(Status.ACTIVE)
                .build();
        branchRepository.save(branch);
        log.info("Branch creation process is completed");

        return "Branch is created successfully";
    }

    @Override
    public String updateBranch(BranchUpdateRequest request) {
        log.info("Branch details updation process is progress");

        Branch branch = branchRepository.findById(request.getId())
                .orElseThrow(() ->
                        BankingAppCoreServiceApiException
                                .builder()
                                .message(request.getId() + " is not found")
                                .httpStatus(HttpStatus.NOT_FOUND)
                                .build()
                );

        modelMapper.map(request, branch);

        branchRepository.save(branch);
        log.info("Branch details updation is completed");

        return "Branch details are updated successfully";
    }

    @Override
    public String deleteBranch(UUID branchId) {
        log.info("Branch deletion process is in progress");
        Branch branch = branchRepository.findById(branchId)
                .orElseThrow(() ->
                        BankingAppCoreServiceApiException
                                .builder()
                                .message(branchId + " is not found")
                                .httpStatus(HttpStatus.NOT_FOUND)
                                .build()
                );

        branch.setStatus(Status.CLOSED);
        branchRepository.save(branch);
        log.info("Branch deletion process is completed");
        return "Branch deleted successfully";
    }

    @Override
    public List<BranchDTO> getAllBranch() {
        log.info("Retrieving branch details is in progress");
        List<BranchDTO> branchList = branchRepository.findAllByStatus(Status.ACTIVE)
                .stream()
                .map(branch -> modelMapper.map(branch, BranchDTO.class))
                .toList();

        log.info("Retrieving branch details is completed");

        return branchList;
    }

    @Override
    public BranchDTO getBranchByID(UUID branchId) {
        log.info("Retrieving branch details is in progress");

        Branch branch = branchRepository.findByIdAndStatus(branchId, Status.ACTIVE)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Branch with " + branchId + " is not found")
                );

        log.info("Retrieving branch details is completed");
        BranchDTO branchDTO = modelMapper.map(branch, BranchDTO.class);
        return branchDTO;
    }


}
