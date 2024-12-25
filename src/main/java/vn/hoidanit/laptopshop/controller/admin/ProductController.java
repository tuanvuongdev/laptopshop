package vn.hoidanit.laptopshop.controller.admin;

import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;
import vn.hoidanit.laptopshop.domain.Product;
import vn.hoidanit.laptopshop.service.ProductService;
import vn.hoidanit.laptopshop.service.UploadService;

@Controller
public class ProductController {

    private final ProductService productService;
    private final UploadService uploadService;

    public ProductController(ProductService productService, UploadService uploadService) {
        this.productService = productService;
        this.uploadService = uploadService;
    }

    @GetMapping("/admin/product")
    public String getProduct(Model model) {
        model.addAttribute("products", this.productService.fetchProducts());
        return "admin/product/show";
    }

    @GetMapping("/admin/product/{id}")
    public String getDetailProductPage(Model model, @PathVariable long id) {
        Product curProd = this.productService.fetchProductById(id).get();
        model.addAttribute("product", curProd);
        return "admin/product/detail";
    }

    // Create
    @GetMapping("/admin/product/create")
    public String getProductCreatePage(Model model) {
        model.addAttribute("newProduct", new Product());
        return "admin/product/create";
    }

    @PostMapping("/admin/product/create")
    public String postCreateProduct(Model model,
            @ModelAttribute("newProduct") @Valid Product product,
            BindingResult newProductBindingResult,
            @RequestParam("hoidanitFile") MultipartFile file) {

        if (newProductBindingResult.hasErrors()) {
            return "admin/product/create";
        }

        String image = this.uploadService.handleSaveUploadFile(file, "product");
        product.setImage(image);
        this.productService.saveProduct(product);
        return "redirect:/admin/product";
    }

    // Update
    @GetMapping("/admin/product/update/{productId}")
    public String getProductUpdatePage(Model model, @PathVariable long productId) {
        Optional<Product> currentProd = this.productService.fetchProductById(productId);
        model.addAttribute("newProduct", currentProd.get());
        return "admin/product/update";
    }

    @PostMapping("/admin/product/update")
    public String getProductUpdatePage(
            @ModelAttribute("newProduct") Product product,
            BindingResult newProductBindingResult,
            @RequestParam("hoidanitFile") MultipartFile file) {

        if (newProductBindingResult.hasErrors()) {
            return "admin/product/update";
        }

        Product currentProd = this.productService.fetchProductById(product.getId()).get();
        if (currentProd != null) {
            if (!file.isEmpty()) {
                String img = this.uploadService.handleSaveUploadFile(file, "product");
                currentProd.setImage(img);
            }
        }

        currentProd.setName(product.getName());
        currentProd.setPrice(product.getPrice());
        currentProd.setQuantity(product.getQuantity());
        currentProd.setDetailDesc(product.getDetailDesc());
        currentProd.setShortDesc(product.getShortDesc());
        currentProd.setFactory(product.getFactory());
        currentProd.setTarget(product.getTarget());

        this.productService.saveProduct(product);
        return "redirect:/admin/product";
    }

    // Delete
    @GetMapping("/admin/product/delete/{productId}")
    public String getProductDeletePage(Model model, @PathVariable long productId) {
        Product prod = new Product();
        prod.setId(productId);
        model.addAttribute("newProduct", prod);
        model.addAttribute("id", productId);
        return "admin/product/delete";
    }

    @PostMapping("/admin/product/delete")
    public String getProductDeletePage(@ModelAttribute("newProduct") Product product) {
        this.productService.deleteProductById(product.getId());
        return "redirect:/admin/product";
    }
}
