package com.treabear.readinglist;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.samePropertyValuesAs;
import static org.hamcrest.Matchers.contains;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.treabear.readinglist.domain.Book;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;

/**
 * MockMvcWebTests
 * 
 * @SpringApplicationConfiguration 은 spring 1.3까지 사용가능 1.4부터는 @SpringBootTest 사용
 * 
 * @WebAppConfiguration 애너테이션은 SpringJUnit4ClassRunnder가 애플리케이션 컨텍스트로 (기본값인 비웹용 ApplicationContext가 아니라) WebApplicationContext를 생성하도록 선언한다. 
 * 
 * 테스트 수행
 * $ gradle test
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes=ReadingListApplication.class)
@WebAppConfiguration    //웹 컨텍스트 활성화
public class MockMvcWebTests {

    @Autowired
    private WebApplicationContext webContext; // WebApplicationContext 주입

    private MockMvc mockMvc;

    @Before
    public void setupMockMvc() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webContext).build();
    }

    @Test
    public void homepage() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/"))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.view().name("readingList"))
            .andExpect(MockMvcResultMatchers.model().attributeExists("books"))
            .andExpect(MockMvcResultMatchers.model().attribute("books", Matchers.is(Matchers.empty())));
    }

    public void homepage2() throws Exception {
        // import static
        mockMvc.perform(get("/"))
            .andExpect(status().isOk())
            .andExpect(view().name("readingList"))
            .andExpect(model().attributeExists("books"))
            .andExpect(model().attribute("books", Matchers.is(Matchers.empty())));
    }

    @Test
    public void postBook() throws Exception {

        // 1. 등록하고 요청 결과 검증
        mockMvc.perform(post("/")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("title", "BOOK TITLE")
                        .param("author", "BOOK AUTHOR")
                        .param("isbn", "1234567890")
                        .param("description", "DESCRIPTION"))
            .andExpect(status().is3xxRedirection())
            .andExpect(header().string("Location", "/"));

        // 2. 메인 페이지에 새로운 GET 요청을 실행하여 새로 생성된 책이 모델에 있는지 검증
        Book expectedBook = new Book(); // 생성할 책 정보 설정
        expectedBook.setId(1L);            
        // expectedBook.setReader("yoojin");
        expectedBook.setTitle(("TITLE"));
        expectedBook.setAuthor("AUTHOR");
        expectedBook.setIsbn("1234567890");
        expectedBook.setDescription("DESCRIPTION");

        mockMvc.perform(get("/"))   // post 요청 실행
            .andExpect(status().isOk())
            .andExpect(view().name("readingList"))
            .andExpect(model().attributeExists("books"))
            .andExpect(model().attribute("books", hasSize(1)))
            .andExpect(model().attribute("books", contains(samePropertyValuesAs(expectedBook))));

    }
}
