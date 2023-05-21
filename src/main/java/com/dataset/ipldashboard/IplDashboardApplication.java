package com.dataset.ipldashboard;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//@EnableBatchProcessing donot enable as it will overwrite auto configure and batch will not be invoked
public class IplDashboardApplication {

    public static void main(String[] args) {
        SpringApplication.run(IplDashboardApplication.class, args);
        System.out.println("Wohoo!! application succesfully ran");
    }

}
