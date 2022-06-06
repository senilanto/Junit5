package com.junit;

import com.junit.model.Product;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import  org.junit.Assert;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertAll;


//while running the application run in a random port
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class JunitApplicationTests {

	@Autowired
	private TestH2Repository testH2Repository;


	//to get the port
	@LocalServerPort
	private  int port;

	//it is for the base url
	private  String baseUrl="http://localhost";

	//to communication with rest apis and spring boot intergration
	private static RestTemplate restTemplate;


	//initialize the rest template and to run before all the testcases initialize the rest template
	@BeforeAll
public static void init(){
	restTemplate= new RestTemplate();
}

//build the base url and it runs before all the test cases
	@BeforeEach
	public  void  setUp(){
		baseUrl=baseUrl.concat(":").concat(port+"").concat("/product");
	}


	@Test
	public  void addProduct(){
		Product product= new Product(1,"Earphone",400,3);
	Product response=	restTemplate.postForObject(baseUrl,product,Product.class);
	assertEquals("Earphone",response.getName());
	assertEquals(1,testH2Repository.findAll().size());

	}

	@Test
	@Sql(statements = "INSERT INTO PRODUCT (id,name,price,quantity) VALUES(1,'AC',300,2)",executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(statements = "INSERT INTO PRODUCT (id,name,price,quantity) VALUES(2,'AC',300,2)",executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(statements = "DELETE FROM PRODUCT WHERE ID=1",executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	@Sql(statements = "DELETE FROM PRODUCT WHERE ID=2",executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	public  void getAllProducts(){
List<Product> productList= restTemplate.getForObject(baseUrl,List.class);
assertEquals(2,productList.size());
//assertEquals(1,testH2Repository.findAll().size());
	}



	@Test
	@Sql(statements = "INSERT INTO PRODUCT (id,name,price,quantity) VALUES(1,'AC',300,2)",executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(statements = "DELETE FROM PRODUCT WHERE ID=1",executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	public void getById(){

		Product product=restTemplate.getForObject(baseUrl+"/{id}",Product.class,1);
//multiple assert statemnt
		assertAll(
				()->assertNotNull(product),
				()->assertEquals(1,product.getId()),
				()->assertEquals("AC",product.getName())


		);

	}


	@Test
	@Sql(statements = "INSERT INTO PRODUCT (id,name,price,quantity) VALUES(1,'AC',300,2)",executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(statements = "DELETE FROM PRODUCT WHERE ID=1",executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	public void getByIdUpdate() {
Product product= new Product("AC",200,2);
restTemplate.put(baseUrl+"/update/{id}",product,1);
Product product1=testH2Repository.findById(1).get();

assertAll(
		()->assertNotNull(product1),
//		()->assertEquals(1,product1.getId()),
		()->assertEquals(200,product1.getPrice(),0)
//		()->assertEquals(200,product1.getPrice())
);
	}

	@Test
	@Sql(statements = "INSERT INTO PRODUCT (id,name,price,quantity) VALUES(1,'AC',300,2)",executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	public  void delete(){
		int s=testH2Repository.findAll().size();
		assertEquals(1,s);
		restTemplate.delete(baseUrl+"/{id}",1);
//		Product p=testH2Repository.findById(1).get();
		assertEquals(0,testH2Repository.findAll().size());

	}











	}
