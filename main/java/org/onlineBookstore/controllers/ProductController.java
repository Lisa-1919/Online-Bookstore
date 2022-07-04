package org.onlineBookstore.controllers;

import org.onlineBookstore.entities.Product;
import org.onlineBookstore.repo.ProductRepository;
import org.onlineBookstore.services.ProductService;
import org.onlineBookstore.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

@Controller
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    @Value("${upload.path}")
    private String uploadPath;

    public ProductController() {
    }

    @GetMapping("/store/catalog")
    public String catalog(Model model){
        Iterable<Product> products = productRepository.findAll();
        model.addAttribute("products", products);
        return "catalog";
    }

    @GetMapping("/admin/product")
    public String product(Model model) {
        Iterable<Product> products = productRepository.findAll();
        model.addAttribute("products", products);
        return "products";
    }

    @GetMapping("/admin/product/add")
    public String productAdd(Model model) {
        return "add_product";
    }

    @PostMapping("/admin/product/add")
    public String addProduct(@RequestParam String title, @RequestParam String author,
                             @RequestParam String genre, @RequestParam String publisher,
                             @RequestParam String description, @RequestParam double price,
                             @RequestParam(value = "file") MultipartFile file, Model model) throws IOException {
        Product product = new Product(title, price, author, genre, publisher);
        if (file != null) {

            File uploadFolder = new File(uploadPath);

            if (!uploadFolder.exists()) {
                uploadFolder.mkdir();
            }

            String resultFileName = UUID.randomUUID() + "." + file.getOriginalFilename();

            file.transferTo(new File(uploadPath + "/" + resultFileName));

            product.setImgName(resultFileName);
        }
        if (description != null) {
            product.setDescription(description);
        }
        productRepository.save(product);
        return "redirect:/admin/product";
    }

    @GetMapping("/admin/product/{id}/edit")
    public String productEdit(@PathVariable(value = "id") long id, Model model) {
        if (!productRepository.existsById(id)) {
            return "redirect:/admin/product";
        }
        Optional<Product> product = productRepository.findById(id);
        ArrayList<Product> res = new ArrayList<>();
        product.ifPresent(res::add);
        model.addAttribute("products", res);
        return "edit_product";
    }

    @PostMapping("/admin/product/{id}/edit")
    public String editProduct(@PathVariable(value = "id") long id, @RequestParam double price, @RequestParam String description, Model model) {
        Product product = productRepository.findById(id).orElseThrow();
        product.setPrice(price);
        product.setDescription(description);
        productRepository.save(product);
        return "redirect:/admin/product";
    }

    @GetMapping("/admin/product/{id}/delete")
    public String deleteProduct(@PathVariable(value = "id") long id, Model model) {
        productService.delete(id);
        return "redirect:/admin/product";
    }

    @GetMapping("/user/product")
    public String productView(Model model) {
        Iterable<Product> products = productRepository.findAll();
        model.addAttribute("products", products);
        return "redirect:/store/catalog";
    }

    @PostMapping("/store/search")
    public String productSearch(@RequestParam(value = "quest") String quest, Model model) {
        model.addAttribute("products", productService.search(quest));
        return "catalog";
    }

    @PostMapping("/store/sorting")
    public String productSort(@Valid String sorting, Model model) {
        model.addAttribute("products", productService.sort(sorting));
        return "catalog";
    }

    @PostMapping("/store/filter")
    public String productFilter(@RequestParam(value="min") double min, @RequestParam(value="max") double max, Model model){
        model.addAttribute("products", productService.filter(min, max));
        return "catalog";
    }

    @GetMapping("/store/genres/{genre}")
    public String categories(@PathVariable(value = "genre") String genre, Model model) {
        model.addAttribute("products", productService.findByGenre("genre"));
        return "catalog";
    }

    @PostMapping("/user/basket/add/{id}")
    public String addProductToBasket(@PathVariable(value="id") long id, Model model){
        productService.addToBasket(id, userService.getAuthorizedUser().getId());
        return "redirect:/store/catalog";
    }

    @GetMapping("/store/product/{id}")
    public String viewProduct(@PathVariable(value = "id") long id, Model model){
        Product product = productRepository.findById(id).orElseThrow();
        model.addAttribute("product", product);
        return "product_page";
    }
}
