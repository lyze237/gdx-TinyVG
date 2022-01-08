package dev.lyze.gdxtinyvg.commands.headers;

import dev.lyze.gdxtinyvg.TinyVG;

public class FillPathHeader extends PathHeader {
    public FillPathHeader(TinyVG tinyVG) {
        super(tinyVG);
    }

    @Override
    protected boolean shouldCalculateTriangles() {
        return true;
    }
}
