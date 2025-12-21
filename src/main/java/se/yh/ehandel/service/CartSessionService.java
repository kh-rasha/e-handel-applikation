package se.yh.ehandel.service;

import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class CartSessionService {

    private String selectedCustomerEmail;
    private final Map<String, Integer> cart = new LinkedHashMap<>();

    public void selectCustomer(String email){
        this.selectedCustomerEmail = email;
        cart.clear();
    }
    public  String getSelectedCustomerEmail(){
        return selectedCustomerEmail;
    }

    public void add (String sku, int qty){
        if (qty <= 0) throw new IllegalArgumentException("qty must be > 0");
        cart.merge(sku, qty, Integer::sum);
    }
    public void remove(String sku) {
        cart.remove(sku);
    }
    public Map<String, Integer> view() {
        return Collections.unmodifiableMap(cart);
    }

    public boolean isEmpty() {
        return cart.isEmpty();
    }

    public void clear() {
        cart.clear();
    }

}
