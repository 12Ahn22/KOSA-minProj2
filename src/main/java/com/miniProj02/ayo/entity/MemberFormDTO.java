package com.miniProj02.ayo.entity;

import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class MemberFormDTO {
    private String id;
    private String name;
    private String password;
    private String birthdate;

    private String address;

    @Pattern(regexp = "^\\d{3}-\\d{4}-\\d{4}$", message = "XXX-XXXX-XXXX 형식으로 작성해주세요.")
    private String phone;

    @Pattern(regexp = "^[FM]$", message = "성별 값은 'F' 와 'M'만 가능합니다.")
    private String gender;
}
