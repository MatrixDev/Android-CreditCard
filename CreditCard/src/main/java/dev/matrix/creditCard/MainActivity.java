package dev.matrix.creditCard;

import android.app.Activity;
import android.os.Bundle;
import android.widget.EditText;

/**
 * @author rostyslav.lesovyi
 */
public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

		EditText editText = (EditText) findViewById(R.id.input);
		editText.setTransformationMethod(new CreditCardTransformation());
    }
}
