package com.example.demo.medium.user.service;

import com.example.demo.common.domain.exception.CertificationCodeNotMatchedException;
import com.example.demo.common.domain.exception.ResourceNotFoundException;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserCreate;
import com.example.demo.user.domain.UserStatus;
import com.example.demo.user.domain.UserUpdate;
import com.example.demo.user.service.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
@SqlGroup({
        @Sql(value = "/sql/delete-all.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD),
        @Sql(value = "/sql/user-repository-test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
})
@Transactional
class UserServiceImplTest {

    @Autowired
    private UserServiceImpl userServiceImpl;

    @MockBean
    private JavaMailSender mailSender;

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
        BDDMockito.doNothing().when(mailSender).send(any(SimpleMailMessage.class));
        // when
        User result = userServiceImpl.create(userCreate);
        // then
        assertThat(result.getId()).isNotNull();
        assertThat(result.getStatus()).isEqualTo(UserStatus.PENDING);
//        assertThat(result.getCertificationCode()).isEqualTo("T.T");
    }

    @Test
    void update는_user_entity를_수정할_수_있다() {
        // given
        UserUpdate userCreateDto = UserUpdate.builder()
                .address("Gveongi")
                .nickname("change")
                .build();
        BDDMockito.doNothing().when(mailSender).send(any(SimpleMailMessage.class));
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
        assertThat(result.getLastLoginAt()).isGreaterThan(0);
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