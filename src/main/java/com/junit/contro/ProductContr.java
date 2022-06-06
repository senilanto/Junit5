package com.junit.contro;

import com.junit.model.Product;
import com.junit.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductContr {

    @Autowired
    private ProductService productService;


@PostMapping
    public ResponseEntity<Product> add(@RequestBody Product product){
      Product product1=  productService.addProduct(product);
        return  new ResponseEntity<>(  product1, HttpStatus.CREATED );
    }


    @GetMapping
    public  ResponseEntity<List<Product>> getAll(){
    List<Product> list= productService.getList();
    return  new ResponseEntity<>(list,HttpStatus.OK);
    }


    @GetMapping("/{id}")
public  ResponseEntity<Product>  getById(@PathVariable("id") Integer id){
    Product product=productService.getById(id);
    return  new ResponseEntity<>(product,HttpStatus.OK);
    }


    @PutMapping("/update/{id}")
    public ResponseEntity<Product> update( @RequestBody Product product,@PathVariable("id")Integer id){
    Product product1= productService.update(product, id);
    return  new ResponseEntity<>(product1,HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public  ResponseEntity<String> dele(@PathVariable("id") Integer id){
    productService.delete(id);
    return  new ResponseEntity<>("deleted succes",HttpStatus.OK);
    }


 }
