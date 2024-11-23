package com.example.watchex.controller.Admin;

import com.example.watchex.dto.ProductDto;
import com.example.watchex.dto.TransactionRevenueDto;
import com.example.watchex.entity.Transaction;
import com.example.watchex.service.ProductService;
import com.example.watchex.service.RatingService;
import com.example.watchex.service.TransactionService;
import com.example.watchex.service.UserService;
import com.example.watchex.utils.CommonUtils;
import com.google.gson.Gson;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.google.gson.JsonObject;

import java.util.*;

@Controller
public class AdminHomeController {
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private UserService userService;
    @Autowired
    private RatingService ratingService;
    @Autowired
    private ProductService productService;
    @GetMapping("/admin")
    public String showHomepage(Model model) {
        Gson gson = new Gson();
        int totalTransaction = transactionService.getAll().size();
        int totalUser = userService.getAll().size();
        int totalRating = ratingService.getAll().size();
        int totalProduct = productService.getActive().size();

        Map<String, String> params = new HashMap<>();
        params.put("pageSize", "5");
        params.put("page", "1");
        Page<ProductDto> hotProduct =  productService.get(params);
        //thống kê trạng thái đơn hàng
        int transactionProcess = transactionService.getByStatus(2).size();
        int transactionShipped = transactionService.getByStatus(3).size();
        int transactionFinish = transactionService.getByStatus(4).size();
        int transactionConfirm = transactionService.getByStatus(5).size();
        int transactionConfirmed = transactionService.getByStatus(6).size();
        int transactionCanCel = transactionService.getByStatus(-1).size();

        List<Object> target = new LinkedList<>();
        target.add("Đang vận chuyển");
        target.add(transactionProcess);
        target.add(false);
        List<List<Object>> statusTransaction = Lists.newArrayList();
        statusTransaction.add(target);

        List<Object> target1 = new LinkedList<>();
        target1.add("Đã giao hàng");
        target1.add(transactionShipped);
        target1.add(false);
        statusTransaction.add(target1);

        List<Object> target2 = new LinkedList<>();
        target2.add("Đã hủy");
        target2.add(transactionCanCel);
        target2.add(false);
        statusTransaction.add(target2);

        List<Object> target3 = new LinkedList<>();
        target3.add("Hoàn thành");
        target3.add(transactionFinish);
        target3.add(false);
        statusTransaction.add(target3);

        List<Object> target4 = new LinkedList<>();
        target4.add("Chờ xác nhận");
        target4.add(transactionConfirm);
        target4.add(false);
        statusTransaction.add(target4);

        List<Object> target5 = new LinkedList<>();
        target5.add("Đã xác nhận");
        target5.add(transactionConfirmed);
        target5.add(false);
        statusTransaction.add(target5);

        ArrayList<String> listDay = CommonUtils.getListDayAndMonth();
        //doanh thu theo tháng đã xử lý
        List<TransactionRevenueDto> revenueTransactionMonthDefault = transactionService.getListRevenueOfMonthByStatus(4);
        //doanh thu theo tháng chưa xử lý
        List<TransactionRevenueDto> revenueTransactionMonth = transactionService.getListRevenueOfMonthByStatus(5);

        ArrayList<Integer> arrRevenueTransactionMonth = new ArrayList<>();
        ArrayList<Integer> arrRevenueTransactionMonthDefault = new ArrayList<>();

        for (String day : listDay) {
            Integer total = 0;
            for (TransactionRevenueDto revenue : revenueTransactionMonthDefault) {
                if (Objects.equals(String.valueOf(revenue.getDay()), day)) {
                    total = revenue.getTotalMoney();
                    break;
                }
            }
            arrRevenueTransactionMonth.add(total);
            total = 0;
            for (TransactionRevenueDto revenue : revenueTransactionMonth) {
                if (Objects.equals(String.valueOf(revenue.getDay()), day)) {
                    total = revenue.getTotalMoney();
                    break;
                }
            }
            arrRevenueTransactionMonthDefault.add(total);
        }

        model.addAttribute("title", "Trang quản trị");
        model.addAttribute("totalTransaction", totalTransaction);
        model.addAttribute("totalUser", totalUser);
        model.addAttribute("totalRating", totalRating);
        model.addAttribute("totalProduct", totalProduct);
        model.addAttribute("hotProduct", hotProduct);
        model.addAttribute("transactionProcess", transactionProcess);
        model.addAttribute("transactionShipped", transactionShipped);
        model.addAttribute("transactionFinish", transactionFinish);
        model.addAttribute("transactionConfirm", transactionConfirm);
        model.addAttribute("transactionConfirmed", transactionConfirmed);
        model.addAttribute("transactionCanCel", transactionCanCel);
        model.addAttribute("listDay", gson.toJson(listDay));
        model.addAttribute("statusTransaction", gson.toJson(statusTransaction));
        model.addAttribute("arrRevenueTransactionMonth", gson.toJson(arrRevenueTransactionMonth));
        model.addAttribute("arrRevenueTransactionMonthDefault", gson.toJson(arrRevenueTransactionMonthDefault));
        return "admin/index";
    }
}
