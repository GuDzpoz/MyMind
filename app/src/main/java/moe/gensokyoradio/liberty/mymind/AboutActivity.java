package moe.gensokyoradio.liberty.mymind;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStreamReader;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        setTitle(getString(R.string.about_title));

        TextView about = (TextView) this.findViewById(R.id.aboutTextview);
        about.setText(getLicense());
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
