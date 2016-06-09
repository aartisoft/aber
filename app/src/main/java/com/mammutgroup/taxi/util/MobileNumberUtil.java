package com.mammutgroup.taxi.util;

import com.mammutgroup.taxi.exception.InvalidMobileNumberException;

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
