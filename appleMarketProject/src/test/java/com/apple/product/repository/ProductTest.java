package com.apple.product.repository;

import com.apple.common.vo.PageRequestDTO;
import com.apple.common.vo.PageResponseDTO;
import com.apple.product.controller.ProductController;
import com.apple.product.domain.Product;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;

import java.util.List;

@SpringBootTest
@Slf4j
public class ProductTest {

    @Autowired
    private ProductController productController;
/*
    @Test
    public void testProductList() {
        PageRequestDTO pageRequestDTO = new PageRequestDTO();
        pageRequestDTO.setSearch("product_name");
        pageRequestDTO.setKeyword("상품");

        Model model = new ConcurrentModel();

        String viewName = productController.productList(pageRequestDTO, model);

        PageResponseDTO<Product> pageResponseDTO = (PageResponseDTO<Product>) model.getAttribute("productList");
        List<Product> productList = pageResponseDTO.getDtoList();

        for(Product product : productList) {
            log.info(product.toString());
        }
        log.info("viewName ==>" + viewName);
    }*/
}
