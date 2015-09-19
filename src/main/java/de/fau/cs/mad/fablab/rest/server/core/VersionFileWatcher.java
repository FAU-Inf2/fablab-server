package de.fau.cs.mad.fablab.rest.server.core;

import de.fau.cs.mad.fablab.rest.server.configuration.VersionCheckConfiguration;
import de.fau.cs.mad.fablab.rest.server.resources.VersionCheckResource;

import java.io.File;
import java.nio.file.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * File Watcher using JAVA WatchService API to check if a file has been changed.
 */
public class VersionFileWatcher implements Runnable {

    private final File file;
    private AtomicBoolean stop = new AtomicBoolean(false);
    private VersionCheckResource versionCheckResource = null;
    private VersionCheckConfiguration config;

    public VersionFileWatcher (VersionCheckConfiguration versionCheckConfiguration, VersionCheckResource resource) {
        config = versionCheckConfiguration;
        file = new File(config.getPath());
        versionCheckResource = resource;
    }

    protected void doOnChange() {
        versionCheckResource.updateConfiguration(file);
    }

    public boolean isStopped() {
        return stop.get();
    }

    public void stopThread() {
        stop.set(true);
    }

    @Override
    public void run() {
        try (WatchService watcher = FileSystems.getDefault().newWatchService()) {

            Path path = file.toPath().getParent();
            path.register(watcher, StandardWatchEventKinds.ENTRY_MODIFY);

            while (!isStopped()) {
                WatchKey key;
                try {
                    key = watcher.poll(25, TimeUnit.MILLISECONDS);
                }
                catch (InterruptedException e) {
                    return;
                }

                if (key == null) {
                    Thread.yield();
                    continue;
                }

                for (WatchEvent<?> event : key.pollEvents()) {
                    WatchEvent.Kind<?> kind = event.kind();

                    @SuppressWarnings("unchecked")
                    WatchEvent<Path> ev = (WatchEvent<Path>) event;
                    Path filename = ev.context();

                    if (kind == StandardWatchEventKinds.OVERFLOW) {
                        Thread.yield();
                        continue;
                    }
                    else if (kind == java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY
                            && filename.toString().equals(file.getName())) {
                        doOnChange();
                    }

                    boolean valid = key.reset();

                    if (!valid) {
                        break;
                    }
                }
                Thread.yield();
            }
        } catch (Throwable e) {
            // Log or rethrow the error
        }
    }
}
