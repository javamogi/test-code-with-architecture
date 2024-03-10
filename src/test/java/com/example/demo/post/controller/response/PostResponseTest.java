package com.example.demo.post.controller.response;

import com.example.demo.post.domain.Post;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class PostResponseTest {
    @Test
    void Post로_응답을_생성할_수_있다(){

        // given
        Post post = Post.builder()
                .content("helloworld")
                .writer(User.builder()
                        .email("pakoh200@naver.com")
                        .nickname("javamogi")
                        .address("Incheon")
                        .certificationCode("aaaaaa-aaaa-aaaa-aaaa-aaaaaaaa")
                        .status(UserStatus.ACTIVE)
                        .build())
                .build();
        // when
        PostResponse postResponse = PostResponse.from(post);

        // then
        assertThat(post.getContent()).isEqualTo("helloworld");
        assertThat(post.getWriter().getEmail()).isEqualTo("pakoh200@naver.com");
        assertThat(post.getWriter().getNickname()).isEqualTo("javamogi");
        assertThat(post.getWriter().getAddress()).isEqualTo("Incheon");
        assertThat(post.getWriter().getStatus()).isEqualTo(UserStatus.ACTIVE);
        assertThat(post.getWriter().getCertificationCode()).isEqualTo("aaaaaa-aaaa-aaaa-aaaa-aaaaaaaa");
    }
}
