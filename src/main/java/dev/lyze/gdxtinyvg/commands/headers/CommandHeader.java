package dev.lyze.gdxtinyvg.commands.headers;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.LittleEndianInputStream;
import dev.lyze.gdxtinyvg.TinyVG;
import dev.lyze.gdxtinyvg.enums.StyleType;
import dev.lyze.gdxtinyvg.styles.Style;
import dev.lyze.gdxtinyvg.utils.StreamUtils;
import java.io.IOException;
import lombok.Getter;
import lombok.var;

public abstract class CommandHeader<TData> {
    @Getter private final Class<TData> clazz;

    @Getter protected Array<TData> data;

    @Getter protected Style primaryStyle;

    public CommandHeader(Class<TData> clazz) {
        this.clazz = clazz;
    }

    public CommandHeader<TData> read(LittleEndianInputStream stream, StyleType primaryStyleType, TinyVG tinyVG)
            throws IOException {
        var count = StreamUtils.readVarUInt(stream) + 1;

        primaryStyle = primaryStyleType.read(stream, tinyVG);

        data = new Array<>(count);

        return this;
    }
}
