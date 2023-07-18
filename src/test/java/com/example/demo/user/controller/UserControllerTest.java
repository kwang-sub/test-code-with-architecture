package com.example.demo.user.controller;

import com.example.demo.user.controller.port.UserReadService;
import com.example.demo.user.domain.User;
import org.junit.jupiter.api.Test;

class UserControllerTest {


    @Test
    void 사용자는_특정_유저의_정보를_전달_받을_수_있다() throws Exception {
        UserController.builder()
                .userReadService(new UserReadService() {
                    @Override
                    public User getByEmail(String email) {
                        return null;
                    }

                    @Override
                    public User getById(long id) {
                        return null;
                    }
                })
                .build();



    }

    @Test
    void 사용자는_존재하지_않는_유저의_아이디로_api_호출할_경우() throws Exception {

    }

    @Test
    void 사용자는_인증_코드로_계정을_활성화_할_수_있다() throws Exception {

    }

    @Test
    void 사용자는_내_정보를_불러올_때_개인정보인_주소도_갖고_올_수_있다() throws Exception {

    }

    @Test
    void 사용자는_내_정보를_수정할_수_있다() throws Exception {

    }
}