package moe.gensokyoradio.liberty.mymind;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;

import moe.gensokyoradio.liberty.mymind.AboutActivity;
import moe.gensokyoradio.liberty.mymind.MindTreeActivity;
import moe.gensokyoradio.liberty.mymind.R;
import moe.gensokyoradio.liberty.mymind.SettingsActivity;
import moe.gensokyoradio.liberty.mymind.Util;

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

public class MindTreeDialog extends Dialog implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {
    interface OnMindTreeChosenListener {
        void onChosen(String path);
    }

    private static final String MAP_PATHS_KEY = "titles";
    private ArrayList<String> titles;
    private ArrayAdapter<String> adapter;
    private OnMindTreeChosenListener listener;

    public MindTreeDialog(@NonNull Context context) {
        super(context);
    }

    public MindTreeDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
    }

    protected MindTreeDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences preferences = getSharedPreferences();
        Set<String> maps = preferences.getAll().keySet();
        titles = new ArrayList<>(maps);

        final ListView listView = new ListView(getContext());
        adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_expandable_list_item_1, titles);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
        listView.setOnItemLongClickListener(this);
        this.setContentView(listView);

        Button button = new Button(getContext());
        button.setText(R.string.button_add_map);
        this.addContentView(button, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final EditText editText = new EditText(getContext());
                editText.setHint(R.string.input_title_hint);
                new AlertDialog.Builder(getContext())
                        .setCancelable(true)
                        .setView(editText)
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String title = editText.getText().toString();
                                if(title.isEmpty()) {
                                    dialog.cancel();
                                }
                                else {
                                    SharedPreferences preferences = getSharedPreferences();
                                    if(preferences.contains(title)) {
                                        dialog.cancel();
                                    }
                                    else {
                                        String path = "MyMind_" + title + ".json";
                                        preferences.edit().putString(title, path).apply();
                                        titles.add(title);
                                        adapter.notifyDataSetChanged();
                                        initializeMap(title, path);
                                    }
                                }
                            }
                        })
                        .show();
            }
        });
    }

    private void initializeMap(String title, String path) {
        try {
            Util.writeAll(getContext(), path, "{\"title\":\"" + title + "\"," + "\"attributes\":{},\"children\":[]}");
        } catch (IOException e) {
            e.printStackTrace();
            this.cancel();
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        SharedPreferences preferences = getSharedPreferences();
        String title = this.titles.get(position);
        preferences.edit().remove(title).apply();
        this.titles.remove(title);
        adapter.notifyDataSetChanged();
        return true;
    }

    public void setOnMindTreeChosenListener(OnMindTreeChosenListener listener) {
        this.listener = listener;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        SharedPreferences preferences = getSharedPreferences();
        if(listener != null) {
            listener.onChosen(preferences.getString(this.titles.get(position), null));
        }
        this.dismiss();
    }

    private SharedPreferences getSharedPreferences() {
        return getContext().getSharedPreferences(MAP_PATHS_KEY, Context.MODE_PRIVATE);
    }
}
