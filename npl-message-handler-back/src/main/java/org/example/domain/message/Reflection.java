package org.example.domain.message;

import org.apache.commons.lang3.reflect.FieldUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.beanutils.PropertyUtils;

public class Reflection {
    public Object get(String position) {
        try {

            if (PropertyUtils.isReadable(this, position)) {
                return PropertyUtils.getProperty(this, position);
            }

            String firstPosition = position.split("\\.")[0];
            String remainingPosition = position.replace(firstPosition + ".", "");

            if (PropertyUtils.isReadable(this, firstPosition)) {
                Object field = PropertyUtils.getProperty(this, firstPosition);
                if ((List.class).isAssignableFrom(field.getClass())) {
                    field = ((ArrayList<?>) field).get(0);
                }
                return ((Reflection) field).get(remainingPosition);
            }

            for (Field field : FieldUtils.getAllFields(this.getClass())) {
                if (((Reflection.class).isAssignableFrom(field.getType()) || (List.class).isAssignableFrom(field.getType())) && !field.getName().equals("this$0")) {
                    field.setAccessible(true);
                    Object fieldObject = field.get(this);
                    if ((List.class).isAssignableFrom(fieldObject.getClass())) {
                        fieldObject = ((ArrayList<?>) fieldObject).get(0);
                    }
                    Object value = ((Reflection) fieldObject).get(position);
                    if (value != null) {
                        return value;
                    }
                }
            }

            return null;

        }  catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void set(String position, Object value) {
        try {
            if (PropertyUtils.isReadable(this, position)) {
                PropertyUtils.setProperty(this, position, value);
            }

            String firstPosition = position.split("\\.")[0];
            String remainingPosition = position.replace(firstPosition + ".", "");


            if (PropertyUtils.isReadable(this, firstPosition)) {
                Object field = PropertyUtils.getProperty(this, firstPosition);
                if ((List.class).isAssignableFrom(field.getClass())) {
                    field = ((ArrayList<?>) field).get(0);
                }
                ((Reflection) field).set(remainingPosition, value);
            }

            for (Field field : FieldUtils.getAllFields(this.getClass())) {
                if (((Reflection.class).isAssignableFrom(field.getType()) || (List.class).isAssignableFrom(field.getType())) && !field.getName().equals("this$0")) {
                    field.setAccessible(true);
                    Object fieldObject = field.get(this);
                    if ((List.class).isAssignableFrom(fieldObject.getClass())) {
                        fieldObject = ((ArrayList<?>) fieldObject).get(0);
                    }
                    ((Reflection) fieldObject).set(position, value);
                }
            }

        }  catch (Exception e) {
            e.printStackTrace();
        }
    }



    private Reflection getNestedObject(Method method, String methodName) throws Exception {
        if (methodName.endsWith("List")) {
            List<Reflection> objectList = (List<Reflection>) method.invoke(this);
            return objectList.get(0);
        } else {
            return (Reflection) method.invoke(this);
        }
    }

    private String capitalize(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    public boolean isFieldFilled(String fieldName) {
        try {
            return isFieldFilledRecursive(this, fieldName);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean isFieldFilledRecursive(Object obj, String fieldName) throws IllegalAccessException {
        if (obj == null) {
            return false;
        }

        for (Field field : obj.getClass().getDeclaredFields()) {

            field.setAccessible(true);
            Object fieldValue = field.get(obj);

            if (field.getName().equals(fieldName)) {
                return fieldValue != null;
            } else if ((fieldValue instanceof Reflection) && (!field.getName().equals("this$0"))) {
                if (((Reflection) fieldValue).isFieldFilled(fieldName)) {
                    return true;
                }
            } else if (fieldValue instanceof List<?>) {
                for (Object item : (List<?>) fieldValue) {
                    if (item instanceof Reflection) {
                        if (((Reflection) item).isFieldFilled(fieldName)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
}
