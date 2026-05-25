package dev.gusramirez.ilfavorito;

import androidx.fragment.app.Fragment;

public interface Manageable<T> {
    Fragment onCreateEntity();
    void onEditEntity(T item);
    void onDeleteEntity(T item);
}
