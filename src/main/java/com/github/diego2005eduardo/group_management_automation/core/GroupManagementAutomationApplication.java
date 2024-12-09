package com.github.diego2005eduardo.group_management_automation.core;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {
		"com.github.diego2005eduardo.group_management_automation.features.group",
		"com.github.diego2005eduardo.group_management_automation.core"
})
public class GroupManagementAutomationApplication {
	public static void main(String[] args) {
		SpringApplication.run(GroupManagementAutomationApplication.class, args);
	}
}
