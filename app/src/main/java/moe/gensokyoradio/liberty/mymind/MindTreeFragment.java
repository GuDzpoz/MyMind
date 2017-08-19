package moe.gensokyoradio.liberty.mymind;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.io.IOException;

import moe.gensokyoradio.liberty.mymind.tree.MyMindTree;
import moe.gensokyoradio.liberty.mymind.tree.MyNode;
import moe.gensokyoradio.liberty.mymind.tree.PartitionLayout;

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

public class MindTreeFragment extends android.support.v4.app.Fragment {
    private PartitionLayout treeLayout;
    private String path;
    private Context context;
    private MyMindTree tree;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        // TODO: 'View rootView = inflater.inflate(R.layout.fragment_mind_tree, container);' stack overflows
        View rootView = inflater.inflate(R.layout.fragment_mind_tree, null);
        treeLayout = rootView.findViewById(R.id.treeLayout);
        if (context != null) {
            try {
                tree = MyMindTree.fromJSON(Util.readAll(path));
            } catch (IOException e) {
                e.printStackTrace();
            }
            treeLayout.setNode(
                    tree.getRootNode(),
                    new PartitionLayout.OnNodeClickListener() {
                        @Override
                        public void onClick(View v, PartitionLayout layout, Button button, MyNode node) {
                            v.showContextMenu();
                        }
                    }, getActivity());
        }
        return rootView;
    }

    public void setMap(Context context, String path) {
        this.context = context;
        this.path = path;
    }

    public PartitionLayout getTreeLayout() {
        return treeLayout;
    }

    public MyMindTree getTree() {
        return tree;
    }

    public String getPath() {
        return path;
    }

    public void save() {
        try {
            Util.writeAll(path, tree.toJSON());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
