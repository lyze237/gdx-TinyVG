package dev.lyze.gdxtinyvg.lwjgl.setup;

import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

public abstract class BaseLwjglExtension implements BeforeAllCallback, ExtensionContext.Store.CloseableResource {

    @Override
    public void beforeAll(ExtensionContext context) throws Exception {
        // We need to use a unique key here, across all usages of this particular
        // extension.
        String uniqueKey = this.getClass().getName();
        Object value = context.getRoot().getStore(ExtensionContext.Namespace.GLOBAL).get(uniqueKey);
        if (value == null) {
            // First test container invocation.
            context.getRoot().getStore(ExtensionContext.Namespace.GLOBAL).put(uniqueKey, this);
            setup();
        }
    }

    // Callback that is invoked <em>exactly once</em>
    // before the start of <em>all</em> test containers.
    abstract void setup() throws InterruptedException;

    // Callback that is invoked <em>exactly once</em>
    // after the end of <em>all</em> test containers.
    // Inherited from {@code CloseableResource}
    public abstract void close() throws Throwable;
}
