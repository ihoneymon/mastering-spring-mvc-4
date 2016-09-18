package kr.co.hanbit.mastering.springmvc4.user.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import kr.co.hanbit.mastering.springmvc4.user.User;
import kr.co.hanbit.mastering.springmvc4.user.UserRepository;
import kr.co.hanbit.mastering.springmvc4.utils.JsonUtil;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class UserApiControllerTest {
    @Autowired
    private WebApplicationContext wac;
    @Autowired
    private UserRepository userRepository;
    private MockMvc mockMvc;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
        userRepository.reset(new User("bob@spring.io"));
    }

    @Test
    public void test_should_list_users() throws Exception {
        this.mockMvc.perform(get("/api/users").accept(MediaType.APPLICATION_JSON_UTF8)).andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(1))).andExpect(jsonPath("$[0].email", is("bob@spring.io")));
    }

    @Test
    public void test_should_create_new_user() throws Exception {
        User user = new User("john@spring.io");
        this.mockMvc
                .perform(post("/api/users").contentType(MediaType.APPLICATION_JSON_UTF8).content(JsonUtil.toJson(user)))
                .andExpect(status().isCreated());
        assertThat(userRepository.findAll()).extracting(User::getEmail).containsOnly("bob@spring.io", "john@spring.io");
    }

    @Test
    public void test_should_delete_user() throws Exception {
        this.mockMvc.perform(delete("/api/user/bob@spring.io").accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());
        assertThat(userRepository.findAll()).hasSize(0);
    }

    @Test
    public void test_should_return_not_found_when_deleting_unknown_user() throws Exception {
        this.mockMvc.perform(delete("/api/user/non-existing@mail.com").accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound());
    }

    @Test
    public void test_put_should_update_existing_user() throws Exception {
        User user = new User("ignored@spring.io");

        this.mockMvc.perform(put("/api/user/bob@spring.io").contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(JsonUtil.toJson(user))).andExpect(status().isOk());

        assertThat(userRepository.findAll()).extracting(User::getEmail).containsOnly("bob@spring.io");
    }
}
