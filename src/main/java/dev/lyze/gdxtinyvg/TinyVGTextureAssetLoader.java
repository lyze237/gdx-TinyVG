package dev.lyze.gdxtinyvg;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.SynchronousAssetLoader;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import dev.lyze.gdxtinyvg.drawers.TinyVGShapeDrawer;
import lombok.*;

public class TinyVGTextureAssetLoader
        extends SynchronousAssetLoader<TinyVGTextureAssetLoader.Result, TinyVGTextureAssetLoader.Parameters> {
    public TinyVGTextureAssetLoader() {
        super(new InternalFileHandleResolver());
    }

    public TinyVGTextureAssetLoader(FileHandleResolver resolver) {
        super(resolver);
    }

    @Override
    public Result load(AssetManager assetManager, String fileName, FileHandle file, Parameters parameter) {
        var tvg = assetManager.get(fileName, TinyVG.class);
        tvg.setScale(parameter.scaleX, parameter.scaleY);
        tvg.setCurvePoints(parameter.curvePoints);

        return new Result(tvg, TinyVGIO.toTextureRegion(tvg, parameter.getShapeDrawer()));
    }

    @Override
    public Array<AssetDescriptor> getDependencies(String fileName, FileHandle file, Parameters parameter) {
        var assetDescriptors = new Array<AssetDescriptor>();
        assetDescriptors.add(new AssetDescriptor(fileName, TinyVG.class));
        return assetDescriptors;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class Parameters extends AssetLoaderParameters<Result> {
        private TinyVGShapeDrawer shapeDrawer;
        private float scaleX, scaleY;
        private int curvePoints;

        public Parameters(TinyVGShapeDrawer shapeDrawer) {
            this(shapeDrawer, 1, 1, 1);
        }

        public Parameters(TinyVGShapeDrawer shapeDrawer, float scaleX, float scaleY) {
            this.shapeDrawer = shapeDrawer;
            this.scaleX = scaleX;
            this.scaleY = scaleY;
        }
    }

    @AllArgsConstructor
    @Getter
    public static class Result {
        private TinyVG tvg;
        private TextureRegion textureRegion;
    }
}
