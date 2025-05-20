package com.nminh.kiemthu.model.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DegreeCreateDTO {
    @NotNull(message = "ARGUMENT_NOT_VALID")
    private String shortName ;
    @NotNull(message = "ARGUMENT_NOT_VALID")
    private String fullName ;

}
