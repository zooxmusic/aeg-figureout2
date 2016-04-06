package com.aeg.config;



import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

public class EnvironmentValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return false;
    }

    @Override
    public void validate(Object target, Errors errors) {
        ValidationUtils.rejectIfEmpty(errors, "default.hosts", "default.host.empty");
        ValidationUtils.rejectIfEmpty(errors, "default.ports", "default.port.empty");
        ValidationUtils.rejectIfEmpty(errors, "default.username", "default.username.empty");
        ValidationUtils.rejectIfEmpty(errors, "default.password", "default.password.empty");
        ValidationUtils.rejectIfEmpty(errors, "AEG_HOME", "aeg.home.empty");
    }
}
