package dev.lyze.gdxtinyvg;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.LittleEndianInputStream;
import dev.lyze.gdxtinyvg.commands.Command;
import dev.lyze.gdxtinyvg.enums.CommandType;
import dev.lyze.gdxtinyvg.enums.StyleType;
import lombok.var;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

public class TinyVGAssetLoader extends AsynchronousAssetLoader<TinyVG, TinyVGAssetLoader.TinyVGParameter> {
    private TinyVG tvg;

    public TinyVGAssetLoader() {
        super(new InternalFileHandleResolver());
    }

    public TinyVGAssetLoader(FileHandleResolver resolver) {
        super(resolver);
    }

    @Override
    public void loadAsync(AssetManager manager, String fileName, FileHandle file, TinyVGParameter parameter) {
        tvg = load(file.read());
    }

    @Override
    public TinyVG loadSync(AssetManager manager, String fileName, FileHandle file, TinyVGParameter parameter) {
        return tvg;
    }

    public TinyVG load(String fileName) {
        return load(resolve(fileName).read());
    }

    public TinyVG load(InputStream stream) {
        try {
            return loadInternal(new LittleEndianInputStream(stream));
        } catch (IOException e) {
            throw new GdxRuntimeException(e);
        }
    }

    private TinyVG loadInternal(LittleEndianInputStream stream) throws IOException {
        var header = new TinyVGHeader();
        header.read(stream);

        var tinyVg = new TinyVG(header, readColorTable(header, stream));

        Command command;
        do {
            try {
                command = readCommand(stream, tinyVg);
                tinyVg.addCommand(command);
            } catch (EOFException e) {
                return tinyVg;
            }
        } while (command == null || command.getType() != CommandType.END_OF_DOCUMENT);

        return tinyVg;
    }

    private Command readCommand(LittleEndianInputStream stream, TinyVG tinyVG) throws IOException {
        var commandStyleByte = stream.readUnsignedByte();
        var commandType = CommandType.valueOf(commandStyleByte & 0b0011_1111);
        var styleType = StyleType.valueOf((commandStyleByte & 0b1100_0000) >> 6);

        return commandType.read(stream, styleType, tinyVG);
    }

    private Color[] readColorTable(TinyVGHeader header, LittleEndianInputStream stream) throws IOException {
        var table = new Color[header.getColorTableCount()];

        for (int i = 0; i < table.length; i++)
            table[i] = header.getColorEncoding().read(stream);

        return table;
    }

    @Override
    public Array<AssetDescriptor> getDependencies(String fileName, FileHandle file, TinyVGParameter parameter) {
        return null;
    }

    public static class TinyVGParameter extends AssetLoaderParameters<TinyVG> {

    }
}
