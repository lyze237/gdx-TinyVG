package dev.lyze.gdxtinyvg.commands.headers;

import dev.lyze.gdxtinyvg.TinyVG;

public class OutlinePathHeader extends PathHeader {
    public OutlinePathHeader(TinyVG tinyVG) {
        super(tinyVG);
    }

    @Override
    protected boolean shouldCalculateTriangles() {
        return false;
    }
}
