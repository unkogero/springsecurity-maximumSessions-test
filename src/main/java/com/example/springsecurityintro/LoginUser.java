package com.example.springsecurityintro;

import java.io.Serializable;
import java.util.List;

public record LoginUser(String email, String name, String password, List<String> roleList) implements Serializable {
}
