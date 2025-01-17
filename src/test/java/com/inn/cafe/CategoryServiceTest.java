package com.inn.cafe;

import com.inn.cafe.dao.CategoryDao;
import com.inn.cafe.POJO.Category;
import com.inn.cafe.serviceImpl.CategoryServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class CategoryServiceTest {

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Mock
    private CategoryDao categoryDao;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllCategoryWithTrueFilter() {
        // Test avec filterValue = "true"
        ResponseEntity<List<Category>> response = categoryService.getAllCategory("true");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(0, response.getBody().size());
    }

    @Test
    void testGetAllCategoryWithoutFilter() {
        // Simulation de catégories dans le DAO
        List<Category> categories = new ArrayList<>();

        Category category1 = new Category();
        category1.setId(1);
        category1.setName("Category 1");

        Category category2 = new Category();
        category2.setId(2);
        category2.setName("Category 2");

        categories.add(category1);
        categories.add(category2);

        when(categoryDao.findAll()).thenReturn(categories);

        ResponseEntity<List<Category>> response = categoryService.getAllCategory("false");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        assertEquals("Category 1", response.getBody().get(0).getName());
    }

}
