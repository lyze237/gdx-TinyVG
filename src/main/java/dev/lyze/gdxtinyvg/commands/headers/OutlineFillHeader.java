package dev.lyze.gdxtinyvg.commands.headers;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.LittleEndianInputStream;
import dev.lyze.gdxtinyvg.TinyVG;
import dev.lyze.gdxtinyvg.enums.StyleType;
import dev.lyze.gdxtinyvg.styles.Style;
import dev.lyze.gdxtinyvg.types.TinyVGIO;
import dev.lyze.gdxtinyvg.types.Unit;
import java.io.IOException;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.var;

@EqualsAndHashCode(callSuper = true)
@Getter
public class OutlineFillHeader<TData> extends CommandHeader<TData> {
    private Style secondaryStyle;
    private float lineWidth;

    public OutlineFillHeader(Class<TData> clazz) {
        super(clazz);
    }

    @Override
    public OutlineFillHeader<TData> read(LittleEndianInputStream stream, StyleType primaryStyleType, TinyVG tinyVG)
            throws IOException {
        var rectangleStyleByte = stream.readUnsignedByte();

        var rectangleCounts = (rectangleStyleByte & 0b0011_1111) + 1;
        var secondaryStyleType = StyleType.valueOf((rectangleStyleByte & 0b1100_0000) >> 6);

        primaryStyle = primaryStyleType.read(stream, tinyVG);
        secondaryStyle = secondaryStyleType.read(stream, tinyVG);

        lineWidth = new Unit(stream, tinyVG.getHeader().getCoordinateRange(), tinyVG.getHeader().getFractionBits())
                .convert();

        data = new Array<>(rectangleCounts);

        for (int i = 0; i < data.items.length; i++)
            data.add(TinyVGIO.read(getClazz(), stream, tinyVG.getHeader().getCoordinateRange(),
                    tinyVG.getHeader().getFractionBits()));

        return this;
    }
}
