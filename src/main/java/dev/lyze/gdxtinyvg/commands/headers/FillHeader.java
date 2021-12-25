package dev.lyze.gdxtinyvg.commands.headers;

import com.badlogic.gdx.utils.LittleEndianInputStream;
import dev.lyze.gdxtinyvg.TinyVG;
import dev.lyze.gdxtinyvg.enums.StyleType;
import dev.lyze.gdxtinyvg.types.TinyVGIO;
import java.io.IOException;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode(callSuper = true)
@Getter
public class FillHeader<TData> extends CommandHeader<TData> {
    public FillHeader(Class<TData> clazz) {
        super(clazz);
    }

    @Override
    public FillHeader<TData> read(LittleEndianInputStream stream, StyleType primaryStyleType, TinyVG tinyVG)
            throws IOException {

        super.read(stream, primaryStyleType, tinyVG);

        for (int i = 0; i < data.items.length; i++)
            data.add(TinyVGIO.read(getClazz(), stream, tinyVG.getHeader().getCoordinateRange(),
                    tinyVG.getHeader().getFractionBits()));

        return this;
    }
}
