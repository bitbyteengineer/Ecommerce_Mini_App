package com.shop.app;

import com.shop.dao.ProductDao;
import com.shop.model.Product;
import java.util.List;

public class ProductTest {
    public static void main(String[] args) throws Exception {
        ProductDao dao = new ProductDao();
        List<Product> products = dao.getAllProductsSortedByPriceAsc();

        System.out.printf("%-6s %-22s %-10s %-6s%n", "ID", "NAME", "PRICE", "QTY");
        System.out.printf("%-6s %-22s %-10s %-6s%n", "------", "----------------------", "----------", "------");

        for (Product p : products) {
            System.out.printf("%-6d %-22s %-10.2f %-6d%n",
                    p.getProductId(),
                    p.getName(),
                    p.getPrice(),
                    p.getQuantity());
        }

    }
}
