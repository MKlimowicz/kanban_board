package com.example.kanban.services.category;

import com.example.kanban.dao.category.CategoryDao;
import com.example.kanban.exception.category.NotFoundCategoryException;
import com.example.kanban.model.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

class CategoryServiceImplTest {

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Mock
    private CategoryDao categoryDao;


    private Category category;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        category = getListCategory().get(0);
    }


    // -------------  List<Category> getListCategory() -------------

    @Test
    public void shouldReturnCategoryList() {
        //given
        given(categoryDao.getListCategory()).willReturn(getListCategory());
        //when
        List<Category> listCategory = categoryService.getListCategory();
        //then
        assertThat(listCategory, hasSize(3));
    }


    // ----------------- Category getCategoryByName(String nameCategory)  ------------

    @Test
    public void shouldThrowExceptionIfCategoryNotExistsByNameCategory() {
        //given
        given(categoryDao.findByNameCategory("Test")).willReturn(Optional.empty());
        //when
        //then
        assertThrows(NotFoundCategoryException.class, () -> {
           categoryService.getCategoryByName("Test");
        });
    }

    @Test
    public void shouldReturnCategoryByName() {
        //given
        given(categoryDao.findByNameCategory("Test1")).willReturn(Optional.ofNullable(category));
        //when
        Category category = categoryService.getCategoryByName("Test1");
        //then
        assertThat(category, equalTo(this.category));
        assertThat(category.getId(), is(this.category.getId()));
    }

    // -----------  Category getCategoryById(Integer id)  --------------

    @Test
    public void shouldThrowExceptionIfCategoryNotExistsById() {
        //given
        given(categoryDao.findById(1)).willReturn(Optional.empty());
        //when
        //then
        assertThrows(NotFoundCategoryException.class, () -> {
            categoryService.getCategoryById(1);
        });

    }

    @Test
    public void shouldReturnCategoryById() {
        //given
        given(categoryDao.findById(1)).willReturn(Optional.ofNullable(category));
        //when
        Category category = categoryService.getCategoryById(1);
        //then
        assertThat(category, equalTo(this.category));
        assertThat(category.getId(), is(this.category.getId()));
        assertThat(category.getId(), is(1));
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

}