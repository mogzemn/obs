package com.example.obs.config;

import com.example.obs.business.responses.AcademicResponse;
import com.example.obs.business.responses.CourseInstructorResponse;
import com.example.obs.core.utilities.mappers.ModelMapperManager;
import com.example.obs.core.utilities.mappers.ModelMapperService;
import com.example.obs.model.entity.Academic;
import com.example.obs.model.entity.CourseInstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.Converter;
import org.modelmapper.AbstractConverter;
import org.modelmapper.Provider;
import org.modelmapper.AbstractProvider;
import org.modelmapper.Conditions;
import org.modelmapper.spi.MappingContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Configuration
public class ModelMapperConfig {

    @Bean("customModelMapper")
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        
        Converter<LocalDateTime, LocalDateTime> dateTimeConverter = new AbstractConverter<LocalDateTime, LocalDateTime>() {
            @Override
            protected LocalDateTime convert(LocalDateTime source) {
                return source == null ? null : LocalDateTime.from(source);
            }
        };

        Provider<LocalDate> localDateProvider = new AbstractProvider<LocalDate>() {
            @Override
            public LocalDate get() {
                return null;
            }
        };
        
        Converter<String, LocalDate> stringToLocalDateConverter = new AbstractConverter<String, LocalDate>() {
            @Override
            protected LocalDate convert(String source) {
                if (source == null || source.trim().isEmpty()) {
                    return null;
                }
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                return LocalDate.parse(source, formatter);
            }
        };
        
        Converter<LocalDate, String> localDateToStringConverter = new AbstractConverter<LocalDate, String>() {
            @Override
            protected String convert(LocalDate source) {
                if (source == null) {
                    return null;
                }
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                return source.format(formatter);
            }
        };
        
        Converter<LocalDate, LocalDate> localDateConverter = new AbstractConverter<LocalDate, LocalDate>() {
            @Override
            protected LocalDate convert(LocalDate source) {
                return source == null ? null : LocalDate.of(source.getYear(), source.getMonth(), source.getDayOfMonth());
            }
        };

        modelMapper.addConverter(dateTimeConverter);
        modelMapper.addConverter(stringToLocalDateConverter);
        modelMapper.addConverter(localDateToStringConverter);
        modelMapper.addConverter(localDateConverter);
        
        modelMapper.getConfiguration().setProvider(localDateProvider);
        
        modelMapper.getConfiguration()
                .setPropertyCondition(context -> context.getSource() != null)
                .setSkipNullEnabled(true);
        
        modelMapper.addMappings(new PropertyMap<Academic, AcademicResponse>() {
            @Override
            protected void configure() {
                using(dateTimeConverter).map(source.getCreatedAt(), destination.getCreatedAt());
                using(dateTimeConverter).map(source.getUpdatedAt(), destination.getUpdatedAt());
            }
        });
        
        modelMapper.addMappings(new PropertyMap<CourseInstructor, CourseInstructorResponse>() {
            @Override
            protected void configure() {
                using(dateTimeConverter).map(source.getCreatedAt(), destination.getCreatedAt());
                using(dateTimeConverter).map(source.getUpdatedAt(), destination.getUpdatedAt());
                map().setAcademic(null);
            }
        });
        
        return modelMapper;
    }
    
    @Bean("customModelMapperService")
    @Primary
    public ModelMapperService modelMapperService(ModelMapper modelMapper) {
        return new ModelMapperManager(modelMapper);
    }
}
