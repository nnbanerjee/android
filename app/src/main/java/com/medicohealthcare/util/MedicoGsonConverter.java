package com.medicohealthcare.util;

import android.app.Activity;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.medicohealthcare.model.ServerResponse;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;

import retrofit.converter.ConversionException;
import retrofit.converter.Converter;
import retrofit.converter.GsonConverter;
import retrofit.mime.TypedByteArray;
import retrofit.mime.TypedInput;
import retrofit.mime.TypedOutput;

/**
 * Created by Narendra on 31-05-2017.
 */

public class MedicoGsonConverter implements Converter
{
    private GsonConverter converter;
    Activity activity;

    public MedicoGsonConverter(Activity activity, Gson gson)
    {
        converter = new GsonConverter(gson);
        this.activity = activity;
    }
    @Override
    public Object fromBody(TypedInput body,Type type) throws ConversionException
    {
        byte[] bodyBytes = readInBytes(body);
        TypedByteArray newBody = new TypedByteArray(body.mimeType(), bodyBytes);
        try
        {
            return converter.fromBody(newBody,type);
        }
        catch(ConversionException e)
        {
            if(e.getCause() instanceof JsonParseException )
            {
                ServerResponse response =  (ServerResponse)converter.fromBody(newBody, ServerResponse.class);
//                Toast.makeText(ParentActivity.activity, response.getErrorMessage(), Toast.LENGTH_LONG).show();
                throw new MedicoConversionException(response,e);
            }
            else
            {
                throw e;
            }
        }
    }
    @Override public TypedOutput toBody(Object object)
    {
        return converter.toBody(object);
    }
    private byte[] readInBytes(TypedInput body) throws ConversionException
    {
        InputStream in = null;
        try
        {
            in = body.in();
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            byte[] buffer = new byte[0xFFFF];
            for (int len; (len = in.read(buffer)) != -1;)
                os.write(buffer, 0, len);
                os.flush();
                return   os.toByteArray();
        } catch (IOException e)
        {
            throw new ConversionException(e);
        } finally
        {
            if (in != null)
            {
                try
                {
                    in.close();
                } catch (IOException ignored)
                {
                }
            }
        }
 
    }



}
