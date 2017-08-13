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

import java.util.ArrayList;
import java.util.List;

public class MyPath {
    public static final String DEFAULT_SEPARATOR = "/";
    private List<String> path;
    public MyPath(MyPath parent, String child) {
        path = new ArrayList<>(parent.getPath());
        path.add(child);
    }
    public MyPath(List<String> path) {
        this.path = path;
    }

    public String getAbsolutePath() {
        return joinPath();
    }

    public List<String> getPath() {
        return path;
    }

    private String joinPath() {
        StringBuilder stringBuilder = new StringBuilder();
        for(String part : path) {
            stringBuilder.append(part).append(DEFAULT_SEPARATOR);
        }
        return stringBuilder.toString();
    }
}
