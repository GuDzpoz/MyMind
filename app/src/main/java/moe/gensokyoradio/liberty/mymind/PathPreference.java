package moe.gensokyoradio.liberty.mymind;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Environment;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.io.FileFilter;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/*
 *     This file is part of MyMind.
 *
 *     MyMind is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     MyMind is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with MyMind. If not, see <http://www.gnu.org/licenses/>.
 */

public class PathPreference extends DialogPreference implements AdapterView.OnItemClickListener {

    public PathPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setDefaultValue(TheApplication.DEFAULT_STORAGE_PATH);
    }

    public PathPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        setDefaultValue(TheApplication.DEFAULT_STORAGE_PATH);
    }

    private ListView listView;
    private File dir;
    @Override
    protected View onCreateDialogView() {
        this.dir = Environment.getExternalStorageDirectory();
        this.listView = new ListView(getContext());
        listView.setAdapter(new DirListAdapter(getContext(), dir));
        listView.setOnItemClickListener(this);
        return listView;
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        super.onDialogClosed(positiveResult);

        if(positiveResult) {
            // TODO: Consider moving the existing files in the old location to the new one
            this.getEditor().putString(this.getKey(), dir.getAbsolutePath()).apply();
        }
    }

    private void update(File newDirectory) {
        this.setSummary(newDirectory.getAbsolutePath());

        listView.setAdapter(new DirListAdapter(this.getContext(), newDirectory));
        listView.postInvalidate();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        File file = (File)adapterView.getAdapter().getItem(i);
        this.dir = file;
        if(file.isDirectory()) {
            this.update(file);
        }
    }
}

class DirListAdapter extends BaseAdapter {
    private File parent;
    private File[] directories;
    private Context context;

    public DirListAdapter(Context context, File directory) {
        super();
        this.directories = directory.listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                return file.isDirectory();
            }
        });
        Comparator<File> byName = new Comparator<File>() {
            @Override
            public int compare(File file, File t1) {
                return file.getName().compareTo(t1.getName());
            }
        };
        Arrays.sort(directories, byName);
        this.parent = directory.getParentFile();
        this.context = context;
    }

    @Override
    public int getCount() {
        return this.directories.length + 1; // including parent
    }

    @Override
    public Object getItem(int i) {
        if (i == 0) {
            return parent;
        } else {
            return this.directories[i - 1];
        }
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        TextView textView = new TextView(this.context);
        textView.setHeight(textView.getLineHeight() * 3);
        textView.setGravity(Gravity.CENTER);
        if (i == 0) {
            textView.setText("..");
            return textView;
        }
        File file = (File) this.getItem(i);
        if (file.isDirectory()) {
            textView.setText(file.getName() + "/");
        } else {
            textView.setText(file.getName());
        }
        return textView;
    }
}


