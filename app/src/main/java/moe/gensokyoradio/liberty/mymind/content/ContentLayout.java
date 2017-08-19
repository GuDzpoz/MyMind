package moe.gensokyoradio.liberty.mymind.content;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.ContextMenu;
import android.view.View;
import android.widget.LinearLayout;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import hu.scythe.droidwriter.DroidWriterEditText;

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

public class ContentLayout extends LinearLayout implements View.OnClickListener, View.OnCreateContextMenuListener {
    private OnClickListener clickListener;
    private OnCreateContextMenuListener contextMenuListener;

    public ContentLayout(Context context) {
        super(context);
    }

    public ContentLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ContentLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void addImage(String path) {
        LocalImageView imageView = new LocalImageView(getContext());
        imageView.setPath(path);
        addView(imageView);
    }
    public void addHeading() {
        HeadingEditText headingEditText = new HeadingEditText(getContext());
        addView(headingEditText);
    }
    private void addHeading(String heading) {
        HeadingEditText headingEditText = new HeadingEditText(getContext());
        headingEditText.setText(heading);
        addView(headingEditText);
    }
    public void addText() {
        DroidWriterEditText editText = new DroidWriterEditText(getContext());
        addView(editText);
    }
    private void addText(String text) {
        DroidWriterEditText editText = new DroidWriterEditText(getContext());
        editText.setTextHTML(text);
        addView(editText);
    }
    public void addLink() {
        LinkEditText editText = new LinkEditText(getContext());
        addView(editText);
        editText.performClick();
    }
    private void addLink(String text, String link) {
        LinkEditText editText = new LinkEditText(getContext());
        editText.setText(text);
        editText.setLink(link);
        addView(editText);
    }

    public void move(View child, int index) {
        this.removeView(child);
        this.addView(child, index);
    }

    @Override
    public void addView(View child) {
        LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 0, 0, 0);
        child.setLayoutParams(params);
        child.setOnClickListener(this);
        child.setOnCreateContextMenuListener(this);

        int index = getIndex(this.getFocusedChild());
        if(index == -1) {
            super.addView(child);
        }
        else {
            super.addView(child);
        }
    }

    public int getIndex(View view) {
        for(int i = 0; i != this.getChildCount(); ++i) {
            View child = this.getChildAt(i);
            if(child == view) {
                return i;
            }
        }
        return -1;
    }

    public void setOnContentClickedListener(OnClickListener listener) {
        this.clickListener = listener;
    }
    public void setOnContextCreatedListener(OnCreateContextMenuListener listener) {
        this.contextMenuListener = listener;
    }

    @Override
    public void onClick(View view) {
        if (clickListener != null) {
            clickListener.onClick(view);
        }
    }

    private static final Pattern LINK_PATTERN = Pattern.compile("^\\[([^\\]]*?)\\]\\(([^)]*?)\\)");
    private static final Pattern IMAGE_PATTERN = Pattern.compile("^!\\[([^\\]]*?)\\]\\(([^)]*?)\\)");
    public void load(String content) {
        BufferedReader reader = new BufferedReader(new StringReader(content));
        try {
            String line;
            while ((line = reader.readLine()) != null) {
                if(line.startsWith("#")) {
                    addHeading(line.substring(1));
                }
                else if(line.startsWith("[")) {
                    Matcher matcher = LINK_PATTERN.matcher(line);
                    if(matcher.find()) {
                        String text = matcher.group(1);
                        String link = matcher.group(2);
                        addLink(text, link);
                    }
                }
                else if(line.startsWith("!")) {
                    Matcher matcher = IMAGE_PATTERN.matcher(line);
                    if(matcher.find()) {
                        String text = matcher.group(1);
                        String path = matcher.group(2);
                        addImage(path);
                    }
                }
                else {
                    if(line.startsWith("\\")) {
                        line = line.substring(1);
                    }
                    line = line.replace("\\n", "\n");
                    addText(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String save() {
        StringBuilder builder = new StringBuilder(512);
        for(int i = 0; i != this.getChildCount(); ++i) {
            View view = this.getChildAt(i);
            if(view instanceof HeadingEditText) {
                builder
                        .append('#')
                        .append(((HeadingEditText)view).getText().toString())
                        .append('\n');
            }
            else if(view instanceof LinkEditText) {
                builder
                        .append('[')
                        .append(((LinkEditText)view).getText().toString())
                        .append("](")
                        .append(((LinkEditText)view).getLink())
                        .append(")\n");
            }
            else if(view instanceof LocalImageView) {
                builder
                        .append("![")
                        .append("image")
                        .append("](")
                        .append(((LocalImageView)view).getPath())
                        .append(")\n");
            }
            else if(view instanceof DroidWriterEditText){
                String content = ((DroidWriterEditText)view).getTextHTML().replace("\n", "\\n");
                if(!content.isEmpty()) {
                    switch (content.charAt(0)) {
                        case '#':
                        case '[':
                        case '!':
                        case '\\':
                            builder.append('\\');
                            default:
                                builder
                                        .append(content)
                                        .append('\n');
                    }
                }
                else {
                    builder.append('\n');
                }
            }
        }
        String result = builder.toString();
        if(result.length() > 0 && result.charAt(result.length() - 1) == '\n') {
            return result.substring(0, result.length() - 1);
        }
        else {
            return result;
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
        this.contextMenuListener.onCreateContextMenu(contextMenu, view, contextMenuInfo);
    }
}
