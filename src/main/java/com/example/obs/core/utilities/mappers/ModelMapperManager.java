package com.example.obs.core.utilities.mappers;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Service;

@Service
public class ModelMapperManager implements ModelMapperService{

    private final ModelMapper modelMapper;

    public ModelMapperManager(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
        
        this.modelMapper.getConfiguration()
                .setAmbiguityIgnored(true)
                .setMatchingStrategy(MatchingStrategies.STANDARD)
                .setPropertyCondition(context -> context.getSource() != null)
                .setSkipNullEnabled(false)
                .setDeepCopyEnabled(true)
                .setFieldMatchingEnabled(true);
    }

    @Override
    public ModelMapper forResponse() {
        this.modelMapper.getConfiguration()
                .setAmbiguityIgnored(true)
                .setMatchingStrategy(MatchingStrategies.LOOSE)
                .setSkipNullEnabled(false)
                .setFieldMatchingEnabled(true)
                .setDeepCopyEnabled(true)
                .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE);
        
        return this.modelMapper;
    }

    @Override
    public ModelMapper forRequest() {
        this.modelMapper.getConfiguration()
                .setAmbiguityIgnored(true)
                .setMatchingStrategy(MatchingStrategies.STANDARD)
                .setSkipNullEnabled(true)
                .setFieldMatchingEnabled(true)
                .setDeepCopyEnabled(true);
        return this.modelMapper;
    }
}
