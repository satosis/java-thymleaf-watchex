package com.example.watchex.controller;

import com.example.watchex.config.CommonConfigurations;
import com.example.watchex.dto.StoreTransactionDto;
import com.example.watchex.entity.Cart;
import com.example.watchex.entity.Order;
import com.example.watchex.entity.Transaction;
import com.example.watchex.entity.User;
import com.example.watchex.service.OrderService;
import com.example.watchex.service.TransactionService;
import com.example.watchex.utils.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequestMapping("/transaction")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private OrderService orderService;

    @PostMapping("/store")
    public String store(@Valid @ModelAttribute("StoreTransactionDto") StoreTransactionDto storeTransactionDto, Model model) {
        Integer total = 0;
        for (int i = 0 ;i < Cart.cart.size(); i++) {
            total += Integer.parseInt(String.valueOf((Cart.cart.get(i).product.getPro_price() - Cart.cart.get(i).product.getPro_price() * (Cart.cart.get(i).product.getPro_sale() / 100)) * Cart.cart.get(i).quantity));
        }
        User user = CommonUtils.getCurrentUser();
        Transaction transaction = new Transaction();
        transaction.setTst_name(storeTransactionDto.getName());
        transaction.setTst_email(storeTransactionDto.getEmail());
        transaction.setTst_phone(storeTransactionDto.getPhone());
        transaction.setTst_address(storeTransactionDto.getAddress());
        transaction.setTst_note(storeTransactionDto.getNote());
        transaction.setTst_code("PAYID-" + CommonUtils.randomString(10).toUpperCase());
        transaction.setTst_status(5);
        transaction.setTst_type(storeTransactionDto.getType());
        transaction.setTst_user_id(user);
        transaction.setTst_total_money(String.valueOf(total));
        transactionService.save(transaction);

        for (int i = 0; i < Cart.cart.size(); i++) {
            Order order = new Order();
            order.setOd_transaction_id(transaction);
            order.setOd_product_id(Cart.cart.get(i).product);
            order.setOd_price(Cart.cart.get(i).product.getPro_price());
            order.setOd_sale(Cart.cart.get(i).product.getPro_sale());
            order.setOd_qty(Cart.cart.get(i).quantity);
            orderService.save(order);
            Cart.cart.remove(Cart.cart.get(i));
        }
        return "redirect:/";
    }

}
