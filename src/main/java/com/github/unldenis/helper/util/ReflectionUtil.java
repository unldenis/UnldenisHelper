package com.github.unldenis.helper.util;

import org.apache.commons.lang.reflect.ConstructorUtils;

import javax.annotation.Nonnull;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

public class ReflectionUtil {

    public static @Nonnull <T> T getValue(@Nonnull Object object, @Nonnull String fieldName) throws NoSuchFieldException, IllegalAccessException {
        Field field = object.getClass().getDeclaredField(fieldName);
        return getValue(object, field);
    }

    public static @Nonnull <T> T getValue(@Nonnull Object object, @Nonnull Field field) throws IllegalAccessException {
        field.setAccessible(true);
        T value = (T) field.get(object);
        field.setAccessible(false);
        return value;
    }

    public static @Nonnull <T> T instantiate(@Nonnull Class<?> input, @Nonnull Object parameter) throws InvocationTargetException, InstantiationException, IllegalAccessException {
        Constructor<?> constructor = ConstructorUtils.getMatchingAccessibleConstructor(input, new Class[]{parameter.getClass()});
        return (T) constructor.newInstance(parameter);
    }
}
