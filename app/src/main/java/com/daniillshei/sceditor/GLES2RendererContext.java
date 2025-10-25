package com.daniillshei.sceditor;

import android.opengl.GLES20;

import dev.donutquine.editor.renderer.gl.GLRendererContext;

import java.lang.reflect.Method;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
// NO VAO SUPPORT, DO NOT USE
@Deprecated
public class GLES2RendererContext implements GLRendererContext {
    private static Method glGenVertexArraysOES;
    private static Method glBindVertexArrayOES;
    private static Method glDeleteVertexArraysOES;

    private static String extensions;

    public GLES2RendererContext() {
        try {
            glGenVertexArraysOES =
                    GLES20.class.getMethod("glGenVertexArraysOES", int.class, int[].class, int.class);
            glBindVertexArrayOES =
                    GLES20.class.getMethod("glBindVertexArrayOES", int.class);
            glDeleteVertexArraysOES =
                    GLES20.class.getMethod("glDeleteVertexArraysOES", int.class, int[].class, int.class);
        } catch (NoSuchMethodException e) {
            // Extension not available
        }
        extensions = GLES20.glGetString(GLES20.GL_EXTENSIONS);
    }

    @Override
    public int glGenVertexArray() {
        int[] ids = new int[1];
        try { glGenVertexArraysOES.invoke(null, 1, ids, 0); } catch (Exception ignored) {}
        return ids[0];
    }

    @Override
    public void glBindVertexArray(int id) {
        try { glBindVertexArrayOES.invoke(null, id); } catch (Exception ignored) {}
    }

    @Override
    public void glDeleteVertexArray(int id) {
        try { glDeleteVertexArraysOES.invoke(null, 1, new int[]{id}, 0); } catch (Exception ignored) {}
    }

    @Override
    public void glEnableVertexAttribArray(int index) {
        GLES20.glEnableVertexAttribArray(index);
    }

    @Override
    public void glDisableVertexAttribArray(int index) {
        GLES20.glDisableVertexAttribArray(index);
    }

    @Override
    public void glVertexAttribPointer(int layout, int size, int type, boolean normalized, int stride, int offset) {
        GLES20.glVertexAttribPointer(layout, size, type, normalized, stride, offset);
    }

    @Override
    public int glGenBuffer() {
        int[] ids = new int[1];
        GLES20.glGenBuffers(1, ids, 0);
        return ids[0];
    }

    @Override
    public void glBindBuffer(int bufferType, int id) {
        GLES20.glBindBuffer(bufferType, id);
    }

    @Override
    public void glDeleteBuffer(int id) {
        GLES20.glDeleteBuffers(1, new int[]{id}, 0);
    }

    @Override
    public void glBufferData(int bufferType, long byteLength, Buffer buffer, int usage) {
        GLES20.glBufferData(bufferType, (int) byteLength, buffer, usage);
    }

    @Override
    public void glBufferSubData(int bufferType, int offset, long byteLength, Buffer buffer) {
        GLES20.glBufferSubData(bufferType, offset, (int) byteLength, buffer);
    }

    @Override
    public void glDrawElements(int drawMode, int elementCount, int glUnsignedInt, int indices) {
        GLES20.glDrawElements(drawMode, elementCount, glUnsignedInt, indices);
    }

    @Override
    public int glGenTexture() {
        IntBuffer ids = IntBuffer.allocate(1);
        GLES20.glGenTextures(1, ids);
        return ids.get(0);
    }

    @Override
    public void glActiveTexture(int textureSlot) {
        GLES20.glActiveTexture(textureSlot);
    }

    @Override
    public void glBindTexture(int textureType, int id) {
        GLES20.glBindTexture(textureType, id);
    }

    @Override
    public void glDeleteTexture(int id) {
        GLES20.glDeleteTextures(1, new int[]{id}, 0);
    }

    @Override
    public void glDeleteTextures(int count, int[] ids, int offset) {
        GLES20.glDeleteTextures(count, ids, offset);
    }

    @Override
    public void glGenerateMipmap(int textureType) {
        GLES20.glGenerateMipmap(textureType);
    }

    @Override
    public void glGetTexImage(int textureType, int level, int internalFormat, int pixelType, IntBuffer pixels) {
//        GLES20.glGetTexImage(textureType, level, internalFormat, pixelType, pixels);
        throw new UnsupportedOperationException("");
    }

    @Override
    public void glGetTexLevelParameteriv(int textureType, int level, int type, IntBuffer result) {
//        GLES20.glGetTexLevelParameteriv(textureType, level, type, result);
        throw new UnsupportedOperationException("");
    }

    @Override
    public void glGetCompressedTexImage(int textureType, int level, IntBuffer data) {
//        GLES20.glGetCompressedTexImage(textureType, level, data);
        throw new UnsupportedOperationException("");
    }

    @Override
    public void glTexParameteri(int textureType, int type, int value) {
        GLES20.glTexParameteri(textureType, type, value);
    }

    @Override
    public void glTexParameteriv(int textureType, int type, IntBuffer value) {
        GLES20.glTexParameteriv(textureType, type, value);
    }

    @Override
    public void glTexImage2D(int textureType, int level, int internalFormat, int width, int height, int border, int format, int pixelType, Buffer data) {
        GLES20.glTexImage2D(textureType, level, internalFormat, width, height, border, format, pixelType, data);
    }

    @Override
    public void glCompressedTexImage2D(int textureType, int level, int internalFormat, int width, int height, int border, int byteLength, ByteBuffer data) {
        GLES20.glCompressedTexImage2D(textureType, level, internalFormat, width, height, border, byteLength, data);
    }

    @Override
    public void glTexSubImage2D(int target, int level, int xOffset, int yOffset, int width, int height, int format, int pixelType, Buffer pixels) {
        GLES20.glTexSubImage2D(target, level, xOffset, yOffset, width, height, format, pixelType, pixels);
    }

    @Override
    public int glGenFramebuffer() {
        IntBuffer ids = IntBuffer.allocate(1);
        GLES20.glGenFramebuffers(1, ids);
        return ids.get(0);
    }

    @Override
    public void glBindFramebuffer(int target, int id) {
        GLES20.glBindFramebuffer(target, id);
    }

    @Override
    public void glDeleteFramebuffer(int id) {
        GLES20.glDeleteFramebuffers(1, new int[]{id}, 0);
    }

    @Override
    public void glFramebufferRenderbuffer(int target, int attachmentType, int renderbufferTarget, int renderbuffer) {
        GLES20.glFramebufferRenderbuffer(target, attachmentType, renderbufferTarget, renderbuffer);
    }

    @Override
    public void glFramebufferTexture2D(int target, int attachmentType, int textureTarget, int texture, int level) {
        GLES20.glFramebufferTexture2D(target, attachmentType, textureTarget, texture, level);
    }

    @Override
    public int getBoundFramebuffer(int target) {
        int[] fbo = new int[1];
        GLES20.glGetIntegerv(GLES20.GL_FRAMEBUFFER_BINDING,fbo,0);
        return fbo[0];
    }

    @Override
    public int glCheckFramebufferStatus(int target) {
        return GLES20.glCheckFramebufferStatus(target);
    }

    @Override
    public int glGenRenderbuffer() {
        IntBuffer ids = IntBuffer.allocate(1);
        GLES20.glGenRenderbuffers(1, ids);
        return ids.get(0);
    }

    @Override
    public void glBindRenderbuffer(int bufferType, int id) {
        GLES20.glBindRenderbuffer(bufferType, id);
    }

    @Override
    public void glDeleteRenderbuffer(int id) {
        GLES20.glDeleteRenderbuffers(1, new int[]{id}, 0);
    }

    @Override
    public void glRenderbufferStorage(int target, int internalFormat, int width, int height) {
        GLES20.glRenderbufferStorage(target, internalFormat, width, height);
    }

    @Override
    public void glViewport(int x, int y, int width, int height) {
        GLES20.glViewport(x, y, width, height);
    }

    @Override
    public void glEnable(int capability) {
        GLES20.glEnable(capability);
    }

    @Override
    public void glDisable(int capability) {
        GLES20.glDisable(capability);
    }

    @Override
    public void glBlendEquation(int mode) {
        GLES20.glBlendEquation(mode);
    }

    @Override
    public void glBlendFunc(int sFactor, int dFactor) {
        GLES20.glBlendFunc(sFactor, dFactor);
    }

    @Override
    public void glClearColor(float r, float g, float b, float a) {
        GLES20.glClearColor(r, g, b, a);
    }

    @Override
    public void glClear(int buffersMask) {
        GLES20.glClear(buffersMask);
    }

    @Override
    public void glStencilMask(int mask) {
        GLES20.glStencilMask(mask);
    }

    @Override
    public void glClearStencil(int index) {
        GLES20.glClearStencil(index);
    }

    @Override
    public void glStencilFunc(int func, int ref, int mask) {
        GLES20.glStencilFunc(func, ref, mask);
    }

    @Override
    public void glStencilOp(int sFail, int dpFail, int dpPass) {
        GLES20.glStencilOp(sFail, dpFail, dpPass);
    }

    @Override
    public void glColorMask(boolean r, boolean g, boolean b, boolean a) {
        GLES20.glColorMask(r, g, b, a);
    }

    @Override
    public void glDepthMask(boolean enabled) {
        GLES20.glDepthMask(enabled);
    }

    @Override
    public int glCreateShader(int shaderType) {
        return GLES20.glCreateShader(shaderType);
    }

    @Override
    public void glDeleteShader(int shader) {
        GLES20.glDeleteShader(shader);
    }

    @Override
    public void glShaderSource(int shader, String source) {
        GLES20.glShaderSource(shader, source);
    }

    @Override
    public void glCompileShader(int shader) {
        GLES20.glCompileShader(shader);
    }

    @Override
    public void glGetShaderInfoLog(int shader, int logBufferLength, IntBuffer length, ByteBuffer logBuffer) {
        var log = GLES20.glGetShaderInfoLog(shader);
        logBuffer.put(log.getBytes());
    }

    @Override
    public void glGetShaderiv(int shader, int parameter, IntBuffer result) {
        GLES20.glGetShaderiv(shader, parameter, result);
    }

    @Override
    public int glCreateProgram() {
        return GLES20.glCreateProgram();
    }

    @Override
    public void glUseProgram(int program) {
        GLES20.glUseProgram(program);
    }

    @Override
    public void glDeleteProgram(int program) {
        GLES20.glDeleteProgram(program);
    }

    @Override
    public void glAttachShader(int program, int shader) {
        GLES20.glAttachShader(program, shader);
    }

    @Override
    public void glLinkProgram(int program) {
        GLES20.glLinkProgram(program);
    }

    @Override
    public void glGetProgramiv(int program, int parameter, IntBuffer result) {
        GLES20.glGetProgramiv(program, parameter, result);
    }

    @Override
    public void glGetProgramInfoLog(int program, int logBufferLength, IntBuffer length, ByteBuffer logBuffer) {
        var log = GLES20.glGetProgramInfoLog(program);
        logBuffer.put(log.getBytes());
    }

    @Override
    public int glGetUniformLocation(int program, String uniformName) {
        return GLES20.glGetUniformLocation(program, uniformName);
    }

    @Override
    public void glUniformMatrix4fv(int uniformLocation, int count, boolean transpose, FloatBuffer matrixBuffer) {
        GLES20.glUniformMatrix4fv(uniformLocation, count, transpose, matrixBuffer);
    }

    @Override
    public void glPixelStorei(int parameter, int value) {
        GLES20.glPixelStorei(parameter, value);
    }

    @Override
    public boolean isExtensionAvailable(String extension) {
        return extensions != null && extensions.contains(extension);
    }

    @Override
    public int glGetError() {
        return GLES20.glGetError();
    }

    @Override
    public void glPolygonMode(int glFrontAndBack, int mode) {
//        GLES20.glPolygonMode(glFrontAndBack, mode);
        throw new UnsupportedOperationException("");
    }

    @Override
    public String glGetString(int target) {
        return GLES20.glGetString(target);
    }
}


