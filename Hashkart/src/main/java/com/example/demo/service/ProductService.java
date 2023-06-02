package com.example.demo.service;



import com.example.demo.model.Product;
import com.example.demo.model.dao.ProductDAO;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service for handling product actions.
 */
@Service
public class ProductService {


  private ProductDAO productDAO;


  public ProductService(ProductDAO productDAO) {
    this.productDAO = productDAO;
  }

  
  public List<Product> getProducts() {
    return productDAO.findAll();
  }

}
