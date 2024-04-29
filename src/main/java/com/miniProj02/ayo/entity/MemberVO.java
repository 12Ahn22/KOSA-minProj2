package com.miniProj02.ayo.entity;

import lombok.Data;

@Data
public class MemberVO {
    private String id;
    private String name;
    private String password;
    private String birthdate;
    private String address;
    private String phone;
    private String gender;
    private int auth;
}
