package com.example.demo.medium.user.domain;

import com.example.demo.common.domain.exception.CertificationCodeNotMatchedException;
import com.example.demo.common.sevice.port.UuidHolder;
import com.example.demo.mock.FakeClockHolder;
import com.example.demo.mock.FakeUuidHolder;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserCreate;
import com.example.demo.user.domain.UserStatus;
import com.example.demo.user.domain.UserUpdate;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class UserTest {

    @Test
    public void User는_UserCreate_객체로_생성할_수_있다() {
        // given
        UserCreate userCreate = UserCreate.builder()
                .email("kwang@naver.com")
                .nickname("kwang")
                .address("Pangyo")
                .build();

        UuidHolder uuidHolder = new FakeUuidHolder("kk");
        // when
        User user = User.from(userCreate, uuidHolder);

        // then
        assertThat(user.getId()).isNull();
        assertThat(user.getEmail()).isEqualTo(userCreate.getEmail());
        assertThat(user.getNickname()).isEqualTo(userCreate.getNickname());
        assertThat(user.getAddress()).isEqualTo(userCreate.getAddress());
        assertThat(user.getCertificationCode()).isEqualTo("kk");
    }

    @Test
    public void User는_UserUpdate_객체로_수정할_수_있다() {
        // given
        User user = User.builder()
                .id(1L)
                .email("kwang@naver.com")
                .nickname("kwang")
                .address("Pangyo")
                .build();

        UserUpdate userUpdate = UserUpdate.builder()
                .address("update")
                .nickname("update")
                .build();

        // when
        User resultUser = user.update(userUpdate);
        // then

        assertThat(resultUser.getId()).isEqualTo(user.getId());
        assertThat(resultUser.getAddress()).isEqualTo(userUpdate.getAddress());
        assertThat(resultUser.getNickname()).isEqualTo(userUpdate.getNickname());
    }

    @Test
    public void User는_로그인을_할_수_있고_로그인시_마지막_로그인_시간이_변경된다() {
        // given
        User user = User.builder()
                .id(1L)
                .email("kwang@naver.com")
                .nickname("kwang")
                .address("Pangyo")
                .build();
        FakeClockHolder fakeClockHolder = new FakeClockHolder(1L);
        // when
        User loginUser = user.login(fakeClockHolder);

        // then
        assertThat(loginUser.getLastLoginAt()).isEqualTo(1L);
    }

    @Test
    public void 유효한_인증_코드로_계정을_활성화_할_수_있다() {
        // given
        User user = User.builder()
                .id(1L)
                .email("kwang@naver.com")
                .nickname("kwang")
                .address("Pangyo")
                .status(UserStatus.PENDING)
                .certificationCode("111")
                .build();

        // when
        User certificateUser = user.certificate("111");

        // then
        assertThat(certificateUser.getStatus()).isEqualTo(UserStatus.ACTIVE);
    }

    @Test
    public void 잘못된_인증_코드로_계정을_활성화_하려하면_예외를_던진다() {
        // given
        User user = User.builder()
                .id(1L)
                .email("kwang@naver.com")
                .nickname("kwang")
                .address("Pangyo")
                .certificationCode("111")
                .build();

        // when

        // then
        assertThatThrownBy(() -> user.certificate("1"))
                .isInstanceOf(CertificationCodeNotMatchedException.class);
    }
}