package com.nminh.kiemthu.enums;

import lombok.Getter;

@Getter
public enum Degree {
    THAC_SI("Thạc Sĩ " , "THS"),
    TIEN_SI("Tiến Sĩ","TS"),
    GIAO_SU("Giáo Sư","GS"),
    PHO_GIAO_SU("Phó Giáo Sư" , "PGS")
    ;

    private final String fullName ;
    private final String shortName ;
    Degree( String fullName,  String shortName) {
        this.fullName = fullName ;
        this.shortName = shortName ;
    }
}
