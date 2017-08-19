package moe.gensokyoradio.liberty.mymind;

import android.app.Application;
import android.os.Environment;

import java.io.File;

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
    public static final String DEFAULT_STORAGE_PATH = Environment.getExternalStorageDirectory().getPath() + "/MyMinds/";
    public static String getPath(String mindMap, String fileName) {
        new File(DEFAULT_STORAGE_PATH + mindMap + "/").mkdirs();
        return DEFAULT_STORAGE_PATH + mindMap + "/" + fileName;
    }
}
