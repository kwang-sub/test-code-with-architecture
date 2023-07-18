package com.example.demo.medium.user.service;

import com.example.demo.mock.FakeMailSender;
import com.example.demo.user.controller.port.CertificationService;
import com.example.demo.user.service.CertificationServiceImpl;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AuthenticationServiceImplTest {

    @Test
    public void 이메일과_컨텐츠가_제대로_만들어져서_보내지는지_테스트한다() {
        // given
        FakeMailSender fakeMailSender = new FakeMailSender();
        CertificationService certificationService = new CertificationServiceImpl(fakeMailSender);
        // when
        certificationService.send("kwang@naver.com", 1L, "333");
        // then
        assertThat(fakeMailSender.email).isEqualTo("kwang@naver.com");
        assertThat(fakeMailSender.title).isEqualTo("Please certify your email address");
        assertThat(fakeMailSender.content)
                .isEqualTo("Please click the following link to certify your email address: http://localhost:8080/api/users/" + 1 + "/verify?certificationCode=" + 333);
    }
}