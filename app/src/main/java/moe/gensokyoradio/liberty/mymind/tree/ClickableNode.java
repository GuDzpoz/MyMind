package moe.gensokyoradio.liberty.mymind.tree;

import android.content.Context;
import android.util.AttributeSet;

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

public class ClickableNode extends android.support.v7.widget.AppCompatButton {
    public ClickableNode(Context context) {
        super(context);
    }

    public ClickableNode(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ClickableNode(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private MyNode node;
    public void setNode(MyNode node) {
        this.node = node;
    }

    public MyNode getNode() {
        return node;
    }
}
