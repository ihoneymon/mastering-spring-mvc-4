package kr.co.hanbit.mastering.springmvc4.search;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import kr.co.hanbit.mastering.springmvc4.MasteringSpringMvc4Application;
import kr.co.hanbit.mastering.springmvc4.config.StubTwitterSearchConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = { MasteringSpringMvc4Application.class, StubTwitterSearchConfig.class })
@WebAppConfiguration
@ActiveProfiles("test")
public class SearchControllerTest {
    @Autowired
    private WebApplicationContext wac;
    private MockMvc mockMvc;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @Test
    public void test_should_search() throws Exception {
        this.mockMvc.perform(get("/search/mixed;keywords=spring")).andExpect(status().isOk())
                .andExpect(view().name("result-page")).andExpect(model().attribute("tweets", hasSize(2)))
                .andExpect(model().attribute("tweets",
                        hasItems(hasProperty("text", is("tweetText")), hasProperty("text", is("secondTweet")))));
    }    
}