package org.onlineBookstore.services;

import org.onlineBookstore.entities.Basket;
import org.onlineBookstore.entities.Product;
import org.onlineBookstore.entities.User;
import org.onlineBookstore.repo.BasketRepository;
import org.onlineBookstore.repo.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BasketService {

    @Autowired
    private BasketRepository basketRepository;

    @Autowired
    private ProductRepository productRepository;


    public Basket findByUserId(Long userId) {
        Basket basket = basketRepository.findByUserId(userId);
        return basket;
    }

    public List<Product> products(Long id) {
        Basket basket = basketRepository.findById(id).orElseThrow();
        return basket.getProducts();
    }

    public Basket create(User user, List<Long> productIds) {
        Basket basket = new Basket();
        basket.setUser(user);
        basket.setSum(0);
        List<Product> productList = getCollectRefProductsByIds(productIds);
        basket.setProducts(productList);
        return basketRepository.save(basket);
    }

    private List<Product> getCollectRefProductsByIds(List<Long> productIds) {
        return productIds.stream()
                //getOne ытаскивает ссылку на объект, findById - сам объект
                .map(productRepository::getOne)
                .collect(Collectors.toList());
    }

    public void addProduct(Basket basket, List<Long> productIds) {
        List<Product> products = basket.getProducts();
        List<Product> newProductList = products == null ? new ArrayList<>() : new ArrayList<>(products);
        newProductList.addAll(getCollectRefProductsByIds(productIds));
        basket.setProducts(newProductList);
        double sum = 0;
        for(Product product : newProductList){
            sum += product.getPrice();
        }
        basket.setSum(sum);
        basketRepository.save(basket);

    }

    public Basket findById(long basket_id) {
        return basketRepository.findById(basket_id).orElseThrow();
    }

    public void deleteProduct(Basket basket, Product product){
        basket.getProducts().remove(product);
        basketRepository.save(basket);
    }
}
