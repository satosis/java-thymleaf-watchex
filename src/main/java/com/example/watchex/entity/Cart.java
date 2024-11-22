package com.example.watchex.entity;

import java.util.ArrayList;
import java.util.List;

public class Cart {
    public static List<CartItem> cart;
    static {
        cart = new ArrayList<CartItem>();
    }

}
