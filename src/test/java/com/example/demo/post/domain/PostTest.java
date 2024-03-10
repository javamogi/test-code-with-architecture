package com.example.demo.post.domain;

import com.example.demo.mock.TestClockHolder;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class PostTest {

    @Test
    void PostCreate로_게시물을_만들_수_있다() {
        // given
        PostCreate postCreate = PostCreate.builder()
                .writerId(1)
                .content("helloworld")
                .build();
        User writer = User.builder()
                .email("pakoh200@naver.com")
                .nickname("javamogi")
                .address("Incheon")
                .certificationCode("aaaaaa-aaaa-aaaa-aaaa-aaaaaaaa")
                .status(UserStatus.ACTIVE)
                .build();

        // when
        Post post = Post.from(writer, postCreate, new TestClockHolder(1125));

        // then
        assertThat(post.getContent()).isEqualTo("helloworld");
        assertThat(post.getWriter().getEmail()).isEqualTo("pakoh200@naver.com");
        assertThat(post.getWriter().getNickname()).isEqualTo("javamogi");
        assertThat(post.getWriter().getAddress()).isEqualTo("Incheon");
        assertThat(post.getWriter().getStatus()).isEqualTo(UserStatus.ACTIVE);
        assertThat(post.getCreatedAt()).isEqualTo(1125);
        assertThat(post.getWriter().getCertificationCode()).isEqualTo("aaaaaa-aaaa-aaaa-aaaa-aaaaaaaa");
    }

    @Test
    void PostUpdate로_게시물을_수정할_수_있다() {
        // given
        PostUpdate postUpdate = PostUpdate.builder()
                .content("foobar")
                .build();
        User writer = User.builder()
                .id(1L)
                .email("pakoh200@naver.com")
                .nickname("javamogi")
                .address("Incheon")
                .certificationCode("aaaaaa-aaaa-aaaa-aaaa-aaaaaaaa")
                .status(UserStatus.ACTIVE)
                .build();

        Post post = Post.builder()
                .id(1L)
                .content("helloworld")
                .createdAt(1678530673958L)
                .modifiedAt(1678530673958L)
                .writer(writer)
                .build();

        // when
        post = post.update(postUpdate, new TestClockHolder(1678530673958L));

        // then
        assertThat(post.getContent()).isEqualTo("foobar");
        assertThat(post.getWriter().getEmail()).isEqualTo("pakoh200@naver.com");
        assertThat(post.getWriter().getNickname()).isEqualTo("javamogi");
        assertThat(post.getWriter().getAddress()).isEqualTo("Incheon");
        assertThat(post.getWriter().getStatus()).isEqualTo(UserStatus.ACTIVE);
        assertThat(post.getCreatedAt()).isEqualTo(1678530673958L);
        assertThat(post.getWriter().getCertificationCode()).isEqualTo("aaaaaa-aaaa-aaaa-aaaa-aaaaaaaa");
    }
}
