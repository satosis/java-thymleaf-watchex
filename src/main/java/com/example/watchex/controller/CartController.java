package com.example.watchex.controller;

import com.example.watchex.config.CommonConfigurations;
import com.example.watchex.entity.Cart;
import com.example.watchex.entity.CartItem;
import com.example.watchex.entity.Category;
import com.example.watchex.entity.Product;
import com.example.watchex.exceptions.MessageEntity;
import com.example.watchex.response.UpdateCartResponse;
import com.example.watchex.service.CategoryService;
import com.example.watchex.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductService productService;

    @GetMapping("")
    public String index(Model model) {
        List<Category> categories = categoryService.getAll();
        Integer total = 0;
        for (int i = 0 ;i < Cart.cart.size(); i++) {
            total += Integer.parseInt(String.valueOf((Cart.cart.get(i).product.getPro_price() - Cart.cart.get(i).product.getPro_price() * (Cart.cart.get(i).product.getPro_sale() / 100)) * Cart.cart.get(i).quantity));
        }
        model.addAttribute("categories", categories);
        model.addAttribute("cartCount", Cart.cart.size());
        model.addAttribute("shopping", Cart.cart);
        model.addAttribute("total", total);
        return "cart/index";
    }

    @GetMapping("/add/{id}")
    public String add(@PathVariable("id") String id, @NonNull HttpServletRequest request) throws ClassNotFoundException {
        Product product = productService.show(Integer.parseInt(id));
        if (product.getPro_amount() == 0) {
            return id;
        }
        Integer isUpdate = 0;
        for (int i = 0; i < Cart.cart.size(); i++) {
            if (Cart.cart.get(i).product.getId() == Integer.parseInt(id)) {
                Cart.cart.get(i).quantity = Cart.cart.get(i).quantity + 1;
                isUpdate = 1;
            }
        }
        if (isUpdate == 0) {
            CartItem item = new CartItem();
            item.setProduct(product);
            item.setQuantity(1);
            Cart.cart.add(item);
        }
        return "redirect:" + request.getHeader("Referer");
    }

    @GetMapping("/update/{id}/{quantity}")
    public ResponseEntity<MessageEntity> update(@PathVariable("id") String id, @PathVariable("quantity") String quantity, @NonNull HttpServletRequest request) throws ClassNotFoundException {
        Product product = productService.show(Integer.parseInt(id));
        if (product.getPro_amount() < Integer.parseInt(quantity)) {
            return new ResponseEntity("Số lượng hàng không đủ ", HttpStatus.BAD_REQUEST);
        }
        for (int i = 0; i < Cart.cart.size(); i++) {
            if (Cart.cart.get(i).product.getId() == Integer.parseInt(id)) {
                Cart.cart.get(i).quantity = Integer.parseInt(quantity);
            } else {
                CartItem item = new CartItem();
                item.setProduct(product);
                item.setQuantity(1);
                Cart.cart.add(item);
            }
        }

        Integer total = 0;
        for (int i = 0 ;i < Cart.cart.size(); i++) {
            total += Integer.parseInt(String.valueOf((Cart.cart.get(i).product.getPro_price() - Cart.cart.get(i).product.getPro_price() * (Cart.cart.get(i).product.getPro_sale() / 100)) * Cart.cart.get(i).quantity));
        }
        UpdateCartResponse response = UpdateCartResponse.builder()
                .message("Cập nhật thành công")
                .totalMoney(CommonConfigurations.formatPrice(total, 0, 0))
                .totalItem(CommonConfigurations.formatPrice((product.getPro_price() - product.getPro_price() * (product.getPro_sale() / 100)) * Integer.parseInt(quantity), 0, 0))
                .number(Cart.cart.size())
                .build();
        return ResponseEntity.ok(new MessageEntity(200, response));
    }

    @GetMapping("/remove/{id}")
    public String remove(@PathVariable("id") String id, @NonNull HttpServletRequest request) throws ClassNotFoundException {
        for (int i = 0; i < Cart.cart.size(); i++) {
            if (Cart.cart.get(i).product.getId() == Integer.parseInt(id)) {
                Cart.cart.remove(Cart.cart.get(i));
            }
        }
        return "redirect:" + request.getHeader("Referer");
    }
}
