package com.github.unldenis.helper.util;

import lombok.NonNull;
import org.apache.commons.lang.reflect.ConstructorUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

public class ReflectionUtil {

    public static @NonNull <T> T getValue(@NonNull Object object, @NonNull String fieldName) throws NoSuchFieldException, IllegalAccessException {
        Field field = object.getClass().getDeclaredField(fieldName);
        return getValue(object, field);
    }

    public static @NonNull <T> T getValue(@NonNull Object object, @NonNull Field field) throws IllegalAccessException {
        field.setAccessible(true);
        T value = (T) field.get(object);
        field.setAccessible(false);
        return value;
    }

    public static @NonNull <T> T instantiate(@NonNull Class<?> input, @NonNull Object parameter) throws InvocationTargetException, InstantiationException, IllegalAccessException {
        Constructor<?> constructor = ConstructorUtils.getMatchingAccessibleConstructor(input, new Class[]{parameter.getClass()});
        return (T) constructor.newInstance(parameter);
    }

    public static void setValue(@NonNull Object object, @NonNull Field field, @NonNull Object newValue) throws IllegalAccessException {
        boolean acc = field.isAccessible();
        if(!acc)
            field.setAccessible(true);
        field.set(object, newValue);
        if(!acc)
            field.setAccessible(false);
    }
}
