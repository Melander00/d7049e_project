package com.example.brainslop.core.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;

import java.io.File;

public class ExternalAssetsResolver implements FileHandleResolver {
    private final FileHandle root;

    public ExternalAssetsResolver(String path) {
        File baseDir = new File(System.getProperty("user.dir"));
        File resolved = new File(baseDir, path);

        this.root = Gdx.files.absolute(resolved.getAbsolutePath());
        Gdx.app.log("Resolver", "Root: " + root.path());
    }

    @Override
    public FileHandle resolve(String fileName) {
        return root.child(fileName);
    }
}