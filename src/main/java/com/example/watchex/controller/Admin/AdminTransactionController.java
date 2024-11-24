package com.example.watchex.controller.Admin;

import com.example.watchex.entity.Order;
import com.example.watchex.entity.Product;
import com.example.watchex.entity.Transaction;
import com.example.watchex.service.OrderService;
import com.example.watchex.service.ProductService;
import com.example.watchex.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Controller
@RequestMapping("/admin/transaction")
public class AdminTransactionController {
    @Autowired
    private MessageSource messageSource;

    @Autowired
    private TransactionService transactionService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private ProductService productService;

    @GetMapping("")
    public String get(Model model, @RequestParam Map<String, String> params) {
        int page = 1;
        if (params.get("page") != null) {
            page = Integer.parseInt(params.get("page"));
        }
        findPaginate(page, model);
        model.addAttribute("id", params.get("id"));
        model.addAttribute("email", params.get("email"));
        model.addAttribute("status", params.get("status"));
        model.addAttribute("title", "Quản lý đơn hàng");
        return "admin/transactions/index";
    }

    private Model findPaginate(int page, Model model) {
        Page<Transaction> transactions = transactionService.get(page);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", transactions.getTotalPages());
        model.addAttribute("totalItems", transactions.getTotalElements());
        model.addAttribute("transactions", transactions);
        model.addAttribute("models", "transaction");
        model.addAttribute("title", "Transactions Management");
        return model;
    }

    @GetMapping("status/{action}/{id}")
    public String changeStatus(@PathVariable("action") String action, @PathVariable("id") Integer id) throws ClassNotFoundException {
        Transaction transaction = transactionService.show(id);
        if (Objects.equals(action, "confirm")) {
            List<Order> orders = orderService.getByTransaction(transaction);

            for (Order order : orders) {
                Product product = productService.show(order.getOd_product_id().getId());
                product.setPro_pay(product.getPro_pay() + 1);
                product.setPro_amount(product.getPro_amount() - order.getOd_qty());
            }
        }
        if (transaction != null) {
            switch (action) {
                case "process":
                    transaction.setTst_status(2);
                    break;
                case "success":
                     transaction.setTst_status(3);
                    break;

                case "cancel":
                     transaction.setTst_status(-1);
                    break;

                case "confirm":
                     transaction.setTst_status(4);
                    break;

                case "waiting_confirmation":
                     transaction.setTst_status(5);
                    break;

                case "confirmed":
                     transaction.setTst_status(6);
                    break;
            }
            transactionService.save(transaction);
        }
        return "redirect:/admin/transaction";
    }


}
