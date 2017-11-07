package nl.anwb.menoa;

import android.view.View;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import nl.anwb.menoa.ui.MainActivity;

import static org.mockito.Mockito.mock;

/**
 * De Noodmeld test class
 */
public class NoodmeldTest {

    @Mock
    private MainActivity mainActivity;
    private View view;

    @Before
    public void setup(){
        mainActivity = mock(MainActivity.class);
        view = mock(View.class);
    }

    @Test
    public void addition_isCorrect() throws Exception {
        mainActivity.onNoodmeldingPressed(view);

    }
}
