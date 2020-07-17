package com.example.sessiondemo;

import com.example.sessiondemo.service.AppController;
import com.example.sessiondemo.service.EmployeeService;
import com.example.sessiondemo.service.RedisService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(AppController.class)
public class EmployeeRestControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    EmployeeService employeeService;

    @MockBean
    private RedisService redisService;

    @Test
    public void login() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/emp/10036")
                .contentType(MediaType.APPLICATION_JSON)
                .with(user("admin").password("password").roles("ADMIN"))
        ).andReturn();

        Collection<String>  headers = result.getResponse().getHeaderNames();
        System.out.println(headers.toString());
    }

    @Test
    public void getEmployee() throws Exception{
//        MockMvcRequestBuilders.get()
//        TestRestTemplate testRestTemplateWithAuth = new TestRestTemplate("admin", "password");
//        ResponseEntity<String> result = testRestTemplateWithAuth.getForEntity(getTestUrl(), String.class);
//        assertEquals("hello admin", result.getBody()); // login worked
//        String sessionCookie = result.getHeaders().get("Set-Cookie").get(0).split(";")[0];

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/emp/10036")
        .contentType(MediaType.APPLICATION_JSON)
                .with(user("admin").password("password").roles("ADMIN"))
        )
                .andExpect(status().isOk()).andReturn();
        System.out.println(result.toString());

    }

    private String getTestUrl(){
        return "http://localhost:" + 8080 + "/";
    }
}
