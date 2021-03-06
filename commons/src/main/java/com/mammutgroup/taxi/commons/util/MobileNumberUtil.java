package com.mammutgroup.taxi.commons.util;


import com.mammutgroup.taxi.commons.exception.InvalidMobileNumberException;

/**
 * @author mushtu
 * @since 5/25/16.
 */
public class MobileNumberUtil {

    public static void validate(String number) throws InvalidMobileNumberException
    {
        if(number.startsWith("0"))
            validate(number.substring(1));
        else if(number.length() != 10)
            throw new InvalidMobileNumberException();

    }
}
