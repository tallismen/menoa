package nl.anwb.menoa;

import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;

public interface ActionBarSupport {

    void setSupportActionBar(@Nullable Toolbar toolbar);

    ActionBar getSupportActionBar();
}
