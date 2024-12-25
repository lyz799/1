package jsu.lyz.teahouse_ks.Controller;

import jsu.lyz.teahouse_ks.Exception.DuplicateProductIdException;
import jsu.lyz.teahouse_ks.Exception.ProductNotFoundException;
import jsu.lyz.teahouse_ks.Model.Product;
import jsu.lyz.teahouse_ks.Service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping("/add")
    public ResponseEntity<?> createProduct(@RequestBody Product product) {
        try {
            Product createdProduct = productService.createProduct(product);
            return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
        } catch (DuplicateProductIdException e) {
            // 返回 JSON 格式的错误消息
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "产品创建失败");
            errorResponse.put("message", e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/getid/{id}")
    public ResponseEntity<?> getProduct(@PathVariable Long id) {
        try {
            Product product = productService.getProduct(id);
            return new ResponseEntity<>(product, HttpStatus.OK); // 成功返回产品信息和 200 状态码
        } catch (ProductNotFoundException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND); // 返回 404 和 JSON 格式错误消息
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Map<String, Object>> updateProduct(@PathVariable Long id, @RequestBody Product product) {
        Map<String, Object> response = new HashMap<>();
        try {
            product.setId(id);
            Product updatedProduct = productService.updateProduct(product);

            // 成功返回
            response.put("message", "更新成功");
            response.put("product", updatedProduct);
            return ResponseEntity.ok(response);

        } catch (ProductNotFoundException e) {
            // ID 不存在
            response.put("message", "ID不存在");
            System.out.println("id不存在");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);

        } catch (IllegalArgumentException e) {
            // 必填字段为空
            response.put("message", "请填写完整信息");
            System.out.println("请填写完整信息");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
        try {
            productService.deleteProduct(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // 删除成功返回 204
        } catch (ProductNotFoundException e) {
            System.out.println("删除失败：id不存在");
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND); // 如果 ID 不存在返回 404
        }
    }

    @GetMapping("/price-range/{minPrice}/{maxPrice}")
    public ResponseEntity<?> getProductsInPriceRange(@PathVariable BigDecimal minPrice, @PathVariable BigDecimal maxPrice) {
        try {
            List<Product> products = productService.getProductsInPriceRange(minPrice, maxPrice);
            return new ResponseEntity<>(products, HttpStatus.OK);
        } catch (ProductNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND); // 如果没有符合条件的产品返回 404
        }
    }


    @GetMapping("/calculate-total/{id}/{quantity}")
    public ResponseEntity<?> calculateTotalPrice(@PathVariable Long id, @PathVariable int quantity) {
        try {
            BigDecimal totalPrice = productService.calculateTotalPrice(id, quantity);
            return new ResponseEntity<>(totalPrice, HttpStatus.OK);
        } catch (ProductNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND); // ID 不存在返回 404
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST); // 其他错误返回 400
        }
    }

}
