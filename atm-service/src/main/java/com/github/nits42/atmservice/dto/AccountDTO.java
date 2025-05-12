package com.github.nits42.atmservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AccountDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long accountNumber;
    private BranchDTO branch;
    private double balance;
    private String accountType;

}
