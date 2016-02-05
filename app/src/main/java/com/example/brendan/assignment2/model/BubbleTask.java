package com.example.brendan.assignment2.model;

import android.renderscript.Allocation;
import android.renderscript.RenderScript;

import com.example.brendan.assignment2.ScriptC_bubble;

/**
 * Created by Brendan on 2/3/2016.
 */
public class BubbleTask extends RenderScriptSyncTask {

    private ScriptC_bubble script;

    @Override
    protected void runScript(Allocation in, Allocation out) {
        script.forEach_bubble(in, out);
    }

    @Override
    protected void createScript(RenderScript renderScript) {
        script = new ScriptC_bubble(renderScript);
        script.set_image(inAllocation);
    }
}
