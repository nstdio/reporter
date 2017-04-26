package com.github.nstdio.reporter.cli;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

class Manifests {
    private final Attributes attributes;

    Manifests() {
        attributes = getManifestInfo();
    }

    private static Attributes getManifestInfo() {
        Enumeration resEnum;
        try {
            resEnum = Thread.currentThread().getContextClassLoader().getResources(JarFile.MANIFEST_NAME);
            while (resEnum.hasMoreElements()) {
                try {
                    URL url = (URL) resEnum.nextElement();
                    InputStream is = url.openStream();
                    if (is != null) {
                        Manifest manifest = new Manifest(is);
                        return manifest.getMainAttributes();
                    }
                } catch (Exception e) {
                }
            }
        } catch (IOException e1) {
        }
        return null;
    }

    String getAttr(String attribute) {
        return attributes.getValue(attribute);
    }
}
