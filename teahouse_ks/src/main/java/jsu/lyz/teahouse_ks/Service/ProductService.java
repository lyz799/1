package jsu.lyz.teahouse_ks.Service;

import jsu.lyz.teahouse_ks.Exception.DuplicateProductIdException;
import jsu.lyz.teahouse_ks.Exception.ProductNotFoundException;
import jsu.lyz.teahouse_ks.Model.Product;
import jsu.lyz.teahouse_ks.Repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public Product createProduct(Product product) {
        // 检查是否存在相同 ID 的产品
        if (productRepository.existsById(product.getId())) {
            throw new DuplicateProductIdException("产品ID " + product.getId() + " 已存在");
        }
        System.out.println(product+"创建成功");
        return productRepository.save(product);
    }

    public void deleteProduct(Long productId) {
        // 检查是否存在指定 ID 的产品
        if (!productRepository.existsById(productId)) {
            throw new ProductNotFoundException("产品ID " + productId + " 不存在");
        }
        productRepository.deleteById(productId);
        System.out.println(productId+"号商品删除成功");
    }

    public Product updateProduct(Product product) {
        // 检查必填字段是否为空
        if (product.getName() == null || product.getName().isEmpty()) {
            throw new IllegalArgumentException("名称不能为空");
        }
        if (product.getCategory() == null || product.getCategory().isEmpty()) {
            throw new IllegalArgumentException("分类不能为空");
        }
        if (product.getPrice() == null) {
            throw new IllegalArgumentException("价格不能为空");
        }
        if (product.getStock() == null) {
            throw new IllegalArgumentException("库存不能为空");
        }

        if (!productRepository.existsById(product.getId())) {
            throw new ProductNotFoundException("产品ID " + product.getId() + " 不存在");
        }
        product.setEntryDate(LocalDateTime.now()); // 更新为当前时间
        System.out.println("更新成功: " + product);
        return productRepository.save(product);
    }


    public Product getProduct(Long productId) {
        if (!productRepository.existsById(productId)) {
            throw new ProductNotFoundException("产品ID " + productId + " 不存在");
        }
        System.out.println("查找"+productId);
        return productRepository.findById(productId).orElse(null);
    }

    public List<Product> getProductsInPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        System.out.println("价格范围：" + minPrice + " 到 " + maxPrice);

        // 查询符合价格范围的产品
        List<Product> products = productRepository.findByPriceBetween(minPrice, maxPrice);

        // 检查是否有产品
        if (products.isEmpty()) {
            throw new ProductNotFoundException("没有符合条件的产品");
        }

        // 检查库存是否为 0
        List<Product> productsWithStock = products.stream()
                .filter(product -> product.getStock() > 0) // 假设 Product 类有 `getStock` 方法
                .toList();

        if (productsWithStock.isEmpty()) {
            throw new ProductNotFoundException("没有符合条件的产品，库存全部为 0");
        }

        return productsWithStock;
    }

    public BigDecimal calculateTotalPrice(Long productId, int quantity) {
        System.out.println("计算总价，产品ID：" + productId);

        // 检查产品是否存在
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> {
                    System.out.println("产品ID不存在：" + productId);
                    return new ProductNotFoundException("产品ID " + productId + " 不存在");
                });

        // 验证价格
        if (product.getPrice() == null) {
            throw new RuntimeException("产品未设置价格： " + productId);
        }

        // 验证数量
        if (quantity <= 0) {
            throw new RuntimeException("数量必须大于0");
        }

        // 计算总价
        return product.getPrice().multiply(BigDecimal.valueOf(quantity));
    }

}

