package moe.gensokyoradio.liberty.mymind.content;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.text.Spannable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

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

public class LinkEditText extends android.support.v7.widget.AppCompatEditText implements View.OnClickListener {
    private String text = "", link = "";
    private OnClickListener listener;

    public LinkEditText(Context context) {
        super(context);
        initialize();
    }

    public LinkEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    public LinkEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize();
    }

    private void initialize() {
        super.setOnClickListener(this);
        this.setHint(R.string.link_edit_text_hint);
        this.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        this.setTextColor(Color.BLUE);
    }

    @Override
    public void setOnClickListener(@Nullable OnClickListener l) {
        this.listener = l;
    }

    @Override
    public void onClick(View view) {
        final EditText urlEdit = new EditText(getContext());
        urlEdit.setHint(R.string.link_identifier);
        urlEdit.setText(link);
        final EditText textEdit = new EditText(getContext());
        textEdit.setText(text);

        LinearLayout layout = new LinearLayout(getContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.addView(urlEdit);
        layout.addView(textEdit);
        new AlertDialog.Builder(getContext())
                .setView(layout)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        link = urlEdit.getText().toString();
                        text = textEdit.getText().toString();
                        if(text.isEmpty()) {
                            text = link;
                        }
                        setText(text);
                    }
                })
                .show();
        urlEdit.requestFocus();

        if(listener != null) {
            listener.onClick(view);
        }
    }

    public String getLink() {
        return link;
    }

    public void setText(String text) {
        this.text = text;
        super.setText(text);
    }

    public void setLink(String link) {
        if(this.link.equals(text) || text == null || text.isEmpty()) {
            this.link = link;
            this.text = link;
        }
        else {
            this.link = link;
        }
    }
}
