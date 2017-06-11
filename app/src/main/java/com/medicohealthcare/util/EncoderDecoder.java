package com.medicohealthcare.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import Decoder.BASE64Decoder;
import Decoder.BASE64Encoder;


public class EncoderDecoder
{
	private static String key = "a.kksd.labdbasdasdasdadasdassdad";
    public static final String DEFAULT_ENCODING = "UTF-8"; 
    static BASE64Encoder enc = new BASE64Encoder();
    static BASE64Decoder dec = new BASE64Decoder();

    public static String base64encode(String text) {
        try {
            return enc.encode(text.getBytes(DEFAULT_ENCODING));
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }//base64encode

    public static String base64decode(String text) {
        try {
            return new String(dec.decodeBuffer(text), DEFAULT_ENCODING);
        } catch (IOException e) {
            return null;
        }
    }//base64decode

    public static String encode(String originalString)
    {
        String txt = originalString;
        txt = xorMessage(txt, key);
        String encoded = base64encode(txt);   
        return encoded;
    }
    
    public static String decode(String encodedString)
    {
        String encoded = encodedString;      
        String txt = base64decode(encoded);
        txt = xorMessage(txt, key);
        return txt;
    }

    private static String xorMessage(String message, String key) 
    {
        try {
            if (message == null || key == null) return null;

            char[] keys = key.toCharArray();
            char[] mesg = message.toCharArray();

            int ml = mesg.length;
            int kl = keys.length;
            char[] newmsg = new char[ml];

            for (int i = 0; i < ml; i++) {
                newmsg[i] = (char)(mesg[i] ^ keys[i % kl]);
            }//for i

            return new String(newmsg);
        } catch (Exception e) {
            return null;
        }
    }//xorMessage


}
