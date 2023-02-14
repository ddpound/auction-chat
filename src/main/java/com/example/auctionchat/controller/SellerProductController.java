package com.example.auctionchat.controller;

import com.ctc.wstx.dtd.ModelNode;
import com.example.auctionchat.mongomodel.ProductModel;
import com.example.auctionchat.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Log4j2
@RequestMapping(value = "seller/product")
@RestController
public class SellerProductController {

    private final ProductService productService;


    @PostMapping("save")
    public Mono<ProductModel> saveProduct(@RequestBody ProductModel productModel){


        return productService.saveProduct(productModel);
    }

}
