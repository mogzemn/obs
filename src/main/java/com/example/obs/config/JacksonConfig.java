package com.example.obs.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Configuration
public class JacksonConfig {

    @Bean
    @Primary
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        
        SimpleModule customModule = new SimpleModule();
        
        customModule.addDeserializer(LocalTime.class, new JsonDeserializer<LocalTime>() {
            @Override
            public LocalTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                String value = p.getValueAsString();
                
                if (value == null || value.isEmpty()) {
                    return null;
                }
                
                try {
                    return LocalTime.parse(value);
                } catch (DateTimeParseException e) {
                    try {
                        if (value.contains("-")) {
                            return LocalTime.of(0, 0);
                        }
                    } catch (Exception ex) {
                    }
                    throw new IllegalArgumentException("Cannot deserialize LocalTime from: " + value);
                }
            }
        });
        
        objectMapper.registerModule(javaTimeModule);
        objectMapper.registerModule(customModule);
        
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        
        return objectMapper;
    }
}
