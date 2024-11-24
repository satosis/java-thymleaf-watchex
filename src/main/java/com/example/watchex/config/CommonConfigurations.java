package com.example.watchex.config;

import com.example.watchex.entity.Admin;
import com.example.watchex.entity.User;
import com.example.watchex.utils.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;
import java.util.UUID;

@Configuration
public class CommonConfigurations implements WebMvcConfigurer {
    @Bean
    public LocaleResolver localeResolver() {
        SessionLocaleResolver localeResolver = new SessionLocaleResolver();
        localeResolver.setDefaultLocale(new Locale("vi"));
        return localeResolver;
    }

    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor lci = new LocaleChangeInterceptor();
        lci.setParamName("lang");
        return lci;
    }

    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(localeChangeInterceptor());
    }

    @Bean
    public MessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("lang/message");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }


    @Autowired
    public static String formatPrice(int price, int sale, int numberDecimal) {
        if (price == 0) {
            return "0";
        }
        Double formatPrice = (double) (price - price * (sale / 100));
        if (numberDecimal > 0) {
            return String.format("%,.2f", formatPrice);
        }
        return String.format("%,.0f", formatPrice);
    }

    @Autowired
    public static String formatPriceString(String price, int sale, int numberDecimal) {
        int newPrice = Integer.parseInt(price);
        return formatPrice(newPrice, sale, numberDecimal);
    }
    public static User getCurrentUser() {
        return CommonUtils.getCurrentUser();
    }

    public static Admin getCurrentAdmin() {
        return CommonUtils.getCurrentAdmin();
    }
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        exposeDirectory("uploads", registry);
    }

    private void exposeDirectory(String dirName, ResourceHandlerRegistry registry) {
        Path uploadDir = Paths.get(dirName);
        String uploadPath = uploadDir.toFile().getAbsolutePath();

        if (dirName.startsWith("../")) dirName = dirName.replace("../", "");

        registry.addResourceHandler("/" + dirName + "/**").addResourceLocations("file:/" + uploadPath + "/");
    }

}
