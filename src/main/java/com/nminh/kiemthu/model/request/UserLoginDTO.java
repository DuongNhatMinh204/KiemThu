package com.nminh.kiemthu.model.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginDTO {
    @NotNull(message = "ARGUMENT_NOT_VALID")
    private String username;
    @NotNull(message = "ARGUMENT_NOT_VALID")
    private String password;
}
