package kr.co.hanbit.mastering.springmvc4.controller;

import java.util.Arrays;

import org.springframework.mock.web.MockHttpSession;

import kr.co.hanbit.mastering.springmvc4.profile.UserProfileSession;

public class SessionBuilder {

    private final MockHttpSession session;
    UserProfileSession sessionBean;

    public SessionBuilder() {
        session = new MockHttpSession();
        sessionBean = new UserProfileSession();
        session.setAttribute("scopedTarget.userProfileSession", sessionBean);
    }

    public MockHttpSession build() {
        return session;
    }

    public SessionBuilder userTastes(String... tastes) {
        sessionBean.setTastes(Arrays.asList(tastes));
        return this;
    }
}
