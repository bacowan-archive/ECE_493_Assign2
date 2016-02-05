package com.example.brendan.assignment2.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.renderscript.Allocation;
import android.renderscript.RenderScript;

/**
 * Created by Brendan on 2/3/2016.
 */
public abstract class RenderScriptSyncTask {

    protected Allocation inAllocation;
    protected Bitmap outBitmap;
    protected Allocation outAllocation;

    public void createRenderScript(Bitmap image, Context c) {
        RenderScript renderScript = RenderScript.create(c);
        inAllocation = Allocation.createFromBitmap(renderScript, image);
        outBitmap = Bitmap.createBitmap(image.getWidth(), image.getHeight(), image.getConfig());
        outAllocation = Allocation.createFromBitmap(renderScript, outBitmap);
        createScript(renderScript);
    }

    public Bitmap execute() {
        runScript(inAllocation, outAllocation);
        outAllocation.copyTo(outBitmap);
        return outBitmap;
    }

    protected abstract void runScript(Allocation in, Allocation out);

    protected abstract void createScript(RenderScript renderScript);

}
