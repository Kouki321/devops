package com.inn.cafe;



import com.inn.cafe.dao.BillDao;
import com.inn.cafe.dao.CategoryDao;
import com.inn.cafe.dao.productDao;
import com.inn.cafe.serviceImpl.DashboardServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class DashboardServiceImple {

    @InjectMocks
    private DashboardServiceImpl dashboardService;

    @Mock
    private CategoryDao categoryDao;

    @Mock
    private productDao productDao;

    @Mock
    private BillDao billDao;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetCount() {
        // Set up mock returns for count methods
        when(categoryDao.count()).thenReturn(5L);
        when(productDao.count()).thenReturn(10L);
        when(billDao.count()).thenReturn(15L);

        // Call the method under test
        ResponseEntity<Map<String, Object>> response = dashboardService.getCount();

        // Verify the response
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(5L, response.getBody().get("category"));
        assertEquals(10L, response.getBody().get("product"));
        assertEquals(15L, response.getBody().get("bill"));
    }
}
