package com.example.obs.config;

import org.springframework.aot.hint.MemberCategory;
import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;
import org.springframework.web.method.ControllerAdviceBean;

public class WebMvcRuntimeHints implements RuntimeHintsRegistrar {
    
    @Override
    public void registerHints(RuntimeHints hints, ClassLoader classLoader) {
        try {
            Class<?> emptyHandlerClass = Class.forName("org.springframework.web.servlet.handler.AbstractHandlerMethodMapping$EmptyHandler");
            hints.reflection().registerType(
                    emptyHandlerClass, 
                    MemberCategory.INVOKE_DECLARED_METHODS,
                    MemberCategory.INVOKE_PUBLIC_METHODS
            );
            
            hints.reflection().registerType(
                    Class.forName("org.springframework.web.servlet.handler.AbstractHandlerMethodMapping"),
                    MemberCategory.INVOKE_DECLARED_METHODS,
                    MemberCategory.INVOKE_PUBLIC_METHODS
            );
            
            hints.reflection().registerType(
                    ControllerAdviceBean.class,
                    MemberCategory.INVOKE_DECLARED_CONSTRUCTORS,
                    MemberCategory.INVOKE_PUBLIC_CONSTRUCTORS
            );
            
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
