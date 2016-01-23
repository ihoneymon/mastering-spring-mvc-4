package kr.co.hanbit.mastering.springmvc4.profile;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class ProfileForm {

    private String twitterHandle;
    private String email;
    private LocalDate birthDate;
    private List<String> tastes = new ArrayList<>();

}