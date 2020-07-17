package com.example.sessiondemo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SessiondemoApplication.class)
public class SessiondemoApplicationTests {

//    private Jedis jedis;
    private TestRestTemplate testRestTemplate;
    private TestRestTemplate testRestTemplateWithAuth;
//
    @LocalServerPort
    private int port;

//    @Test
//    void contextLoads() {
//    }

//    @Before
//    public void clearRedisData() {
//
//        testRestTemplate = new TestRestTemplate();
//        testRestTemplateWithAuth = new TestRestTemplate("admin", "password");
//
////        jedis = new Jedis("localhost", 6379);
////        jedis.flushAll();
//    }
//
    @Test
    public void testRedisControlsSession() {
        testRestTemplate = new TestRestTemplate();
        testRestTemplateWithAuth = new TestRestTemplate("admin", "password");


        ResponseEntity<String> result = testRestTemplateWithAuth.getForEntity(getTestUrl(), String.class);
        assertEquals("hello admin", result.getBody()); // login worked

        String sessionCookie = result.getHeaders().get("Set-Cookie").get(0).split(";")[0];
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", sessionCookie);
        HttpEntity<String> httpEntity = new HttpEntity<>(headers);
        result = testRestTemplate.exchange(getTestUrl(), HttpMethod.GET, httpEntity, String.class);
        assertEquals("hello admin", result.getBody()); // access with session works worked

    }

    private String getTestUrl(){
        return "http://localhost:" + port + "/emp/10036";
    }

}
