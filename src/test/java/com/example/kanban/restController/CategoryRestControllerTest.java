package com.example.kanban.restController;

import com.example.kanban.model.Category;
import com.example.kanban.services.category.CategoryService;
import com.example.kanban.services.category.CategoryServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;


@RunWith(SpringRunner.class)
@WebMvcTest(CategoryRestController.class)
class CategoryRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoryService categoryService;

    private String api;

    @Before
    public void setUp() {
        api = "/api/category";
    }


    @Test
    public void shouldReturnListCategory() throws Exception{
        //given
        given(categoryService.getListCategory()).willReturn(getListCategory());
        //when
        //then

        mockMvc.perform( get("/api/category"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)));
    }


    private List<Category> getListCategory() {
        Category category = new Category();
        category.setId(1);
        category.setNameCategory("Test1");
        Category category2 = new Category();
        category2.setId(2);
        category2.setNameCategory("Test2");
        Category category3 = new Category();
        category3.setId(3);
        category3.setNameCategory("Test3");

        return Arrays.asList(category, category2, category3);
    }

    private String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }



}