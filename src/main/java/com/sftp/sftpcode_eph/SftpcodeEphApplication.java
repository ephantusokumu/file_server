package com.sftp.sftpcode_eph;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication

@EnableCaching
public class SftpcodeEphApplication {

	public static void main(String[] args) {
		SpringApplication.run(SftpcodeEphApplication.class, args);
	}

}
