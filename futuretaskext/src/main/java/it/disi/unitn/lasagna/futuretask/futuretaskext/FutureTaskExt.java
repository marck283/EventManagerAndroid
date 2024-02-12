package it.disi.unitn.lasagna.futuretask.futuretaskext;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

public class FutureTaskExt<V> extends FutureTask<V> {
    private final Bundle b;

    private final Callable<V> callable;

    public FutureTaskExt(Callable<V> callable, @NotNull Bundle b) {
        super(callable);
        this.b = b;
        this.callable = callable;
    }

    public FutureTaskExt(Callable<V> callable) {
        super(callable);
        b = null;
        this.callable = callable;
    }

    public void run() {
        try {
            callable.call();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
