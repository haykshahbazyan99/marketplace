package io.webz.marketplace.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserRegisterDTO {

    private String fullName;
    private String address;
    private String username;
    private String password;

}
