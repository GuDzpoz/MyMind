package moe.gensokyoradio.liberty.mymind;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Toast;

import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import hu.scythe.droidwriter.DroidWriterEditText;
import moe.gensokyoradio.liberty.mymind.content.ContentLayout;
import moe.gensokyoradio.liberty.mymind.content.LocalImageView;

public class ContentActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "ContentActivity";
    public static final String CONTENT_PATH_KEY = "path";
    public static final String MAP_FILENAME_KEY = "fileName";
    private DroidWriterEditText editText;
    private ContentLayout contentLayout;
    private String path;
    private String mapFileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);

        path = getIntent().getStringExtra(CONTENT_PATH_KEY);
        mapFileName = getIntent().getStringExtra(MAP_FILENAME_KEY);
        contentLayout = (ContentLayout) findViewById(R.id.contentLayout);
        contentLayout.setOnContentClickedListener(this);
        contentLayout.setOnContextCreatedListener(this);

        try {
            String content = Util.readAll(path);
            contentLayout.load(content);
        } catch (IOException e) {
            e.printStackTrace();
            this.finish();
        }

        findViewById(R.id.action_text).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contentLayout.addText();
            }
        });

        findViewById(R.id.action_heading).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contentLayout.addHeading();
            }
        });

        findViewById(R.id.action_insert_image).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImage();
            }
        });

        findViewById(R.id.action_insert_link).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contentLayout.addLink();
            }
        });

        findViewById(R.id.action_bold).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editText != null) {
                    editText.toggleStyle(DroidWriterEditText.STYLE_BOLD);
                    editText.requestFocus();
                }
            }
        });

        findViewById(R.id.action_italic).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editText != null) {
                    editText.toggleStyle(DroidWriterEditText.STYLE_ITALIC);
                    editText.requestFocus();
                }
            }
        });

        findViewById(R.id.action_underline).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editText != null) {
                    editText.toggleStyle(DroidWriterEditText.STYLE_UNDERLINED);
                    editText.requestFocus();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_content_operations, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.saveOption) {
            try {
                Util.writeAll(path, contentLayout.save());
            } catch (IOException e) {
                e.printStackTrace();
                this.finish();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void getImage() {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .start(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                Uri resultUri = result.getUri();
                String path = getPhysicalPath(resultUri);
                try {
                    path = backup(path);
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Image load failed. Try editing the settings.", Toast.LENGTH_LONG).show();
                }
                contentLayout.addImage(path);
            }
        }
    }

    @Nullable
    private String getPhysicalPath(Uri uri) {
        if (uri.getScheme().equals("content")) {
            return getRealPathFromContentURI(uri);
        } else {
            String path = null;
            try {
                path = URLDecoder.decode(uri.getPath(), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                Toast.makeText(this, "UTF-8 is not supported on your device.", Toast.LENGTH_LONG).show();
            }
            return path;
        }
    }

    public String getRealPathFromContentURI(Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] projection = {MediaStore.Images.Media.DATA};
            cursor = this.getContentResolver().query(contentUri, projection, null, null, null);
            if (cursor == null) {
                return null;
            }
            else {
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    private View contextMenuView;
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.menu_content_item_operations, menu);
        if(contentLayout.getIndex(v) == 0) {
            menu.findItem(R.id.up).setEnabled(false);
        }
        if(contentLayout.getIndex(v) == contentLayout.getChildCount() - 1) {
            menu.findItem(R.id.down).setEnabled(false);
        }
        contextMenuView = v;
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        super.onContextItemSelected(item);
        int id = item.getItemId();
        switch (id) {
            case R.id.up:
                contentLayout.move(contextMenuView, contentLayout.getIndex(contextMenuView) - 1);
                break;
            case R.id.down:
                contentLayout.move(contextMenuView, contentLayout.getIndex(contextMenuView) + 1);
                break;
            case R.id.delete:
                contentLayout.removeView(contextMenuView);
                break;
        }
        return true;
    }

    private String backup(String path) throws IOException {
        File src = new File(path);
        String newPath = TheApplication.getPath(mapFileName, src.getName());
        File dst = new File(newPath);
        Util.directorySaveCopy(src, dst);
        return newPath;
    }

    @Override
    public void onClick(View view) {
        if(view instanceof DroidWriterEditText) {
            this.editText = (DroidWriterEditText)view;
            findViewById(R.id.action_bold).setEnabled(true);
            findViewById(R.id.action_italic).setEnabled(true);
            findViewById(R.id.action_underline).setEnabled(true);
        }
        else {
            findViewById(R.id.action_bold).setEnabled(false);
            findViewById(R.id.action_italic).setEnabled(false);
            findViewById(R.id.action_underline).setEnabled(false);

            if(view instanceof LocalImageView) {
                WebView webView = new WebView(this);
                webView.getSettings().setLoadWithOverviewMode(true);
                webView.getSettings().setUseWideViewPort(true);
                webView.getSettings().setSupportZoom(true);
                webView.getSettings().setBuiltInZoomControls(true);
                webView.getSettings().setDisplayZoomControls(true);
                webView.setBackgroundColor(0x00000000);
                webView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                webView.loadUrl("file://" + ((LocalImageView)view).getPath());
                new AlertDialog.Builder(this)
                        .setView(webView)
                        .show();
            }
        }
    }
}
