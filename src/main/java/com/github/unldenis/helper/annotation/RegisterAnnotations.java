package com.github.unldenis.helper.annotation;

import com.github.unldenis.helper.annotation.craftbukkit.CBClass;
import com.github.unldenis.helper.annotation.craftbukkit.CBClassNotFoundException;
import com.github.unldenis.helper.annotation.craftbukkit.CBField;
import com.github.unldenis.helper.annotation.craftbukkit.CBMethod;
import com.github.unldenis.helper.annotation.nms.NMSClass;
import com.github.unldenis.helper.annotation.nms.NMSClassNotFoundException;
import com.github.unldenis.helper.annotation.nms.NMSField;
import com.github.unldenis.helper.annotation.nms.NMSMethod;
import com.github.unldenis.helper.util.ReflectionUtil;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class RegisterAnnotations {

    private static Map<String, Class<?>> NMS_CLASSES = new HashMap<>();
    private static Map<String, Class<?>> CRAFTBUKKIT_CLASSES = new HashMap<>();

    static {
        //nms
        Reflections reflections = new Reflections("net.minecraft.", new SubTypesScanner(false));
        for(Class clazz: reflections.getSubTypesOf(Object.class))
            NMS_CLASSES.put(clazz.getSimpleName(), clazz);
        //craftbukkit
        String versionCode = Bukkit.getServer().getClass().getPackage().getName().substring("org.bukkit.craftbukkit".length() + 1);
        Reflections reflectionsCB = new Reflections("org.bukkit.craftbukkit." + versionCode + ".", new SubTypesScanner(false));
        for(Class clazz: reflectionsCB.getSubTypesOf(Object.class))
            CRAFTBUKKIT_CLASSES.put(clazz.getSimpleName(), clazz);
    }

    /**
     * Method used to register a class that can contain one of the annotations (NMSClass. NMSField, NMSMethod, CBClass, CBField, CBMethod)
     * @param obj an object or class that contains one of the annotations
     * @param registerType if the class in question has only nms or craftbukkit annotations or both
     * @throws Exception launched if the class sought does not exist or in the presence of some other error
     */
    public static void register(@NonNull Object obj, @NonNull RegisterType registerType) throws Exception {
        if(obj instanceof Class)
            register((Class) obj, null, registerType);
        else
            register(obj.getClass(), obj, registerType);
    }


    private static void register(Class clazz, Object obj, RegisterType registerType) throws Exception {
        Field[] fields = clazz.getFields();
        switch (registerType) {
            case ALL: {
                for (Field field : fields) {
                    checkNMS(obj, field);
                    checkCB(obj, field);
                }
                break;
            }
            case NMS : {
                for (Field field : fields)
                    checkNMS(obj, field);
                break;
            }
            case CRAFTBUKKIT : {
                for (Field field : fields)
                    checkCB(obj, field);
            }
        }
    }

    private static Class<?> containsNMS(String simpleName) {
        return NMS_CLASSES.getOrDefault(simpleName, null);
    }

    private static Class<?> containsCB(String simpleName) {
        return CRAFTBUKKIT_CLASSES.getOrDefault(simpleName, null);
    }


    private static Class<?> parseClass(String clazz) {
        //primitives
        switch(clazz) {
            case "boolean":
                return boolean.class;
            case "boolean[]":
                return boolean[].class;
            case "byte":
                return byte.class;
            case "byte[]":
                return byte[].class;
            case "short":
                return short.class;
            case "short[]":
                return short[].class;
            case "int":
                return int.class;
            case "int[]":
                return int[].class;
            case "long":
                return long.class;
            case "long[]":
                return long[].class;
            case "float":
                return float.class;
            case "float[]":
                return float[].class;
            case "double":
                return double.class;
            case "double[]":
                return double[].class;
        }
        if(clazz.contains(".")) {
            //like java.lang.String , org.bukkit.inventory.ItemStack
            try {
                return Class.forName(clazz);
            } catch (ClassNotFoundException ignored) { }
        }else{
            //class from nms
            Class nmsClass = containsNMS(clazz);
            //if nms class not found find craft bukkit class
            return nmsClass!=null ? nmsClass : containsCB(clazz);
        }
        return null;
    }


    /**
     * NMS
     */
    private static void checkNMS(Object obj, Field field) throws Exception {
        if(field.isAnnotationPresent(NMSClass.class)) {
            NMSClass annotation = field.getAnnotation(NMSClass.class);
            String nAnnotation = annotation.name().isEmpty() ? field.getName() : annotation.name();
            Class<?> nmsclass = containsNMS(nAnnotation);
            if(nmsclass==null)
                throw new NMSClassNotFoundException(nAnnotation);
            ReflectionUtil.setValue(obj, field, nmsclass);
        }
        else if(field.isAnnotationPresent(NMSField.class)) {
            NMSField annotation = field.getAnnotation(NMSField.class);
            Class<?> nmsclass = containsNMS(annotation.clazz());
            if(nmsclass==null)
                throw new NMSClassNotFoundException(annotation.clazz());
            Field fieldNms = nmsclass.getField(annotation.name());
            ReflectionUtil.setValue(obj, field, fieldNms);
        }
        else if(field.isAnnotationPresent(NMSMethod.class)) {
            NMSMethod annotation = field.getAnnotation(NMSMethod.class);
            Class<?> nmsclass = containsNMS(annotation.clazz());
            if(nmsclass==null)
                throw new NMSClassNotFoundException(annotation.clazz());
            Class<?>[] parametersTypes = new Class[annotation.parametersTypes().length];
            for(int j=0; j<parametersTypes.length; j++)
                parametersTypes[j] = parseClass(annotation.parametersTypes()[j]);
            Method methodNms = nmsclass.getMethod(annotation.name(), parametersTypes);
            ReflectionUtil.setValue(obj, field, methodNms);;
        }
    }

    /**
     * CraftBukkit
     */
    private static void checkCB(Object obj, Field field) throws Exception {
        if(field.isAnnotationPresent(CBClass.class)) {
            CBClass annotation = field.getAnnotation(CBClass.class);
            String nAnnotation = annotation.name().isEmpty() ? field.getName() : annotation.name();
            Class<?> cbclass = containsCB(nAnnotation);
            if(cbclass==null)
                throw new CBClassNotFoundException(nAnnotation);
            ReflectionUtil.setValue(obj, field, cbclass);
        }
        else if(field.isAnnotationPresent(CBField.class)) {
            CBField annotation = field.getAnnotation(CBField.class);
            Class<?> cbclass = containsCB(annotation.clazz());
            if(cbclass==null)
                throw new CBClassNotFoundException(annotation.clazz());
            Field fieldNms = cbclass.getField(annotation.name());
            ReflectionUtil.setValue(obj, field, fieldNms);
        }
        else if(field.isAnnotationPresent(CBMethod.class)) {
            CBMethod annotation = field.getAnnotation(CBMethod.class);
            Class<?> cbclass = containsCB(annotation.clazz());
            if(cbclass==null)
                throw new CBClassNotFoundException(annotation.clazz());
            Class<?>[] parametersTypes = new Class[annotation.parametersTypes().length];
            for(int j=0; j<parametersTypes.length; j++)
                parametersTypes[j] = parseClass(annotation.parametersTypes()[j]);
            Method methodNms = cbclass.getMethod(annotation.name(), parametersTypes);
            ReflectionUtil.setValue(obj, field, methodNms);;
        }
    }
}
