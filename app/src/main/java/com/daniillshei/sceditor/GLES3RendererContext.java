package com.daniillshei.sceditor;

import android.opengl.GLES30;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import dev.donutquine.editor.renderer.gl.GLRendererContext;

public class GLES3RendererContext implements GLRendererContext {
    private static String extensions;

    public GLES3RendererContext() {
        extensions = GLES30.glGetString(GLES30.GL_EXTENSIONS);
    }

    @Override
    public int glGenVertexArray() {
        int[] ids = new int[1];
        GLES30.glGenVertexArrays(1, ids, 0);
        return ids[0];
    }

    @Override
    public void glBindVertexArray(int id) {
        GLES30.glBindVertexArray(id);
    }

    @Override
    public void glDeleteVertexArray(int id) {
        GLES30.glDeleteVertexArrays(1, new int[]{id}, 0);
    }

    @Override
    public void glEnableVertexAttribArray(int index) {
        GLES30.glEnableVertexAttribArray(index);
    }

    @Override
    public void glDisableVertexAttribArray(int index) {
        GLES30.glDisableVertexAttribArray(index);
    }

    @Override
    public void glVertexAttribPointer(int layout, int size, int type, boolean normalized, int stride, int offset) {
        GLES30.glVertexAttribPointer(layout, size, type, normalized, stride, offset);
    }

    @Override
    public int glGenBuffer() {
        int[] ids = new int[1];
        GLES30.glGenBuffers(1, ids, 0);
        return ids[0];
    }

    @Override
    public void glBindBuffer(int bufferType, int id) {
        GLES30.glBindBuffer(bufferType, id);
    }

    @Override
    public void glDeleteBuffer(int id) {
        GLES30.glDeleteBuffers(1, new int[]{id}, 0);
    }

    @Override
    public void glBufferData(int bufferType, long byteLength, Buffer buffer, int usage) {
        GLES30.glBufferData(bufferType, (int) byteLength, buffer, usage);
    }

    @Override
    public void glBufferSubData(int bufferType, int offset, long byteLength, Buffer buffer) {
        GLES30.glBufferSubData(bufferType, offset, (int) byteLength, buffer);
    }

    @Override
    public void glDrawElements(int drawMode, int elementCount, int glUnsignedInt, int indices) {
        GLES30.glDrawElements(drawMode, elementCount, glUnsignedInt, indices);
    }

    @Override
    public int glGenTexture() {
        IntBuffer ids = IntBuffer.allocate(1);
        GLES30.glGenTextures(1, ids);
        return ids.get(0);
    }

    @Override
    public void glActiveTexture(int textureSlot) {
        GLES30.glActiveTexture(textureSlot);
    }

    @Override
    public void glBindTexture(int textureType, int id) {
        GLES30.glBindTexture(textureType, id);
    }

    @Override
    public void glDeleteTexture(int id) {
        GLES30.glDeleteTextures(1, new int[]{id}, 0);
    }

    @Override
    public void glDeleteTextures(int count, int[] ids, int offset) {
        GLES30.glDeleteTextures(count, ids, offset);
    }

    @Override
    public void glGenerateMipmap(int textureType) {
        GLES30.glGenerateMipmap(textureType);
    }

    @Override
    public void glGetTexImage(int textureType, int level, int internalFormat, int pixelType, IntBuffer pixels) {
//        GLES30.glGetTexImage(textureType, level, internalFormat, pixelType, pixels);
        throw new UnsupportedOperationException("");
    }

    @Override
    public void glGetTexLevelParameteriv(int textureType, int level, int type, IntBuffer result) {
//        GLES30.glGetTexLevelParameteriv(textureType, level, type, result);
        throw new UnsupportedOperationException("");
    }

    @Override
    public void glGetCompressedTexImage(int textureType, int level, IntBuffer data) {
//        GLES30.glGetCompressedTexImage(textureType, level, data);
        throw new UnsupportedOperationException("");
    }

    @Override
    public void glTexParameteri(int textureType, int type, int value) {
        GLES30.glTexParameteri(textureType, type, value);
    }

    @Override
    public void glTexParameteriv(int textureType, int type, IntBuffer value) {
        GLES30.glTexParameteriv(textureType, type, value);
    }

    @Override
    public void glTexImage2D(int textureType, int level, int internalFormat, int width, int height, int border, int format, int pixelType, Buffer data) {
        GLES30.glTexImage2D(textureType, level, internalFormat, width, height, border, format, pixelType, data);
    }

    @Override
    public void glCompressedTexImage2D(int textureType, int level, int internalFormat, int width, int height, int border, int byteLength, ByteBuffer data) {
        GLES30.glCompressedTexImage2D(textureType, level, internalFormat, width, height, border, byteLength, data);
    }

    @Override
    public void glTexSubImage2D(int target, int level, int xOffset, int yOffset, int width, int height, int format, int pixelType, Buffer pixels) {
        GLES30.glTexSubImage2D(target, level, xOffset, yOffset, width, height, format, pixelType, pixels);
    }

    @Override
    public int glGenFramebuffer() {
        IntBuffer ids = IntBuffer.allocate(1);
        GLES30.glGenFramebuffers(1, ids);
        return ids.get(0);
    }

    @Override
    public void glBindFramebuffer(int target, int id) {
        GLES30.glBindFramebuffer(target, id);
    }

    @Override
    public void glDeleteFramebuffer(int id) {
        GLES30.glDeleteFramebuffers(1, new int[]{id}, 0);
    }

    @Override
    public void glFramebufferRenderbuffer(int target, int attachmentType, int renderbufferTarget, int renderbuffer) {
        GLES30.glFramebufferRenderbuffer(target, attachmentType, renderbufferTarget, renderbuffer);
    }

    @Override
    public void glFramebufferTexture2D(int target, int attachmentType, int textureTarget, int texture, int level) {
        GLES30.glFramebufferTexture2D(target, attachmentType, textureTarget, texture, level);
    }

    @Override
    public int getBoundFramebuffer(int target) {
        int[] fbo = new int[1];
        GLES30.glGetIntegerv(GLES30.GL_FRAMEBUFFER_BINDING, fbo, 0);
        return fbo[0];
    }

    @Override
    public int glCheckFramebufferStatus(int target) {
        return GLES30.glCheckFramebufferStatus(target);
    }

    @Override
    public int glGenRenderbuffer() {
        IntBuffer ids = IntBuffer.allocate(1);
        GLES30.glGenRenderbuffers(1, ids);
        return ids.get(0);
    }

    @Override
    public void glBindRenderbuffer(int bufferType, int id) {
        GLES30.glBindRenderbuffer(bufferType, id);
    }

    @Override
    public void glDeleteRenderbuffer(int id) {
        GLES30.glDeleteRenderbuffers(1, new int[]{id}, 0);
    }

    @Override
    public void glRenderbufferStorage(int target, int internalFormat, int width, int height) {
        GLES30.glRenderbufferStorage(target, internalFormat, width, height);
    }

    @Override
    public void glViewport(int x, int y, int width, int height) {
        GLES30.glViewport(x, y, width, height);
    }

    @Override
    public void glEnable(int capability) {
        GLES30.glEnable(capability);
    }

    @Override
    public void glDisable(int capability) {
        GLES30.glDisable(capability);
    }

    @Override
    public void glBlendEquation(int mode) {
        GLES30.glBlendEquation(mode);
    }

    @Override
    public void glBlendFunc(int sFactor, int dFactor) {
        GLES30.glBlendFunc(sFactor, dFactor);
    }

    @Override
    public void glClearColor(float r, float g, float b, float a) {
        GLES30.glClearColor(r, g, b, a);
    }

    @Override
    public void glClear(int buffersMask) {
        GLES30.glClear(buffersMask);
    }

    @Override
    public void glStencilMask(int mask) {
        GLES30.glStencilMask(mask);
    }

    @Override
    public void glClearStencil(int index) {
        GLES30.glClearStencil(index);
    }

    @Override
    public void glStencilFunc(int func, int ref, int mask) {
        GLES30.glStencilFunc(func, ref, mask);
    }

    @Override
    public void glStencilOp(int sFail, int dpFail, int dpPass) {
        GLES30.glStencilOp(sFail, dpFail, dpPass);
    }

    @Override
    public void glColorMask(boolean r, boolean g, boolean b, boolean a) {
        GLES30.glColorMask(r, g, b, a);
    }

    @Override
    public void glDepthMask(boolean enabled) {
        GLES30.glDepthMask(enabled);
    }

    @Override
    public int glCreateShader(int shaderType) {
        return GLES30.glCreateShader(shaderType);
    }

    @Override
    public void glDeleteShader(int shader) {
        GLES30.glDeleteShader(shader);
    }

    @Override
    public void glShaderSource(int shader, String source) {
        GLES30.glShaderSource(shader, source);
    }

    @Override
    public void glCompileShader(int shader) {
        GLES30.glCompileShader(shader);
    }

    @Override
    public void glGetShaderInfoLog(int shader, int logBufferLength, IntBuffer length, ByteBuffer logBuffer) {
        var log = GLES30.glGetShaderInfoLog(shader);
        logBuffer.put(log.getBytes());
    }

    @Override
    public void glGetShaderiv(int shader, int parameter, IntBuffer result) {
        GLES30.glGetShaderiv(shader, parameter, result);
    }

    @Override
    public int glCreateProgram() {
        return GLES30.glCreateProgram();
    }

    @Override
    public void glUseProgram(int program) {
        GLES30.glUseProgram(program);
    }

    @Override
    public void glDeleteProgram(int program) {
        GLES30.glDeleteProgram(program);
    }

    @Override
    public void glAttachShader(int program, int shader) {
        GLES30.glAttachShader(program, shader);
    }

    @Override
    public void glLinkProgram(int program) {
        GLES30.glLinkProgram(program);
    }

    @Override
    public void glGetProgramiv(int program, int parameter, IntBuffer result) {
        GLES30.glGetProgramiv(program, parameter, result);
    }

    @Override
    public void glGetProgramInfoLog(int program, int logBufferLength, IntBuffer length, ByteBuffer logBuffer) {
        var log = GLES30.glGetProgramInfoLog(program);
        logBuffer.put(log.getBytes());
    }

    @Override
    public int glGetUniformLocation(int program, String uniformName) {
        return GLES30.glGetUniformLocation(program, uniformName);
    }

    @Override
    public void glUniformMatrix4fv(int uniformLocation, int count, boolean transpose, FloatBuffer matrixBuffer) {
        GLES30.glUniformMatrix4fv(uniformLocation, count, transpose, matrixBuffer);
    }

    @Override
    public void glPixelStorei(int parameter, int value) {
        GLES30.glPixelStorei(parameter, value);
    }

    @Override
    public boolean isExtensionAvailable(String extension) {
        return extensions != null && extensions.contains(extension);
    }

    @Override
    public int glGetError() {
        return GLES30.glGetError();
    }

    @Override
    public void glPolygonMode(int glFrontAndBack, int mode) {
//        GLES30.glPolygonMode(glFrontAndBack, mode);
        throw new UnsupportedOperationException("");
    }

    @Override
    public String glGetString(int target) {
        return GLES30.glGetString(target);
    }
}


