package kr.co.hanbit.mastering.springmvc4.user;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.util.Assert;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class User {

    private String twitterHandle;
    private String email;
    private LocalDate birthDate;
    private List<String> tastes = new ArrayList<>();

    // 모든 필드에 대한 접근자/주입자 getter/setter 생성
    public User(String email) {
        setEmail(email);
    }

    public User setEmail(String email) {
        Assert.hasText(email);
        this.email = email;
        return this;
    }
}
