package com.example.task.exception;

public class ItemNotFoundException extends RuntimeException {
    public ItemNotFoundException() {
         super("Item not found");
    }

}
