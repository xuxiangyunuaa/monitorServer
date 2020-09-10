package org.nit.monitorserver.util;

//import com.sun.istack.internal.FinalArrayList;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
//import jdk.nashorn.internal.runtime.regexp.joni.Regex;

import javax.lang.model.element.VariableElement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.function.ObjIntConsumer;
import java.util.regex.Pattern;

/**
 * 表单验证
 * @author eda
 * @date 2018/7/4
 */

public class FormValidator {
    /**
     * 正则验证
     *
     * @param value 表单值
     * @param regex 正则表达式
     * @return boolean
     */
    public static boolean regex(final String value, final String regex) {
        boolean res = false;
        if (value != null && regex != null) {
            res = Pattern.matches(regex, value);
        }
        return res;
    }

    public static boolean lengthBetween(final String value, final int minLength, final int maxLength) {
        boolean res = false;
        if (value == null) {
            return false;
        }
        if (value.length() >= minLength && value.length() <= maxLength) {
            res = true;
        }
        return res;
    }

    public static boolean in(final int value, final int... range) {
        boolean res = false;

        for (int i : range) {
            if (value == i) {
                res = true;
            }
        }
        return res;
    }

    public static boolean in(final String value, final String... range) {
        boolean res = false;

        if (value == null) {
            return false;
        }

        for (String i : range) {
            if (value.equals(i)) {
                res = true;
            }
        }
        return res;
    }

    public static boolean between(final int value, final int minValue, final int maxValue) {
        return value >= minValue && value <= maxValue;
    }

    public static boolean isString(final Object obj) {
        return obj instanceof String;

    }

    public static boolean isInteger(final Object obj) {
        return obj instanceof Integer;
    }

    public static boolean isLong(final Object obj) {
        return obj instanceof Long;
    }

    public static boolean isBoolean(final Object obj) {
        return obj instanceof Boolean;
    }


    public static boolean isFloat(final Object obj) {
        return obj instanceof Float;
    }

    public static boolean isDouble(final Object obj) {
        return obj instanceof Double;
    }


    public static boolean isJsonArray(final Object obj) {
        return obj instanceof JsonArray;
    }


    public static boolean isJsonObject(final Object obj) {
        return obj instanceof JsonObject;
    }


    public static boolean isValidYYYYMMDD(final Object date) {
        String dateRegex = "^[1-9]\\d{3}(0[1-9]|1[0-2])(0[1-9]|[1-2][0-9]|3[0-1])";

        if (date == null) {
            return false;
        }
        if (Pattern.matches(dateRegex, date.toString())) {
            return true;
        }
        return false;
    }

    public static boolean isValidHHMMSS(final Object time) {
        String timeRegex = "^(([0-1]?\\d)|(2[0-4])):[0-5]?\\d$";

        if (time == null) {
            return false;
        }
        if (Pattern.matches(timeRegex, time.toString())) {
            return true;
        }
        return  false;
    }
    public static boolean isValidYYYYMMDDHHmmSSString(Object dateString) {
        if (dateString == null || !(dateString instanceof String)) return false;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try {
            Date date = format.parse(dateString.toString());
        } catch (Exception e) {
            return false;
        }
        return true;

    }
}

