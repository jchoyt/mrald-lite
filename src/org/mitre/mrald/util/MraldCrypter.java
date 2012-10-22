/*
 *  Copyright 2008 The MITRE Corporation (http://www.mitre.org/). All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.mitre.mrald.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;

/**
 *  Description of the Class
 *
 *@author     gail
 *@created    December 9, 2003
 */
public class MraldCrypter
{
    private final static SecureRandom secureRandom = new SecureRandom();


    /**
     *  Constructor for the SampleEncrypter object
     */
    public MraldCrypter() { }


    /**
     *  Description of the Method
     *
     *@param  prevPassStr                               Description of the Parameter
     *@param  newPassword                               Description of the Parameter
     *@return                                           Description of the Return Value
     *@exception  NoSuchAlgorithmException              Description of the Exception
     *@exception  java.io.IOException                   Description of the Exception
     *@exception  java.io.UnsupportedEncodingException  Description of the Exception
     */
    public static boolean matchPassword(String prevPassStr, String newPassword)
        throws NoSuchAlgorithmException, java.io.IOException, java.io.UnsupportedEncodingException
    {
        MessageDigest md = MessageDigest.getInstance("MD5");

        byte[] seed = new byte[12];

        //copy the seed values from the old password,
        //this seed value is used to generate the next value
        byte[] prevPass = new sun.misc.BASE64Decoder().decodeBuffer(prevPassStr);
        System.arraycopy(prevPass, 0, seed, 0, 12);

        md.update(seed);
        md.update(newPassword.getBytes("UTF8"));

        //Get digest of the new Password
        byte[] digestNewPassword = md.digest();

        //Create a new Byte array
        byte[] choppedPrevPassword = new byte[prevPass.length - 12];

        //Get the last bytes and put into choppedPrevPassword
        System.arraycopy(prevPass, 12, choppedPrevPassword, 0, prevPass.length - 12);

        boolean isMatching = Arrays.equals(digestNewPassword, choppedPrevPassword);
        return isMatching;
    }


    /**
     *  Description of the Method
     *
     *@param  password                          Description of the Parameter
     *@param  seed                              Description of the Parameter
     *@return                                   Description of the Return Value
     *@exception  NoSuchAlgorithmException      Description of the Exception
     *@exception  UnsupportedEncodingException  Description of the Exception
     */
    public static String encodePassword(String password, byte[] seed)
        throws NoSuchAlgorithmException, UnsupportedEncodingException
    {
        if (seed == null)
        {
            seed = new byte[12];
            secureRandom.nextBytes(seed);
        }

        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(seed);
        md.update(password.getBytes("UTF8"));
        byte[] digest = md.digest();
        byte[] storedPassword = new byte[digest.length + 12];

        System.arraycopy(seed, 0, storedPassword, 0, 12);
        System.arraycopy(digest, 0, storedPassword, 12, digest.length);

        return new sun.misc.BASE64Encoder().encode(storedPassword);
    }

    public static String encodeHash( String hash, byte[] seed)
        throws NoSuchAlgorithmException, UnsupportedEncodingException
    {
        return encodePassword( hash, seed );
    }
}

