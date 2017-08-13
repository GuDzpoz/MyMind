package moe.gensokyoradio.liberty.mymind;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import moe.gensokyoradio.liberty.mymind.tree.ClickableNode;
import moe.gensokyoradio.liberty.mymind.tree.MyMindTree;
import moe.gensokyoradio.liberty.mymind.tree.MyNode;
import moe.gensokyoradio.liberty.mymind.tree.PartitionLayout;

public class MindTreeActivity extends AppCompatActivity {
    static final String MIND_PATH_KEY = "mind";

    private String mapPath;
    private MyMindTree tree;
    private PartitionLayout treeLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mind_tree);

        mapPath = this.getIntent().getStringExtra(MIND_PATH_KEY);

        tree = MyMindTree.fromJSON(readAll(mapPath));

        treeLayout = (PartitionLayout) findViewById(R.id.treeLayout);
        Log.i("TITLE", tree.getRootNode().getTitle());
        treeLayout.setNode(
                tree.getRootNode(),
                new PartitionLayout.OnNodeClickListener() {
                    @Override
                    public void onClick(View v, PartitionLayout layout, Button button, MyNode node) {
                        Log.i("OnClick", node.getPath().getAbsolutePath());
                    }
                }, this);
    }

    private String readAll(String path) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            InputStreamReader reader = new InputStreamReader(openFileInput(mapPath));
            int c = 0;
            while ((c = reader.read()) != -1) {
                stringBuilder.append((char) c);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            this.finish();
        } catch (IOException e) {
            e.printStackTrace();
            this.finish();
        }
        return stringBuilder.toString();
    }

    private void writeAll(String path, String string) {
        try {
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(openFileOutput(mapPath, MODE_PRIVATE)));
            writer.write(string);
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            this.finish();
        } catch (IOException e) {
            e.printStackTrace();
            this.finish();
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_node_operations, menu);
        currentButton = (ClickableNode) v;
        Log.i("ON", currentButton.getNode().getPath().getPath().toString());
    }

    private ClickableNode currentButton;
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.addNode:
                final EditText editText = new EditText(this);
                editText.setHint(R.string.title_input_hint);
                new AlertDialog.Builder(this)
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
                                treeLayout.addNode(currentButton.getNode().getPath(), new MyNode(title));
                            }
                        }
                    })
                    .show();
                return true;
            case R.id.editNode:
                return true;
            case R.id.deleteNode:
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_map_operations, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.saveOption) {
            writeAll(mapPath, this.tree.toJSON());
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
