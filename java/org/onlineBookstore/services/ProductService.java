package org.onlineBookstore.services;

import org.onlineBookstore.entities.Basket;
import org.onlineBookstore.entities.Product;
import org.onlineBookstore.entities.User;
import org.onlineBookstore.repo.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderService orderService;

    @Autowired
    private BasketService basketService;

    @Autowired
    private UserService userService;

    public List<Product> search(String quest) {
        List<Product> products = new ArrayList<>();
        for (Product product : productRepository.findAll()) {
            if (product.getTitle().contains(quest) || product.getPublisher().contains(quest) || product.getGenre().contains(quest)) {
                products.add(product);
            }
        }
        return products;
    }

    public List<Product> sort(String sorting) {
        List<Product> products;
        if (sorting.equals("price_from_max_to_min")) {
            products = productRepository.findAll().stream().sorted(Comparator.comparing(Product::getPrice).reversed())
                    .toList();
        } else if (sorting.equals("price_from_min_to_max")) {
            products = productRepository.findAll().stream().sorted(Comparator.comparing(Product::getPrice)).toList();
        } else if (sorting.equals("title")) {
            products = productRepository.findAll().stream().sorted(Comparator.comparing(Product::getTitle)).toList();
            ;
        } else if (sorting.equals("author")) {
            products = productRepository.findAll().stream().sorted(Comparator.comparing(Product::getAuthor)).toList();
            ;
        } else {
            products = productRepository.findAll();
        }
        return products;
    }

    public List<Product> filter(double min, double max) {
        List<Product> products = new ArrayList<>();
        for (Product product : productRepository.findAll()) {
            if (product.getPrice() >= min && product.getPrice() <= max) {
                products.add(product);
            }
        }
        return products;
    }

    public void addToBasket(Long productId, Long userId) {
        User user = userService.findById(userId);
        Basket basket = user.getBasket();
        if (basket == null) {
            Basket newBasket = basketService.create(user, Collections.singletonList(productId));
            user.setBasket(newBasket);
            userService.saveUser(user);
        } else {
            basketService.addProduct(basket, Collections.singletonList(productId));
        }
    }

    public List<Product> findByGenre(String genre) {
        String genreBook = "";
        if (genre.equals("foreign")) {
            genreBook = "Зарубежная классика";
        } else if (genre.equals("detective")) {
            genreBook = "Детектив";
        } else if (genre.equals("psychology")) {
            genreBook = "Психология";
        } else if (genre.equals("fantasy")) {
            genreBook = "Фэнтези";
        } else if (genre.equals("fantastic")) {
            genreBook = "Фантастика";
        } else if (genre.equals("roman")) {
            genreBook = "Роман";
        } else if (genre.equals("history")) {
            genreBook = "Исторические";
        } else if (genre.equals("science")) {
            genreBook = "Наука";
        } else {
            genreBook = "Детские";
        }
        List<Product> products = new ArrayList<>();
        for (Product product : productRepository.findAll()) {
            if (product.getGenre().equals(genreBook)) {
                products.add(product);
            }
        }
        return products;
    }

    public void delete(Long id) {
        Product product = productRepository.findById(id).orElseThrow();
        productRepository.delete(product);
    }
}
