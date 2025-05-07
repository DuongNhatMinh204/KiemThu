package com.nminh.kiemthu.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DepartmentCreateDTO {
    private String fullName ;
    private String shortName ;
    private String description ;
}
