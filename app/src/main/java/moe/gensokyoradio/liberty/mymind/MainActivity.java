package moe.gensokyoradio.liberty.mymind;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Set;

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

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemLongClickListener, AdapterView.OnItemClickListener {
    private static final String MAP_PATHS_KEY = "titles";
    private ArrayList<String> titles;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SharedPreferences preferences = getSharedPreferences(MAP_PATHS_KEY, MODE_PRIVATE);
        Set<String> maps = preferences.getAll().keySet();
        titles = new ArrayList<>(maps);

        final ListView listView = (ListView)findViewById(R.id.treeList);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_expandable_list_item_1, titles);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
        listView.setOnItemLongClickListener(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.addMind);
        final Context context = this;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final EditText editText = new EditText(context);
                editText.setHint(R.string.input_title_hint);
                new AlertDialog.Builder(context)
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
                                    SharedPreferences preferences = getSharedPreferences(MAP_PATHS_KEY, MODE_PRIVATE);
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
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(openFileOutput(path, MODE_PRIVATE)));
            writer.write(""
                    + "{\"title\":\"" + title + "\"," + "\"attributes\":{},\"children\":["
                    + "{\"title\":\"" + "test1" + "\"," + "\"attributes\":{},\"children\":[]},"
                    + "{\"title\":\"" + "test2" + "\"," + "\"attributes\":{},\"children\":[]},"
                    + "{\"title\":\"" + "LinuxBanzai" + "\"," + "\"attributes\":{},\"children\":[]}"
                    + "]}"
            );
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.action_about) {
            startActivity(new Intent(this, AboutActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        SharedPreferences preferences = getSharedPreferences(MAP_PATHS_KEY, MODE_PRIVATE);
        String title = this.titles.get(position);
        preferences.edit().remove(title).apply();
        this.titles.remove(title);
        adapter.notifyDataSetChanged();
        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        SharedPreferences preferences = getSharedPreferences(MAP_PATHS_KEY, MODE_PRIVATE);
        Intent intent = new Intent(this, MindTreeActivity.class);
        intent.putExtra(MindTreeActivity.MIND_PATH_KEY, preferences.getString(this.titles.get(position), null));
        startActivity(intent);
    }
}
