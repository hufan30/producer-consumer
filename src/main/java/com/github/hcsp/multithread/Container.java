package com.github.hcsp.multithread;

import java.util.Optional;

public class Container {
    Optional<Integer> product = Optional.empty();

    public Optional<Integer> getProduct() {
        return product;
    }

    public void setProduct(Optional<Integer> product) {
        this.product = product;
    }
}
