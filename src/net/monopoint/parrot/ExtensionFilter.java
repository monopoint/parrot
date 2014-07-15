package net.monopoint.parrot;

import java.io.File;
import java.io.FilenameFilter;

/**
 * Filters out files that meet the requirements of the provided extension
 * To only get .rar files, the provided extension should be ".rar";
 */
public class ExtensionFilter implements FilenameFilter {

    String extension;

    public ExtensionFilter(String extension) {
        this.extension = extension;
    }

    @Override
    public boolean accept(File dir, String name) {
        return name.endsWith(extension);
    }
}
