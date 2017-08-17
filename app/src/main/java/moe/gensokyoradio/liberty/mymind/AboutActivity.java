package moe.gensokyoradio.liberty.mymind;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStreamReader;

import hu.scythe.droidwriter.DroidWriterEditText;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        setTitle(getString(R.string.about_title));
    }

    private String getLicense() {
        InputStreamReader reader = new InputStreamReader(getResources().openRawResource(R.raw.license));
        StringBuilder sb = new StringBuilder(512);
        try {
            int c = 0;
            while ((c = reader.read()) != -1) {
                sb.append((char) c);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return sb.toString();
    }
}
