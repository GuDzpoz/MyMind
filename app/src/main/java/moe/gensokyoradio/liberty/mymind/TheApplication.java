package moe.gensokyoradio.liberty.mymind;

import android.app.Application;

import java.io.IOException;
import java.util.Hashtable;

import moe.gensokyoradio.liberty.mymind.tree.MyMindTree;

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

public class TheApplication extends Application {
/*    private Hashtable<String, MyMindTree> trees = new Hashtable<>();
    private Hashtable<String, Integer> userNumbers = new Hashtable<>();
    public MyMindTree getMindTree(String path) {
        plus(path);
        if(trees.containsKey(path)) {
            return trees.get(path);
        }
        else {
            try {
                MyMindTree tree = MyMindTree.fromJSON(Util.readAll(this, path));
                trees.put(path, tree);
                return tree;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    public void releaseMindTree(String path) {
        minus(path);
        if(!isBeingUsed(path)) {
            save(path);
            if(trees.containsKey(path)) {
                trees.remove(path);
            }
        }
    }

    public void save(String path) {
        try {
            Util.writeAll(this, path, this.getMindTree(path).toJSON());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean isBeingUsed(String path) {
        if(userNumbers.containsKey(path)) {
            if( userNumbers.get(path) == 0) {
                return false;
            }
            else {
                return true;
            }
        }
        else {
            return false;
        }
    }

    private void plus(String path) {
        if(userNumbers.containsKey(path)) {
            userNumbers.put(path, userNumbers.get(path) + 1);
        }
        else {
            userNumbers.put(path, 1);
        }
    }

    private void minus(String path) {
        if(userNumbers.containsKey(path)) {
            userNumbers.put(path, userNumbers.get(path) - 1);
        }
        else {
            userNumbers.put(path, 0);
        }
    }*/
}
