package com.github.hcsp.multithread;

import java.util.Optional;
import java.util.concurrent.Semaphore;

public class Container6  {
    Semaphore semaphore;
    private Optional<Integer> value=Optional.empty();

    public Container6(Semaphore semaphore) {
        this.semaphore = semaphore;
    }

    public Optional<Integer> getValue(){
        return value;
    }

    public void setValue(Optional<Integer> container){
            this.value = container;
    }

}
