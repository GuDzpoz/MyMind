package moe.gensokyoradio.liberty.mymind;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

import moe.gensokyoradio.liberty.mymind.tree.ClickableNode;
import moe.gensokyoradio.liberty.mymind.tree.MyNode;

public class MindTreeActivity extends AppCompatActivity {
    // TODO: Set this activity as the launch activity, and consider what preferences should be set to allow user to choose the start map ( last opened or specific )
    public static final String MAP_FILENAME_ID = "fileName";

    private List<android.support.v4.app.Fragment> fragments = new ArrayList<>();
    private ViewPager viewPager;
    private FragmentPagerAdapter adapter;
    // TODO: If we are to add support for multi-fragment, remove the fileName field
    private String fileName;

    public static String getMapPath(String mapFileName) {
        return TheApplication.getPath(mapFileName, mapFileName);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mind_tree);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        adapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public android.support.v4.app.Fragment getItem(int position) {
                return fragments.get(position);
            }

            @Override
            public int getCount() {
                return fragments.size();
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return ((MindTreeFragment) getItem(position)).getPath();
            }
        };
        viewPager.setAdapter(adapter);

        this.fileName = getIntent().getStringExtra(MAP_FILENAME_ID);
        if (fileName != null) {
            addFragment(getMapPath(fileName));
        }
    }

    private void addFragment(String path) {
        MindTreeFragment fragment = new MindTreeFragment();
        fragment.setMap(this, path);
        fragments.add(fragment);
        adapter.notifyDataSetChanged();
    }

    private MindTreeFragment getCurrentFragment() {
        return ((MindTreeFragment) adapter.getItem(viewPager.getCurrentItem()));
    }

    private void removeCurrentFragment() {
        int index = viewPager.getCurrentItem();
        fragments.remove(index);
        adapter.notifyDataSetChanged();
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
                                if (title.isEmpty()) {
                                    dialog.cancel();
                                } else {
                                    getCurrentFragment().getTreeLayout().addNode(currentButton.getNode().getPath(), new MyNode(title));
                                }
                            }
                        })
                        .show();
                return true;
            case R.id.editNode:
                Intent intent = new Intent(this, ContentActivity.class);
                intent.putExtra(ContentActivity.MAP_FILENAME_KEY, fileName);
                intent.putExtra(
                        ContentActivity.CONTENT_PATH_KEY,
                        TheApplication.getPath(
                                fileName,
                                Util.getMD5Checksum(currentButton.getNode().getPath().getAbsolutePath())));
                startActivity(intent);
                return true;
            case R.id.deleteNode:
                getCurrentFragment().getTreeLayout().removeNode(currentButton.getNode().getPath());
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_map_operations, menu);
        if (fragments.isEmpty()) {
            findViewById(R.id.saveOption).setEnabled(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO: find solution (disabling the add button and remove button for now as displaying problem.)
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

//        if (id == R.id.actionAdd) {
//            MindTreeDialog dialog = new MindTreeDialog(this);
//            dialog.setOnMindTreeChosenListener(new MindTreeDialog.OnMindTreeChosenListener() {
//                @Override
//                public void onChosen(String path) {
//                    addFragment(path);
//                }
//            });
//            dialog.show();
//            return true;
//        }
//        if(id == R.id.actionRemove) {
//            removeCurrentFragment();
//            return true;
//        }
        if (id == R.id.saveOption) {
            getCurrentFragment().save();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
