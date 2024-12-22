package io.webz.marketplace.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthenticationRequestDTO {

    private String username;
    private String password;

}
