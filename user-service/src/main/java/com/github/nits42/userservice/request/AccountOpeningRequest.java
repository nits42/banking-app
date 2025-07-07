package com.github.nits42.userservice.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

/**
 * Author: Nitin Kumar
 * Date:14/05/25
 * Time:00:55
 */


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AccountOpeningRequest {

    private UserRequest userRequest;
    private AddressRequest addressRequest;

}
