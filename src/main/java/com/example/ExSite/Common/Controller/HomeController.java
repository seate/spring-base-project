package com.example.ExSite.Common.Controller;
import jakarta.servlet.http.HttpSession;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.IOException;
import java.net.http.HttpClient;

@Controller
public class HomeController {

    private final HttpSession httpSession;

    @Autowired
    public HomeController(HttpSession httpSession) {
        this.httpSession = httpSession;
    }

    @GetMapping(value = {"", "/"})
    public String home(Model model) {




        return "home";
    }


}