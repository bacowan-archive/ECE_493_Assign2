package com.example.brendan.assignment2.model;

import android.renderscript.Allocation;
import android.renderscript.RenderScript;

import com.example.brendan.assignment2.ScriptC_ripple;

/**
 * Created by Brendan on 2/3/2016.
 */
public class RippleTask extends RenderScriptSyncTask {

    private ScriptC_ripple script;

    @Override
    protected void runScript(Allocation in, Allocation out) {
        script.forEach_ripple(in, out);
    }

    @Override
    protected void createScript(RenderScript renderScript) {
        script = new ScriptC_ripple(renderScript);
        script.set_image(inAllocation);
    }
}
