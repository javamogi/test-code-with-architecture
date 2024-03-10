package com.example.demo.user.service;

import com.example.demo.common.domain.exception.CertificationCodeNotMatchedException;
import com.example.demo.common.domain.exception.ResourceNotFoundException;
import com.example.demo.mock.FakeMailSender;
import com.example.demo.mock.FakeUserRepository;
import com.example.demo.mock.TestClockHolder;
import com.example.demo.mock.TestUuidHolder;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserCreate;
import com.example.demo.user.domain.UserStatus;
import com.example.demo.user.domain.UserUpdate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;

class UserServiceTest {

    private UserServiceImpl userService;

    @BeforeEach
    void init() {
        FakeMailSender fakeMailSender = new FakeMailSender();
        FakeUserRepository fakeUserRepository = new FakeUserRepository();
        this.userService = UserServiceImpl.builder()
                .uuidHolder(new TestUuidHolder("aaaaaa-aaaa-aaaa-aaaa-aaaaaaab"))
                .clockHolder(new TestClockHolder(1678530673958L))
                .userRepository(fakeUserRepository)
                .certificationService(new CertificationService(fakeMailSender))
                .build();
        fakeUserRepository.save(User.builder()
                .id(1L)
                .email("pakoh200@naver.com")
                .nickname("javamogi")
                .address("Incheon")
                .certificationCode("aaaaaa-aaaa-aaaa-aaaa-aaaaaaaa")
                .status(UserStatus.ACTIVE)
                .lastLoginAt(0L)
                .build());

        fakeUserRepository.save(User.builder()
                .id(2L)
                .email("pakoh300@naver.com")
                .nickname("javamogi2")
                .address("Incheon")
                .certificationCode("aaaaaa-aaaa-aaaa-aaaa-aaaaaaab")
                .status(UserStatus.PENDING)
                .lastLoginAt(0L)
                .build());
    }

    @Test
    void getByEmail은_ACTIVE_상태인_유저를_찾아올_수_있다(){
        // given
        String email = "pakoh200@naver.com";

        // when
        User result = userService.getByEmail(email);

        // then
        assertThat(result.getNickname()).isEqualTo("javamogi");
    }

    @Test
    void getByEmail은_PANDING_상태인_유저를_찾아올_수_없다(){
        // given
        String email = "pakoh300@naver.com";

        // when
        // then
        assertThatThrownBy(() -> {
            User result = userService.getByEmail(email);
        }).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void getById은_ACTIVE_상태인_유저를_찾아올_수_있다(){
        // given
        // when
        User result = userService.getById(1);

        // then
        assertThat(result.getNickname()).isEqualTo("javamogi");
    }

    @Test
    void getById은_PANDING_상태인_유저를_찾아올_수_없다(){
        // given
        // when
        // then
        assertThatThrownBy(() -> {
            User result = userService.getById(2);
        }).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void userCreate_를_이용하여_유저를_생성할_수_있다(){
        // given
        UserCreate userCreate = UserCreate.builder()
                .email("javamogi@gmail.com")
                .address("Korea")
                .nickname("mogi")
                .build();

        // when
        User result = userService.create(userCreate);

        // then
        assertThat(result.getId()).isNotNull();
        assertThat(result.getStatus()).isEqualTo(UserStatus.PENDING);
         assertThat(result.getCertificationCode()).isEqualTo("aaaaaa-aaaa-aaaa-aaaa-aaaaaaab");
    }

    @Test
    void userUpdate_를_이용하여_유저를_수정할_수_있다(){
        // given
        UserUpdate userUpdate = UserUpdate.builder()
                .address("Seoul")
                .nickname("mogi-ng")
                .build();

        // when
        userService.update(1, userUpdate);

        // then
        User userEntity = userService.getById(1);
        assertThat(userEntity.getId()).isNotNull();
        assertThat(userEntity.getAddress()).isEqualTo("Seoul");
        assertThat(userEntity.getNickname()).isEqualTo("mogi-ng");
    }

    @Test
    void user를_로그인_시키면_마지막_로그인_시간이_변경된다(){
        // given
        // when
        userService.login(1);

        // then
        User user = userService.getById(1);
        assertThat(user.getLastLoginAt()).isEqualTo(1678530673958L);
    }

    @Test
    void PENDING_상태의_사용자는_인증_코드로_ACTIVE_시킬_수_있다(){
        // given
        // when
        userService.verifyEmail(2, "aaaaaa-aaaa-aaaa-aaaa-aaaaaaab");

        // then
        User userEntity = userService.getById(2);
        assertThat(userEntity.getStatus()).isEqualTo(UserStatus.ACTIVE);
    }

    @Test
    void PENDING_상태의_사용자는_잘못된_인증_코드를_받으면_에러를_던진다(){
        // given
        // when
        // then
        assertThatThrownBy(() -> {
            userService.verifyEmail(2, "aaaaaa-aaaa-aaaa-aaaa-aaaaaaac");
        }).isInstanceOf(CertificationCodeNotMatchedException.class);
    }
}