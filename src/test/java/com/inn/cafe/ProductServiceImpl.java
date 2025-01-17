package com.inn.cafe;

import com.inn.cafe.serviceImpl.productServiceImpl;
import com.inn.cafe.JWT.JwtFilter;
import com.inn.cafe.POJO.Product;
import com.inn.cafe.constents.CafeConstants;
import com.inn.cafe.dao.productDao;
import com.inn.cafe.utils.CafeUtils;
import com.inn.cafe.wrapper.ProductWrapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceImpl {

    @InjectMocks
    private productServiceImpl productServiceImpl;

    @Mock
    private productDao productDao;

    @Mock
    private JwtFilter jwtFilter;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAddNewProduct_AdminAccess() {
        // Arrange
        Map<String, String> requestMap = new HashMap<>();
        requestMap.put("name", "Espresso");
        requestMap.put("categoryId", "1");
        requestMap.put("description", "Strong coffee");
        requestMap.put("price", "10");

        when(jwtFilter.isAdmin()).thenReturn(true);

        // Act
        ResponseEntity<String> response = productServiceImpl.addNewProduct(requestMap);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Product Added Successfully", response.getBody());
        verify(productDao, times(1)).save(any(Product.class));
    }

    @Test
    public void testAddNewProduct_UnauthorizedAccess() {
        // Arrange
        Map<String, String> requestMap = new HashMap<>();
        when(jwtFilter.isAdmin()).thenReturn(false);

        // Act
        ResponseEntity<String> response = productServiceImpl.addNewProduct(requestMap);

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals(CafeConstants.UNAUTHORIZED_ACCESS, response.getBody());
        verify(productDao, never()).save(any(Product.class));
    }

    @Test
    public void testGetAllProducts() {
        // Arrange
        List<ProductWrapper> productList = new ArrayList<>();
        productList.add(new ProductWrapper(1, "Espresso","Available", 10 ));
        when(productDao.getAllProduct()).thenReturn(productList);

        // Act
        ResponseEntity<List<ProductWrapper>> response = productServiceImpl.getAllProduct();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        verify(productDao, times(1)).getAllProduct();
    }

    @Test
    public void testDeleteProduct_Success() {
        // Arrange
        Integer productId = 1;
        when(jwtFilter.isAdmin()).thenReturn(true);
        when(productDao.findById(productId)).thenReturn(Optional.of(new Product()));

        // Act
        ResponseEntity<String> response = productServiceImpl.delete(productId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Product is deleted successfully", response.getBody());
        verify(productDao, times(1)).deleteById(productId);
    }

    @Test
    public void testDeleteProduct_ProductNotFound() {
        // Arrange
        Integer productId = 1;
        when(jwtFilter.isAdmin()).thenReturn(true);
        when(productDao.findById(productId)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<String> response = productServiceImpl.delete(productId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Product id doesn't exist", response.getBody());
        verify(productDao, never()).deleteById(productId);
    }

    @Test
    public void testDeleteProduct_UnauthorizedAccess() {
        // Arrange
        Integer productId = 1;
        when(jwtFilter.isAdmin()).thenReturn(false);

        // Act
        ResponseEntity<String> response = productServiceImpl.delete(productId);

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals(CafeConstants.UNAUTHORIZED_ACCESS, response.getBody());
        verify(productDao, never()).deleteById(productId);
    }
}
