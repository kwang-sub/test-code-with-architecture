package com.example.demo.user.service;

import com.example.demo.common.domain.exception.CertificationCodeNotMatchedException;
import com.example.demo.common.domain.exception.ResourceNotFoundException;
import com.example.demo.common.sevice.port.ClockHolder;
import com.example.demo.common.sevice.port.UuidHolder;
import com.example.demo.mock.FakeClockHolder;
import com.example.demo.mock.FakeMailSender;
import com.example.demo.mock.FakeUserRepository;
import com.example.demo.mock.FakeUuidHolder;
import com.example.demo.post.service.port.MailSender;
import com.example.demo.user.controller.port.CertificationService;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserCreate;
import com.example.demo.user.domain.UserStatus;
import com.example.demo.user.domain.UserUpdate;
import com.example.demo.user.service.port.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AuthenticationServiceImplTest {

    private MailSender mailSender = new FakeMailSender();
    private CertificationService certificationServiceImpl = new CertificationServiceImpl(mailSender);
    private ClockHolder clockHolder;

    private UuidHolder uuidHolder;

    private String uuid;
    private long clock;

    {
        uuid = "tt";
        clock = 1L;
        clockHolder = new FakeClockHolder(clock);
        uuidHolder = new FakeUuidHolder(uuid);
    }

    private UserRepository userRepository = new FakeUserRepository();
    private UserServiceImpl userServiceImpl = new UserServiceImpl(userRepository, certificationServiceImpl, uuidHolder, clockHolder);


    @BeforeEach
    void init() {
        User user1 = User.builder()
                .email("kwang@naver.com")
                .nickname("kwang")
                .address("test")
                .certificationCode("aaaaaa-aaazaaaa-a")
                .status(UserStatus.ACTIVE)
                .build();

        userRepository.save(user1);

        User user2 = User.builder()
                .email("kwang1@naver.com")
                .nickname("kwang")
                .address("test")
                .certificationCode("aaaaaa-aaazaaaa-a")
                .status(UserStatus.PENDING)
                .build();

        userRepository.save(user2);
    }


    @Test
    void getByEmail은_ACTIVE_상태인_유저를_찾아올_수_있다() {
        // given
        String email = "kwang@naver.com";
        // when
        User result = userServiceImpl.getByEmail(email);
        // then
        assertThat(result).isNotNull();
    }

    @Test
    void getByEmail은_PENDING_상태인_유저는_찾을_수_없다() {
        // given
        String email = "kwang1@naver.com";
        // when
        // then
        assertThatThrownBy(() -> userServiceImpl.getByEmail(email))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void getById은_ACTIVE_상태인_유저를_찾아올_수_있다() {
        // given
        // when
        User result = userServiceImpl.getById(1L);
        // then
        assertThat(result).isNotNull();
    }

    @Test
    void getById은_PENDING_상태인_유저는_찾을_수_없다() {
        // given
        // when
        // then
        assertThatThrownBy(() -> userServiceImpl.getById(2L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void create는_user_entity를_생성할_수_있다() {
        // given
        UserCreate userCreate = UserCreate.builder()
                .email("kwnag123@weedscomm.com")
                .address("Gveongi")
                .nickname("kwang")
                .build();
        // when
        User result = userServiceImpl.create(userCreate);
        // then
        assertThat(result.getId()).isNotNull();
        assertThat(result.getStatus()).isEqualTo(UserStatus.PENDING);
        assertThat(result.getCertificationCode()).isEqualTo(uuid);
    }

    @Test
    void update는_user_entity를_수정할_수_있다() {
        // given
        UserUpdate userCreateDto = UserUpdate.builder()
                .address("Gveongi")
                .nickname("change")
                .build();
        // when
        User result = userServiceImpl.update(1L, userCreateDto);
        // then
        assertThat(result.getId()).isNotNull();
        assertThat(result.getNickname()).isEqualTo("change");
    }

    @Test
    void login_마지막_로그인_시간이_변경된다() {
        // given
        // when
        userServiceImpl.login(1L);
        // then
        User result = userServiceImpl.getById(1L);
        assertThat(result.getLastLoginAt()).isEqualTo(clock);
    }

    @Test
    void PENDING_상태의_사용자는_인증_코드로_활성화_할_수_있다() {
        // given

        // when
        userServiceImpl.verifyEmail(2L, "aaaaaa-aaazaaaa-a");
        // then
        User result = userServiceImpl.getById(2L);
        assertThat(result.getStatus()).isEqualTo(UserStatus.ACTIVE);
    }

    @Test
    void PENDING_상태의_사용자는_인증_코드로_틀릴_경우_예외() {
        // given

        // when
        // then
        assertThatThrownBy(() -> userServiceImpl.verifyEmail(2L, "aaaaaa-aaaaaa-a"))
                .isInstanceOf(CertificationCodeNotMatchedException.class);
    }


}