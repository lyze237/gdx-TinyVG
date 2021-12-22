package dev.lyze.gdxtinyvg.lwjgl.setup;

import com.badlogic.gdx.ApplicationAdapter;

public class ApplicationAdapterWrapper extends ApplicationAdapter {
    public ApplicationAdapter currentAdapter;

    public ApplicationAdapterWrapper(ApplicationAdapter adapter) {
        currentAdapter = adapter;
    }

    @Override
    public void create() {
        currentAdapter.create();
    }

    @Override
    public void resize(int width, int height) {
        currentAdapter.resize(width, height);
    }

    @Override
    public void render() {
        currentAdapter.render();
    }

    @Override
    public void dispose() {
        currentAdapter.dispose();
    }
}
