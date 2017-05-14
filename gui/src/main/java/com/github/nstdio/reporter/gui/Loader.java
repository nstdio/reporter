package com.github.nstdio.reporter.gui;

import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.InputStream;

public class Loader {

    public static <T> void load(T instance, String view) {

        FXMLLoader fxmlLoader = new FXMLLoader(Loader.class.getResource("/view/" + view));
        fxmlLoader.setRoot(instance);
        fxmlLoader.setController(instance);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    private static InputStream asStream(String path) {
        return Loader.class.getResourceAsStream(path);
    }

    public static ImageView icon(String named) {
        return icon(named, "png");
    }

    /**
     * Loads image from application resources.
     *
     * @param named The image name without extension.
     * @param ext   The image extension without dot at start.
     *
     * @return The loaded image resource wrapped in {@code ImageView}
     */
    public static ImageView icon(String named, String ext) {
        if (!StringUtils.endsWithIgnoreCase(named, ext)) {
            named = String.format("%s.%s", named, ext);
        }

        final String imagePath = String.format("/icon/%s", named);

        final Image image = new Image(asStream(imagePath));

        return new ImageView(image);
    }
}
