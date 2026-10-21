package com.poly.controller;

import com.poly.entity.Product;
import com.poly.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class ProductControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductController productController;

    @BeforeEach
    void setUp() {
        // Cấu hình ViewResolver để khắc phục triệt để lỗi Circular view path
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/templates/");
        viewResolver.setSuffix(".html");

        mockMvc = MockMvcBuilders.standaloneSetup(productController)
                .setViewResolvers(viewResolver)
                .build();
    }

    @Test
    @DisplayName("TC_PROD_01: Lấy danh sách sản phẩm hiển thị ra giao diện")
    void testListProducts() throws Exception {
        Product p1 = new Product(1L, "Laptop", 15000000.0, 10);
        Product p2 = new Product(2L, "Mouse", 300000.0, 25);
        when(productRepository.findAll()).thenReturn(Arrays.asList(p1, p2));

        mockMvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andExpect(view().name("products"))
                .andExpect(model().attributeExists("products"));

        verify(productRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("TC_PROD_02: Mở form thêm mới sản phẩm")
    void testShowCreateForm() throws Exception {
        mockMvc.perform(get("/products/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("product-form"))
                .andExpect(model().attributeExists("product"));
    }

    @Test
    @DisplayName("TC_PROD_03: Lưu hoặc cập nhật thông tin sản phẩm")
    void testSaveProduct() throws Exception {
        mockMvc.perform(post("/products/save")
                        .param("name", "Keyboard RGB")
                        .param("price", "850000.0")
                        .param("quantity", "15"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/products"));

        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    @DisplayName("TC_PROD_04: Mở form sửa sản phẩm khi tìm thấy ID")
    void testShowEditFormSuccess() throws Exception {
        Product product = new Product(1L, "Monitor 24 inch", 3500000.0, 5);
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        mockMvc.perform(get("/products/edit/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("product-form"))
                .andExpect(model().attribute("product", product));

        verify(productRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("TC_PROD_05: Ném lỗi IllegalArgumentException khi sửa ID không tồn tại")
    void testShowEditFormNotFound() {
        when(productRepository.findById(999L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(Exception.class, () -> {
            mockMvc.perform(get("/products/edit/999"));
        });

        assertTrue(exception.getCause() instanceof IllegalArgumentException);
        assertTrue(exception.getCause().getMessage().contains("Invalid ID: 999"));
    }

    @Test
    @DisplayName("TC_PROD_06: Xóa sản phẩm theo ID")
    void testDeleteProduct() throws Exception {
        mockMvc.perform(get("/products/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/products"));

        verify(productRepository, times(1)).deleteById(1L);
    }
}