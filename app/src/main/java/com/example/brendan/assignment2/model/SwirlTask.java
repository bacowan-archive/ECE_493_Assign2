package com.example.brendan.assignment2.model;

import android.renderscript.Allocation;
import android.renderscript.RenderScript;

import com.example.brendan.assignment2.ScriptC_twirl;

/**
 * Created by Brendan on 2/3/2016.
 */
public class SwirlTask extends RenderScriptSyncTask {

    private ScriptC_twirl script;

    @Override
    protected void runScript(Allocation in, Allocation out) {
        script.forEach_twirl(in, out);
    }

    @Override
    protected void createScript(RenderScript renderScript) {
        script = new ScriptC_twirl(renderScript);
        script.set_image(inAllocation);
    }
}
