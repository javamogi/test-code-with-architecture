package com.example.demo.user.domain;

import com.example.demo.common.domain.exception.CertificationCodeNotMatchedException;
import com.example.demo.mock.TestClockHolder;
import com.example.demo.mock.TestUuidHolder;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class UserTest {

    @Test
    void UserCreate_객체로_생성할_수_있다(){
        // given
        UserCreate userCreate = UserCreate.builder()
                .email("javamogi@gmail.com")
                .nickname("JJinJavamogi")
                .address("Incheon")
                .build();

        // when
        User user = User.from(userCreate, new TestUuidHolder("aaaaaa-aaaa-aaaa-aaaa-aaaaaaaa"));

        // then
        assertThat(user.getId()).isNull();
        assertThat(user.getEmail()).isEqualTo("javamogi@gmail.com");
        assertThat(user.getNickname()).isEqualTo("JJinJavamogi");
        assertThat(user.getAddress()).isEqualTo("Incheon");
        assertThat(user.getStatus()).isEqualTo(UserStatus.PENDING);
        assertThat(user.getCertificationCode()).isEqualTo("aaaaaa-aaaa-aaaa-aaaa-aaaaaaaa");
    }

    @Test
    void UserUpdate_객체로_데이터를_업데이트_할_수_있다(){
        // given
        User user = User.builder()
                .id(1L)
                .email("pakoh200@naver.com")
                .nickname("javamogi")
                .address("Incheon")
                .certificationCode("aaaaaa-aaaa-aaaa-aaaa-aaaaaaaa")
                .status(UserStatus.ACTIVE)
                .lastLoginAt(100L)
                .build();
        UserUpdate userUpdate = UserUpdate.builder()
                .nickname("mogijava")
                .address("Seoul")
                .build();

        // when
        user = user.update(userUpdate);

        // then
        assertThat(user.getId()).isEqualTo(1L);
        assertThat(user.getEmail()).isEqualTo("pakoh200@naver.com");
        assertThat(user.getNickname()).isEqualTo("mogijava");
        assertThat(user.getAddress()).isEqualTo("Seoul");
        assertThat(user.getStatus()).isEqualTo(UserStatus.ACTIVE);
        assertThat(user.getCertificationCode()).isEqualTo("aaaaaa-aaaa-aaaa-aaaa-aaaaaaaa");
        assertThat(user.getLastLoginAt()).isEqualTo(100L);
    }

    @Test
    void 로그인을_할_수_있고_로그인시_마지막_로그인_시간이_변경된다(){
        // given
        User user = User.builder()
                .id(1L)
                .email("pakoh200@naver.com")
                .nickname("javamogi")
                .address("Incheon")
                .certificationCode("aaaaaa-aaaa-aaaa-aaaa-aaaaaaaa")
                .status(UserStatus.ACTIVE)
                .lastLoginAt(100L)
                .build();

        // when
        user = user.login(new TestClockHolder(1678530673958L));

        // then
        assertThat(user.getLastLoginAt()).isEqualTo(1678530673958L);
    }

    @Test
    void 유효한_인증_코드로_계정을_활성화_할_수_있다(){
        // given
        User user = User.builder()
                .id(1L)
                .email("pakoh200@naver.com")
                .nickname("javamogi")
                .address("Incheon")
                .certificationCode("aaaaaa-aaaa-aaaa-aaaa-aaaaaaaa")
                .status(UserStatus.PENDING)
                .lastLoginAt(100L)
                .build();

        // when
        user = user.certificate("aaaaaa-aaaa-aaaa-aaaa-aaaaaaaa");

        // then
        assertThat(user.getStatus()).isEqualTo(UserStatus.ACTIVE);
    }

    @Test
    void 잘못된_인증_코드로_계정을_활성화_하려하면_에러를_던진다(){
        // given
        User user = User.builder()
                .id(1L)
                .email("pakoh200@naver.com")
                .nickname("javamogi")
                .address("Incheon")
                .certificationCode("aaaaaa-aaaa-aaaa-aaaa-aaaaaaaa")
                .status(UserStatus.PENDING)
                .lastLoginAt(100L)
                .build();

        // when
        // then
        assertThatThrownBy(() -> user.certificate("aaaaaa-aaaa-aaaa-aaaa-aaaaaaab"))
                .isInstanceOf(CertificationCodeNotMatchedException.class);
    }
}
