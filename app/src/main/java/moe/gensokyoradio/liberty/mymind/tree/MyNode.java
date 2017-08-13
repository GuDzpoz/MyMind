package moe.gensokyoradio.liberty.mymind.tree;
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

import android.support.annotation.Nullable;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class MyNode {
    @Nullable
    transient MyNode parent;
    String title;
    Map<String, String> attributes;
    List<MyNode> children;

    public MyNode(String title) {
        this.title = title;
        this.attributes = new Hashtable<>();
        this.children = new ArrayList<>();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public MyPath getPath() {
        MyNode parent = this;
        Deque<String> path = new ArrayDeque<>();
        path.add(this.getTitle());
        while ((parent = parent.getParent()) != null) {
            path.addFirst(parent.getTitle());
        }
        return new MyPath(new ArrayList<>(path));
    }

    @Nullable
    public MyNode getParent() {
        return parent;
    }

    public void setParent(@Nullable MyNode parent) {
        this.parent = parent;
    }

    public List<MyNode> getChildren() {
        return children;
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    public String toString() {
        return title;
    }
}
