package moe.gensokyoradio.liberty.mymind.content;

import android.content.Context;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.EditText;

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

public class HeadingEditText extends android.support.v7.widget.AppCompatEditText {
    public HeadingEditText(Context context) {
        super(context);
        initialize();
    }

    public HeadingEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    public HeadingEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize();
    }

    private void initialize() {
        this.setTextSize(this.getTextSize() * 2);
        this.getPaint().setFlags(Paint.FAKE_BOLD_TEXT_FLAG);
        this.setHint(R.string.heading_edit_text_hint);
    }
}
