package moe.gensokyoradio.liberty.mymind.tree;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.List;

import moe.gensokyoradio.liberty.mymind.R;

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

public class PartitionLayout extends LinearLayout {
    public interface OnNodeClickListener {
        void onClick(View v, PartitionLayout layout, Button button, MyNode node);
    }

    public PartitionLayout(Context context) {
        super(context);
    }

    public PartitionLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public PartitionLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private MyNode node;
    private ClickableNode root;
    private LinearLayout childrenLayout;
    @Nullable
    private OnNodeClickListener listener;
    private Activity activity;

    public void setNode(MyNode n, OnNodeClickListener l, Activity activity) {
        listener = l;
        this.activity = activity;
        this.removeAllViews();

        this.setOrientation(HORIZONTAL);
        this.setGravity(Gravity.CENTER | Gravity.START);
        this.setFocusable(false);
        this.setBackgroundResource(R.drawable.textview_border);

        this.node = n;
        this.root = new ClickableNode(this.getContext());
        root.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        root.setNode(node);
        root.setMinHeight(80);
        root.setMinWidth(80);
        final PartitionLayout layout = this;
        root.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onClick(v, layout, root, node);
                }
            }
        });
        activity.registerForContextMenu(root);
        this.addView(root);

        childrenLayout = new LinearLayout(getContext());
        childrenLayout.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        childrenLayout.setOrientation(VERTICAL);
        childrenLayout.setFocusable(false);
        this.addView(childrenLayout);

        update();
    }

    public void addNode(MyPath path, MyNode node) {
        PartitionLayout current = this;
        List<String> paths = path.getPath();
        paths.remove(0); // "this"
        for (String part : paths) {
            current = getChild(current, part);
        }
        if (current != null) {
            if (getChild(current, node.getTitle()) == null) {
                node.setParent(current.node);
                current.node.getChildren().add(node);
                current.setNode(current.node, current.listener, current.activity);
            }
        }
    }

    public void removeNode(MyPath path) {
        PartitionLayout current = this;
        PartitionLayout parent = null;
        List<String> paths = path.getPath();
        paths.remove(0); // "this"
        for (String part : paths) {
            parent = current;
            current = getChild(current, part);
        }
        if (current != null) {
            if (current.node.getChildren().isEmpty()) {
                if (parent != null) {
                    parent.node.getChildren().remove(current.node);
                    parent.setNode(parent.node, parent.listener, parent.activity);
                }
            }
        }
    }

    private PartitionLayout getChild(PartitionLayout currentLayout, String title) {
        for (int i = 0; i != currentLayout.childrenLayout.getChildCount(); ++i) {
            PartitionLayout layout = (PartitionLayout) currentLayout.childrenLayout.getChildAt(i);
            if (layout.node.getTitle().equals(title)) {
                return layout;
            }
        }
        return null;
    }

    private void update() {
        root.setText(node.getTitle());

        childrenLayout.removeAllViews();
        for (MyNode child : node.getChildren()) {
            PartitionLayout childLayout = new PartitionLayout(getContext());
            childLayout.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
            childLayout.setNode(child, listener, activity);
            childrenLayout.addView(childLayout);
        }
    }

}
