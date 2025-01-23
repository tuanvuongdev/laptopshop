package vn.hoidanit.laptopshop.service.specification;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import vn.hoidanit.laptopshop.domain.Product;
import vn.hoidanit.laptopshop.domain.Product_;

public class ProductSpecs {

    public static Specification<Product> nameLike(String name) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get(Product_.NAME), "%" + name + "%");
    }

    public static Specification<Product> minPrice(double price) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.ge(root.get(Product_.PRICE), price);
    }

    public static Specification<Product> maxPrice(double price) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.le(root.get(Product_.PRICE), price);
    }

    public static Specification<Product> matchFactory(String factory) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(Product_.FACTORY), factory);
    }

    public static Specification<Product> matchListFactory(List<String> factory) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.in(root.get(Product_.FACTORY)).value(factory);
    }

    public static Specification<Product> priceInRange(
            String minPrice1,
            String maxPrice1,
            String minPrice2,
            String maxPrice2) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.or(
                criteriaBuilder.and(
                        criteriaBuilder.greaterThanOrEqualTo(
                                root.get(Product_.PRICE),
                                minPrice1),
                        criteriaBuilder.lessThanOrEqualTo(
                                root.get(Product_.PRICE),
                                maxPrice1)),
                criteriaBuilder.and(
                        criteriaBuilder.greaterThanOrEqualTo(
                                root.get(Product_.PRICE),
                                minPrice2),
                        criteriaBuilder.lessThanOrEqualTo(
                                root.get(Product_.PRICE),
                                maxPrice2)));
    }

}
