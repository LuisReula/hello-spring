package com.sinensia.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Generated;
import java.math.BigDecimal;

import static java.math.RoundingMode.HALF_DOWN;

@SpringBootApplication
@RestController
public class DemoProjectApplication { //Proyecto de testeo para una calculadora

	@Generated(value="org.springframework.boot")
	public static void main(String[] args) {
		SpringApplication.run(DemoProjectApplication.class, args);
	}

	//Inicial
	@GetMapping("/")
	public String root(){
		return String.format("Bienvenidos a mi primera prueba");
	}

	//Hello World! en dir: /hello
	@GetMapping("/hello")
	public String hello(@RequestParam(value = "name", defaultValue = "World") String Name){
		return String.format("Hello %s!", Name);
	}

	//Suma en dir: /add
	@GetMapping("/add")
	public Object add( @RequestParam(value="a", defaultValue="0") Float a,
						  @RequestParam(value="b", defaultValue="0") Float b){
		Float sum =a+b;
		Float decimals = sum - sum.intValue();
		if (decimals!=0){
			return sum;
		}
		return  Integer.valueOf(sum.intValue());
	}

	//Producto en dir: /multiply
	@GetMapping("/multiply")
	public Object multiply( @RequestParam(value="a", defaultValue="0") Float a,
							   @RequestParam(value="b", defaultValue="0") Float b){
		Float product =a*b;
		Float decimals = product - product.intValue();
		if (decimals!=0){
			return product;
		}
		return  Integer.valueOf(product.intValue());
	}

	//Resta en dir: /subtraction
	@GetMapping("/subtraction")
	public Object subtraction( @RequestParam(value="a", defaultValue="0") Float a,
							   @RequestParam(value="b", defaultValue="0") Float b){
		Float subt =a-b;
		Float decimals = subt - subt.intValue();
		if (decimals!=0){
			return subt;
		}
		return  Integer.valueOf(subt.intValue());
	}


	@GetMapping("/division")
	public BigDecimal division(@RequestParam(value="a", defaultValue="0") BigDecimal a,
							   @RequestParam(value="b", defaultValue="0") BigDecimal b){
		return a.divide(b, 2, HALF_DOWN);
	}


}
