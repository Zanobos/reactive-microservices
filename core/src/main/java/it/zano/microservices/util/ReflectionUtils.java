package it.zano.microservices.util;

import java.lang.reflect.Field;

public final class ReflectionUtils {

    private ReflectionUtils() {}
	/**
     * Set Object field value.
     *
     * @param object the object
     * @param field  the field
     * @param value  the value
     * @return true, if successful
     * @throws IllegalAccessException the illegal access exception
     */
    public static boolean setFieldValue(Object object, Field field, Object value) throws IllegalAccessException {
        try {
            if (field != null && object != null && value != null) {
                field.set(object, value);
                return true;
            }
        } catch (Exception ex) {
            return false;
        }
        return false;
    }

    /**
     * Retrieve field by name.
     *
     * @param name  the name
     * @param clazz the clazz
     * @return the field
     */
    public static Field getField(String name, Class clazz) {
        if (name != null && clazz != null) {
            try {
                Field f = clazz.getDeclaredField(name);
                f.setAccessible(true);
                return f;
            } catch (Exception ex) {
                return null;
            }
        }
        return null;
    }

    /**
     * Instantiate Object.
     *
     * @param clazz the clazz
     * @return the object
     */
    public static Object newInstance(Class clazz) {
        if (clazz != null) {
            try {
                return clazz.newInstance();
            } catch (Exception ex) {
                return null;
            }
        }
        return null;
    }

    /**
     * Returns true if aClassOrigin extends aClass
     *
     * @param aClass       reference class
     * @param aClassOrigin origin class
     * @return boolean
     */
    public static boolean isInstanceOf(Class<?> aClassOrigin, Class<?> aClass) {
        boolean isInstanceOf = false;
        while (!aClassOrigin.equals(Object.class)) {
            if (aClassOrigin.equals(aClass)) {
                isInstanceOf = true;
                break;
            }
            aClassOrigin = aClassOrigin.getSuperclass();
        }
        return isInstanceOf;
    }

}
