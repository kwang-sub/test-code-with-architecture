package com.example.demo.user.domain;

import com.example.demo.common.domain.exception.CertificationCodeNotMatchedException;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
public class User {

    private final Long id;

    private final String email;

    private final String nickname;

    private final String address;

    private final String certificationCode;

    private final UserStatus status;

    private final Long lastLoginAt;


    @Builder
    public User(Long id, String email, String nickname, String address, String certificationCode, UserStatus status, Long lastLoginAt) {
        this.id = id;
        this.email = email;
        this.nickname = nickname;
        this.address = address;
        this.certificationCode = certificationCode;
        this.status = status;
        this.lastLoginAt = lastLoginAt;
    }

    public static User from(UserCreate userCreate) {
        return User.builder()
                .email(userCreate.getEmail())
                .nickname(userCreate.getNickname())
                .address(userCreate.getAddress())
                .status(UserStatus.PENDING)
                .certificationCode(UUID.randomUUID().toString())
                .build();
    }

    public User update(UserUpdate userUpdate) {
        return User.builder()
                .id(this.getId())
                .email(this.getEmail())
                .nickname(userUpdate.getNickname())
                .address(userUpdate.getAddress())
                .status(this.getStatus())
                .certificationCode(this.getCertificationCode())
                .lastLoginAt(this.lastLoginAt)
                .build();

    }

    public User login(long millis) {
        return User.builder()
                .id(this.getId())
                .email(this.getEmail())
                .nickname(this.getNickname())
                .address(this.getAddress())
                .status(this.getStatus())
                .certificationCode(this.getCertificationCode())
                .lastLoginAt(millis)
                .build();
    }

    public User certificate(String certificationCode) {
        if (!this.getCertificationCode().equals(certificationCode)) {
            throw new CertificationCodeNotMatchedException();
        }

        return User.builder()
                .id(this.getId())
                .email(this.getEmail())
                .nickname(this.getNickname())
                .address(this.getAddress())
                .status(UserStatus.ACTIVE)
                .certificationCode(this.getCertificationCode())
                .lastLoginAt(this.lastLoginAt)
                .build();
    }
}
