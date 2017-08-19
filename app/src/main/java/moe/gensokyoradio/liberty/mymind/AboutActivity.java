package moe.gensokyoradio.liberty.mymind;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        setTitle(getString(R.string.about_title));
        Toast.makeText(this, R.string.load_license_toast, Toast.LENGTH_SHORT).show();
        final Handler handler = new Handler();
        new Thread(new Runnable() {
            @Override
            public void run() {
                final String license = getLicense();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        ((TextView)findViewById(R.id.aboutTextview)).setText(license);
                    }
                });
            }
        }).start();
    }

    private String getLicense() {
        try {
            return Util.read(getResources().openRawResource(R.raw.license));
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
