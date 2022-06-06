package com.junit.service;


import com.junit.model.Product;
import com.junit.repo.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepo productRepo;

    public Product  addProduct(Product product){
        return productRepo.save(product);
    }

    public List<Product>  getList(){
        return (List<Product>) productRepo.findAll();
    }

    public  Product getById(int id){
        return  productRepo.findById(id).get();
    }

public  Product update(Product product,int id){

Product exist= productRepo.findById(id).orElse(null);
exist.setName(product.getName());
exist.setPrice(product.getPrice());
exist.setQuantity(product.getQuantity());
productRepo.save(exist);

        return exist;
}

public  String delete(int id){
 productRepo.deleteById(id);
 return  "deleted";
}
}
