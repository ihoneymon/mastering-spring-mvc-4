package kr.co.hanbit.mastering.springmvc4

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*


import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.SpringApplicationContextLoader
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.web.WebAppConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext

import kr.co.hanbit.mastering.springmvc4.search.StubTwitterSearchConfig;
import spock.lang.Specification

@ContextConfiguration(loader = SpringApplicationContextLoader,
classes = [MasteringSpringMvc4Application, StubTwitterSearchConfig])
@WebAppConfiguration
class HomeControllerSpec extends Specification {

    @Autowired
    WebApplicationContext wac;
    MockMvc mockMvc;
    def setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).
                build();
    }

    def "User is redirected to its profile on his first visit"() {
        when: "I navigate to the home page"
        def response = this.mockMvc.perform(get("/"))

        then: "I am redirected to the profile page"
        response
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/profile"))
    }
}
