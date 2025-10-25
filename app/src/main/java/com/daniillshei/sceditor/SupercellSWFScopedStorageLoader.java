package com.daniillshei.sceditor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Paths;

import dev.donutquine.swf.SupercellSWF;
import dev.donutquine.swf.exceptions.LoadingFaultException;
import dev.donutquine.swf.exceptions.TextureFileNotFound;
import dev.donutquine.swf.exceptions.UnableToFindObjectException;
import dev.donutquine.swf.exceptions.UnsupportedCustomPropertyException;
import dev.donutquine.swf.file.ScFileInfo;
import dev.donutquine.swf.file.ScFileUnpacker;
import dev.donutquine.swf.file.exceptions.FileVerificationException;
import dev.donutquine.swf.file.exceptions.UnknownFileVersionException;

public class SupercellSWFScopedStorageLoader {
    private static final Logger LOGGER = LoggerFactory.getLogger(SupercellSWF.class);
    public static boolean LoadScBytes(SupercellSWF swf, byte[] data, String fileName, boolean isTextureFile, boolean preferLowres) throws LoadingFaultException, UnableToFindObjectException, UnsupportedCustomPropertyException, TextureFileNotFound {
//        setPrivateField(swf,"filename",fileName);
//        setPrivateField(swf,"path",fileName);
        try {
            ScFileInfo unpacked = ScFileUnpacker.unpack(data);
            setPrivateField(swf,"containerVersion",unpacked.version());
            return (boolean) (unpacked.version() >= 5 ? invokePrivateMethod(swf, "loadSc2",unpacked.data(), preferLowres) : invokePrivateMethod(swf, "loadSc1", Paths.get(fileName), isTextureFile, unpacked.data()));
        } catch (FileVerificationException | IOException | UnknownFileVersionException exception) {
            LOGGER.error("An error occurred while decompressing the file: {}", fileName, exception);
            return false;
        }
    }
    public static void setPrivateField(Object e,String fieldName,Object value){
        try {
            var field = e.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(e, value);
        }
        catch (Exception ignored){
            ;
        }
    }
    public static Object invokePrivateMethod(Object e,String methodName,Object... args){
        try {
            var method = e.getClass().getDeclaredMethod(methodName);
            method.setAccessible(true);
            return method.invoke(e,args);
        }
        catch (Exception ignored){
            return null;
        }
    }
}
