package com.daniillshei.sceditor;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;

public class MyGLSurfaceView extends GLSurfaceView {
    private final ScaleGestureDetector scaleDetector;
    private float lastTouchX, lastTouchY;
    private boolean dragging = false;

    public MyGLSurfaceView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
//        System.loadLibrary("VkLayer_GLES_RenderDoc");
        setEGLContextClientVersion(3); // Use OpenGL ES 3.0
        setRenderer(StageGLES.getInstance());

        // For continuous rendering during interaction
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);

        // Pinch zoom detector
        scaleDetector = new ScaleGestureDetector(context, new ScaleGestureDetector.SimpleOnScaleGestureListener() {
            @Override
            public boolean onScale(ScaleGestureDetector detector) {
                float scaleFactor = detector.getScaleFactor();
                var stageGLES = StageGLES.getInstance();
                stageGLES.doInRenderThread(() -> {
                    stageGLES.getCamera().getZoom().zoomTo(scaleFactor);
                    stageGLES.updatePMVMatrix();
                });
                return true;
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        scaleDetector.onTouchEvent(event);

        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                lastTouchX = event.getX();
                lastTouchY = event.getY();
                dragging = true;
                break;

            case MotionEvent.ACTION_MOVE:
                if (dragging && event.getPointerCount() == 1 && !scaleDetector.isInProgress()) {
                    float dx = event.getX() - lastTouchX;
                    float dy = event.getY() - lastTouchY;
                    lastTouchX = event.getX();
                    lastTouchY = event.getY();
                    var stageGLES = StageGLES.getInstance();
                    stageGLES.doInRenderThread(() -> {
                        var camera = stageGLES.getCamera();
                        camera.addOffset(-dx / camera.getZoom().getPointSize(), -dy / camera.getZoom().getPointSize());
                        stageGLES.updatePMVMatrix();
                    });
                }
                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                dragging = false;
                break;
        }

        return true;
    }
}
